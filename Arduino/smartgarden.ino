#include <Wire.h>
#include <Time.h>
#include <TimeLib.h> 
#include <DS1307RTC.h>
#include <SPI.h>
#include <Ethernet.h>

const char *monthName[12] = {
  "Jan", "Feb", "Mar", "Apr", "May", "Jun",
  "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
};

tmElements_t tm;

const int led = 8;
const int relay = 9;

const int soilSensor = A0;

int hariIni = 0;
int jam_jadwalSiram = 0;
int menit_jadwalSiram = 0;

const int lamaSiram = 3000; //dalam ms

int disiram = 0;

// assign a MAC address for the ethernet controller.
// fill in your address here:
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};
// fill in an available IP address on your network here,
// for manual configuration:
IPAddress ip(192, 168, 1, 177);

// initialize the library instance:
EthernetClient client;

IPAddress server(192,168,1,18);
int portInt = 3000;
String host = "192.168.1.18";
String port = "3000";
String baseUrl;

void setup() {
  // start serial port:
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for native USB port only
  }

  // give the ethernet module time to boot up:
  delay(1000);

  // start the Ethernet connection:
  if (Ethernet.begin(mac) == 0) {
    Serial.println("Failed to configure Ethernet using DHCP");
    // try to congifure using IP address instead of DHCP:
    Ethernet.begin(mac, ip);
  }

  baseUrl = "http://" + host + ":" + port;
  
  // print the Ethernet board/shield's IP address:
  Serial.print("My IP address: ");
  Serial.println(Ethernet.localIP());
  
  pinMode(led, OUTPUT);
  pinMode(relay, OUTPUT);
  pinMode(soilSensor, INPUT);
  digitalWrite(led, LOW);
  digitalWrite(relay, LOW);

  set_time();
}

void loop() {
  int kelembaban = analogRead(soilSensor);
  Serial.print("Kelembaban = ");
  Serial.println(kelembaban);

  if(kelembaban < 300){
    Serial.println("tanaman dalam keadan baik");
    digitalWrite(led, LOW);
    digitalWrite(relay, LOW);
  }else if(kelembaban < 1000){
    Serial.println("tanaman dalam keadadaan kering, melakukan penyiraman ...");
    siram();
  }else if(kelembaban >= 1000){
    Serial.println("Sensor is not in the Soil or DISCONNECTED");
    digitalWrite(led, LOW);
    digitalWrite(relay, LOW);
  }

  Serial.println(); //print empty line

  dapatkan_jadwal_siram();
  get_time();

  upload_kelembaban(kelembaban);

  //cek jika ada perintah siram dari android
  if(http_request("GET /siram/latest HTTP/1.1").toInt() == 2){
    digitalWrite(led, HIGH);
    digitalWrite(relay, HIGH);
  }else if(http_request("GET /siram/latest HTTP/1.1").toInt() == 0){
    digitalWrite(led, LOW);
    digitalWrite(relay, LOW);
  }
  
  delay(50);
}

void siram(){
  digitalWrite(led, HIGH);
  digitalWrite(relay, HIGH);
  delay(lamaSiram);
  digitalWrite(led, LOW);
  digitalWrite(relay, LOW);

  upload_status_siram(1);
}

