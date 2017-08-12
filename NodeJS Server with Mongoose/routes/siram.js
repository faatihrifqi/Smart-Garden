var express = require('express');
var router = express.Router();

var mongoose = require('mongoose');

var Model = require('../db/model');

router.get('/', function(req, res, next) {

    mongoose.connect('mongodb://localhost:27017/smartgarden');
    var db = mongoose.connection;

    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {

        Model.Siram.find({}, function(err, sirams) {
            var siramMap = {};

            sirams.forEach(function(siram) {
                siramMap[siram._id] = siram;
            });

            res.send(siramMap);

            db.close();
        });
    });
});

router.get('/latest', function(req, res, next) {

    mongoose.connect('mongodb://localhost:27017/smartgarden');
    var db = mongoose.connection;

    db.on('error', console.error.bind(console, 'connection error:'));
    db.once('open', function() {

        Model.Siram.findOne().sort({'tanggal': -1}).exec(function(err, post) {

            res.send(post.status);

            db.close();
        });
    });
});

module.exports = router;