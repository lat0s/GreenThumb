const express = require('express')
const mqtt = require('mqtt')
const fs = require('fs')

var options={
clientId:"NodeJSServer",
clean:true};

const mqttClient = mqtt.connect('mqtt:192.168.216.192',options)

mqttClient.on('connect',function () {
  console.log("connected")
  
  mqttClient.subscribe('sensor/data', function(err) {
    if (err) {
      console.error('Error subscribing to topic:', err);
    } else {
      console.log('Subscribed to topic: sensor/data');
    }
  });
});

mqttClient.on('message', (topic, message) => {
  console.log(`Received message`)
});


//end here






