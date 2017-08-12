var express = require('express');
var router = express.Router();

var mongoose = require('mongoose');

var Model = require('../db/model');

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

router.get('/kelembaban', function(req, res) {

    var tanggal = Date.now();
    var nilai = req.param('nilai');

    mongoose.connect('mongodb://localhost:27017/smartgarden');
    var db = mongoose.connection;

    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {

        console.log('Database mongo connected!');

        var kelembabanBaru = new Model.Kelembaban({
            tanggal: tanggal,
            nilai: nilai
        });

        kelembabanBaru.save(function(err) {
            if (err) {

                db.close();

                throw err;
            }

            console.log('Nilai kelembaban pada ' + convertTanggal(tanggal) + ' saved successfully!');

            res.send(convertTanggal(tanggal) + ' Kelembaban saat ini adalah ' + nilai);

            db.close();
        });
    });
});

router.get('/siram', function(req, res) {
    var tanggal = Date.now();
    var status = req.param('status');

    mongoose.connect('mongodb://localhost:27017/smartgarden');
    var db = mongoose.connection;

    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {

        console.log('Database mongo connected!');

        var siramBaru = new Model.Siram({
            tanggal: tanggal,
            status: status
        });

        siramBaru.save(function(err) {
            if (err) {

                db.close();

                throw err;
            }

            console.log('Status siram pada ' + convertTanggal(tanggal) + ' saved successfully!');

            if(status == 1){
                res.send(convertTanggal(tanggal) + ' Tanaman disiram');
            } else if(status == 0){
                res.send(convertTanggal(tanggal) + ' Tanaman berhenti disiram');
            }

            db.close();
        });
    });
});

module.exports = router;