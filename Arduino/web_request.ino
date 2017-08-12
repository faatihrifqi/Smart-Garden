void upload_kelembaban(int kelembaban){
  // close any connection before send a new request.
  // This will free the socket on the WiFi shield
  client.stop();

  // if there's a successful connection:
  if (client.connect(server, 3000)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.print("GET /api/kelembaban?nilai=");
    client.print(String(kelembaban));
    client.println(" HTTP/1.1");
    client.print("Host: ");
    client.println(baseUrl);
    client.println("Connection: close");
    client.println();
  } else {
    // if you couldn't make a connection:
    Serial.println("connection failed");
  }
  
  if (client.available()) {
    char c = client.read();
    Serial.write(c);
  }
}

void upload_status_siram(int diSiram){
  //hanya dua kemungkinan, jika telah disiram berikan 1 otherwise 0
  // close any connection before send a new request.
  // This will free the socket on the WiFi shield
  client.stop();

  // if there's a successful connection:
  if (client.connect(server, 3000)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.print("GET /api/siram?status=");
    client.print(String(diSiram));
    client.println(" HTTP/1.1");
    client.print("Host: ");
    client.println(baseUrl);
    client.println("Connection: close");
    client.println();
  } else {
    // if you couldn't make a connection:
    Serial.println("connection failed");
  }
  
  if (client.available()) {
    char c = client.read();
    Serial.write(c);
  }
}

void dapatkan_jadwal_siram(){
  jam_jadwalSiram = http_request("GET /jadwal/latest/jam HTTP/1.1").toInt();
  menit_jadwalSiram = http_request("GET /jadwal/latest/menit HTTP/1.1").toInt();
}

String http_request(String getUrl){
  // close any connection before send a new request.
  // This will free the socket on the WiFi shield
  client.stop();

  // if there's a successful connection:
  if (client.connect(server, 3000)) {
    Serial.println("connected");
    // Make a HTTP request:
    client.println(getUrl);
    client.print("Host: ");
    client.println(baseUrl);
    client.println("Connection: close");
    client.println();
  } else {
    // if you couldn't make a connection:
    Serial.println("connection failed");
  }

  String data = "";
  if (client.available()) {
    char c = client.read();
    Serial.write(c);
    data += c;
  }

  return data;
}

