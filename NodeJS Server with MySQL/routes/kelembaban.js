var express = require('express');
var router = express.Router();

var mysql = require('mysql');

var connection = mysql.createConnection({
    host     : 'localhost',
    port     : 3306,
    user     : 'root',
    password : '',
    database : 'smartgarden'
});

connection.connect(function(err, client, done){
    if(!err) {
        console.log("Database is connected ... Kelembaban \n\n");

    } else {
        console.log("Error connecting database ... Kelembaban \n" + err.message);
    }
});

router.get('/', function(req, res, next) {

    var json = '';

    connection.query('SELECT * from kelembaban', function(err, results, fields) {
        if (!err){
            json = JSON.stringify(results);

            res.send(json);
        }
        else
            console.log('Error while performing Query.');
    });
});

router.get('/latest', function(req, res, next) {

    connection.query('SELECT * from kelembaban ORDER BY id DESC LIMIT 1', function(err, results, fields) {
        if (!err){

            res.send(results[0].nilai);
        }
        else
            console.log('Error while performing Query.');
    });

});

module.exports = router;