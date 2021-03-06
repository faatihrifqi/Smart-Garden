var express = require('express');
var router = express.Router();

var mongoose = require('mongoose');

var Model = require('../db/model');

router.get('/', function(req, res, next) {

    mongoose.connect('mongodb://localhost:27017/smartgarden');
    var db = mongoose.connection;

    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {

        Model.Kelembaban.find({}, function(err, kelembabans) {
            var kelembabanMap = {};

            kelembabans.forEach(function(kelembaban) {
                kelembabanMap[kelembaban._id] = kelembaban;
            });

            res.send(kelembabanMap);

            db.close();
        });
    });
});

router.get('/latest', function(req, res, next) {

    mongoose.connect('mongodb://localhost:27017/smartgarden');
    var db = mongoose.connection;

    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {

        Model.Kelembaban.findOne().sort({'tanggal': -1}).exec(function(err, post) {

            res.send(post.nilai);

            db.close();
        });
    });
});

module.exports = router;