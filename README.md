# Smart-Garden

**Mongoose (Mongodb)**
1. Install Node.js
2. Install Mongodb<br />
    a. Pastikan telah dibuat file data/db di C: (untuk menyimpan file mongo database)<br />
    b. Pastikan mongod running saat hendak berinteraksi dengan mongo database (bisa di run menggunakan cmd dan biarkan terbuka, jangan di close)
3. Jalankan mongo.exe di terminal dan ketikkan ‘use smartgarden’ untuk membuat document/table bernama smartgarden
4. Jalankan Smart Garden Server

**MySQL**
1. Install Node.js
2. Install MySQL di server (dalam hal ini PC/Laptop). Pastikan database sudah terkonfigurasi dengan baik. Running di localhost:3306 (default)  

    ```mysql
    CREATE SCHEMA smartgarden;

    USE smartgarden;

    CREATE TABLE `smartgarden`.`kelembaban` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `tanggal` DATETIME NOT NULL,
      `nilai` VARCHAR(20) NOT NULL,
      PRIMARY KEY (`id`)
    );

    CREATE TABLE `smartgarden`.`siram` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `tanggal` DATETIME NOT NULL,
    `status` VARCHAR(1) NOT NULL,
    PRIMARY KEY (`id`)
    );

    CREATE TABLE `smartgarden`.`jadwal` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `tanggal` DATETIME NOT NULL, 
    `jam` VARCHAR(2) NOT NULL,
    `menit` VARCHAR(2) NOT NULL,
    PRIMARY KEY (`id`)
    );
    ```
3. Jalankan Smart Garden Server

<br /><br />
Homepage : http://localhost:3000/

**API**<br />
Siram : http://localhost:3000/api/siram?status={1, 2 atau 0}<br />
Ket : 1 menyiram dari arduino, 2 menyiram dari android, 0 berhenti menyiram dari android

Kelembaban : http://localhost:3000/api/kelembaban?nilai={nilai kelembaban}<br /> 
Jadwal : http://localhost:3000/api/jadwal?jam={7}&menit={30}

**GET ALL JSON DATA**<br />
Siram : http://localhost:3000/siram<br /> 
Kelembaban : http://localhost:3000/kelembaban 

**GET LATEST SINGLE JSON DATA**<br />
Siram : http://localhost:3000/siram/latest<br /> 
Kelembaban : http://localhost:3000/kelembaban/latest<br /> 
Jadwal : http://localhost:3000/jadwal/latest<br /> 
Jam : http://localhost:3000/jadwal/latest/jam<br />
Menit : http://localhost:3000/jadwal/latest/menit
