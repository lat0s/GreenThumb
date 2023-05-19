const express = require('express')
const mqtt = require('mqtt')
const fs = require('fs')
const path = require('path')

var options={
clientId:"NodeJSServer",
clean:true};

const mqttClient = mqtt.connect('mqtt:192.168.216.161',options)

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

let lastWriteTime = 0 //initialising variable for storing the last previous write time, so it can be compared to the interval below
const writeDelay = 60000 //Specify the interval in which entries are written to the data file

mqttClient.on('message', (topic, message) => {
  console.log(`Received message on topic ${topic}: ${message.toString()}`)
  
  //parses the incoming message from mqtt, splits the input string by every ","
  const [temperature, humidity, light, soilmoisture] = message.toString().split(',')
  
  //creates the json object
  const data = {
    timestamp: Date.now(), // get current unix timestamp (seconds after 1970)
    temperature: parseFloat(temperature),
    humidity: parseFloat(humidity),
    light: parseInt(light, 10),
    soilmoisture: parseInt(soilmoisture, 10)
  };
  
  const currentTime = Date.now()
  if(currentTime >= lastWriteTime + writeDelay) {
	  lastWriteTime = currentTime //update the last write time to now

  
  //converts json object to a string (how it will look like inside the json file)
  const dataStr = JSON.stringify(data, null, 2);
  
  const filePath = path.join('C:/Users/samth/greenThumbNodeJS/data', 'data.json'); 
  
  //code for dealing with errors when reading from the data file
  fs.readFile(filePath, {encoding: 'utf8'}, (err, fileData) => {
	  if (err) {
		  console.error('Error reading from file:', err)
	  }
	  
	  let json
	  if (fileData){
		  try {
			  json = JSON.parse(fileData) //converts to json object
		  } catch (e) {
			  console.error('Error parsing JSON:',e)
		  }
	  } else {  //if there is no data inside the file, initialise an array
		  json = []
	  }
	  if(!Array.isArray(json)) {
		  console.error('Existing data is not an array')
	  }
	  
	  json.push(data) //appends the new entry to the array
	  
	  
	  //writes the json data back to the file
	  fs.writeFile(filePath, JSON.stringify(json,null,2), (err) => {
		  if (err) {
			  console.error('Error writing to file:', err)
		  } else {
			  console.log('Wrote data to file:', filePath);
		  }
	  })
  })
  }
});


//end here


