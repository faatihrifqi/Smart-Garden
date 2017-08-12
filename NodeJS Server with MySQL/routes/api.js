var express = require('express');
var router = express.Router();

var mysql = require('mysql');

var convertTanggal = function (new_tanggal) {
    var a = new Date(new_tanggal);
    var months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
    var year = a.getFullYear();
    var month = months[a.getMonth()];
    var date = a.getDate();
    var hour = a.getHours();
    var min = a.getMinutes();
    var sec = a.getSeconds();
    return date + ' ' + month + ' ' + year + ' ' + hour + ':' + min + ':' + sec;
};

var connection = mysql.createConnection({
    host     : 'localhost',
    port     : 3306,
    user     : 'root',
    password : '',
    database : 'smartgarden'
});

connection.connect(function(err, client, done){
    if(!err) {
        console.log("Database is connected ... API \n\n");
    } else {
        console.log("Error connecting database ... API \n" + err.message);
    }
});

router.get('/kelembaban', function(req, res) {

    var tanggal = new Date().toISOString().slice(0, 19).replace('T', ' ');
    var nilai = req.param('nilai');

    var data  = {tanggal: tanggal, nilai: nilai};

    connection.query('INSERT INTO kelembaban SET ?', data, function(err, rows, fields) {
        if (!err) {
            console.log('Nilai kelembaban pada ' + convertTanggal(tanggal) + ' saved successfully!');
            res.send(convertTanggal(tanggal) + ' Kelembaban saat ini adalah ' + nilai);
        } else {
            console.log('The following error occurred while trying to insert a new record kelembaban ' + err.message);
        }
    });
});

router.get('/siram', function(req, res) {

    var tanggal = new Date().toISOString().slice(0, 19).replace('T', ' ');
    var status = req.param('status');

    var data  = {tanggal: tanggal, status: status};

    connection.query('INSERT INTO siram SET ?', data, function(err, rows, fields) {
        if (!err) {
            console.log('Status siram pada ' + convertTanggal(tanggal) + ' saved successfully!');
            if(status == 1 || status == 2){
                res.send(convertTanggal(tanggal) + ' Tanaman disiram');
            } else if(status == 0){
                res.send(convertTanggal(tanggal) + ' Tanaman berhenti disiram');
            }
        } else {
            console.log('The following error occurred while trying to insert a new record siram ' + err.message);
        }
    });
});

router.get('/jadwal', function(req, res) {

    var tanggal = new Date().toISOString().slice(0, 19).replace('T', ' ');
    var jam = req.param('jam');
    var menit = req.param('menit');

    var data  = {tanggal: tanggal, jam: jam, menit: menit};

    connection.query('INSERT INTO jadwal SET ?', data, function(err, rows, fields) {
        if (!err) {
            console.log('Jadwal penyiraman telah di rubah');
            res.send(convertTanggal(tanggal) + ' Jadwal penyiraman telah di rubah menjadi ' + jam + ':' + menit);
        } else {
            console.log('The following error occurred while trying to insert a new record jadwal ' + err.message);
        }
    });
});

module.exports = router;