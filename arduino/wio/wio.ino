#include "TFT_eSPI.h"
#include "DHT.h"
#include <rpcWiFi.h>
#include "PubSubClient.h"

// WiFi and MQTT credentials
const char* ssid = "";
const char* password = "";
const char* mqtt_server = "";
const int mqtt_port = 1883;

WiFiClient espClient;
PubSubClient client(espClient);


#define DHTPIN PIN_WIRE_SCL
#define DHTTYPE DHT11
DHT dht(DHTPIN, DHTTYPE);

TFT_eSPI tft;
const uint8_t soilMoistureSensorPin = A0;
uint8_t soilMoistureValue = 0;

int oldTemperature = -1;
int oldHumidity = -1;
int oldLight = -1;
int oldSoilMoistureValue = -1;

void setup() {
  Serial.begin(9600);
  pinMode(WIO_LIGHT, INPUT);
  pinMode(WIO_BUZZER, OUTPUT);
  
  dht.begin();
  tft.begin();
  tft.setRotation(3);
  displayHeader(tft);
  WiFi.begin(ssid, password);
  client.setServer(mqtt_server, mqtt_port);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(2);
  tft.drawString("Temperature", 10, 65); 
  tft.drawString("Humidity", 25, 160);
  tft.drawString("Soil moisture", 160, 65);
  tft.drawString("Light", 200, 160);
}


void setup_wifi() {
  delay(10);
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

// REFACTORED OLD DISPLAY FUNCTION TO USE LESS RAM - Georgios
void displayHeader(TFT_eSPI &tft) {
  tft.fillRect(0, 0, 320, 50, TFT_DARKGREEN);
  tft.setTextColor(TFT_WHITE);
  tft.setTextSize(3);  
  tft.drawString("Green Thumb", 50, 15);
  tft.drawFastVLine(150, 50, 190, TFT_DARKGREEN);
  tft.drawFastHLine(0, 140, 320, TFT_DARKGREEN);
}

void displayData(int temperature,int humidity,int soilMoistureValue,int light) {
  // Display Temperature
  tft.fillRect(15, 85, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber((int)temperature, 20, 90);
  tft.setTextSize(2);
  tft.drawString("°C", 100, 95); 
  // Display Humidity
  tft.fillRect(15, 190, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber((int)humidity, 20, 195);
  tft.setTextSize(2);
  tft.drawString("%RH", 100, 200);
  // Display Moisture
  tft.fillRect(165, 85, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber(soilMoistureValue, 170, 90);
  tft.setTextSize(2);
  tft.drawString("%", 240, 95);
  // Display Light
  tft.fillRect(165, 190, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber(light, 170, 195);
  tft.setTextSize(2);
  tft.drawString("Lux", 240, 200);
}



void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    if (!client.connected()) {
      reconnect();
    }

    client.loop();
  }
  else{
    Serial.println("Wi-Fi not connected");
  }
  

  int temperature = (int) dht.readTemperature();
  int humidity = (int) dht.readHumidity();
  int light = analogRead(WIO_LIGHT);
  int sensorValue = map(analogRead(soilMoistureSensorPin), 0, 1023, 0, 100);
  
  if (oldTemperature != temperature || oldHumidity != humidity || oldLight != light || oldSoilMoistureValue != sensorValue) {
    if (WiFi.status() == WL_CONNECTED) {
      // Publish to MQTT
      String payload = String(temperature) + "," + String(humidity) + "," + String(light) + "," + String(sensorValue);
      client.publish("sensor/data", payload.c_str());
    }
	
	displayData(temperature,humidity,sensorValue,light);

  
    oldTemperature = temperature;
    oldHumidity = humidity;
    oldLight = light;
    oldSoilMoistureValue = sensorValue;
  }
  delay(500);
}