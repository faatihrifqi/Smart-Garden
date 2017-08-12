var express = require('express');

var mongoose = require('mongoose');
var Schema = mongoose.Schema;

// create a kelembaban schema
var kelembabanSchema = new Schema({
    tanggal: Date,
    nilai: String
});
var Kelembaban = mongoose.model('Kelembaban', kelembabanSchema);

// create a siram schema
var siramSchema = new Schema({
    tanggal: Date,
    status: String
});
var Siram = mongoose.model('Siram', siramSchema);

module.exports = {
    Kelembaban: Kelembaban,
    Siram: Siram
};

