#include "TFT_eSPI.h"
#include "DHT.h"
#include <rpcWiFi.h>
#include "PubSubClient.h"

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

bool welcomeMessageReceived = false;

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
void displayWelcomeScreen(TFT_eSPI &tft) {
  String welcomeText = "Welcome!";
  
  tft.fillScreen(TFT_DARKGREEN);
  tft.setTextColor(TFT_WHITE); 
  tft.setTextSize(3);

  //Centering the welcome text
  int textWidth = welcomeText.length() * 18; 
  int xStart = (tft.width() - textWidth) / 2;
  tft.drawString(welcomeText, xStart, 120); 
  
  delay(3000); 

  tft.fillScreen(TFT_WHITE); 
}
void displayHeader(TFT_eSPI &tft) {
  tft.fillRect(0, 0, 320, 50, TFT_DARKGREEN);
  tft.setTextColor(TFT_WHITE);
  tft.setTextSize(3);  
  tft.drawString("Green Thumb", 50, 15);
  tft.drawFastVLine(150, 50, 190, TFT_DARKGREEN);
  tft.drawFastHLine(0, 140, 320, TFT_DARKGREEN);
}

void displayTemperature(TFT_eSPI &tft, int temperature) {
  tft.fillRect(15, 85, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber(temperature, 20, 90);
  tft.setTextSize(2);
  tft.drawString("C", 100, 95); 
}

void displayHumidity(TFT_eSPI &tft, int humidity) {
  tft.fillRect(15, 190, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber(humidity, 20, 195);
  tft.setTextSize(2);
  tft.drawString("%RH", 100, 200);
}

void displaySoilMoisture(TFT_eSPI &tft, int soilMoistureValue) {
  tft.fillRect(165, 85, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber(soilMoistureValue, 170, 90);
  tft.setTextSize(2);
  tft.drawString("%", 240, 95);
}

void displayLight(TFT_eSPI &tft, int light) {
  tft.fillRect(165, 190, 130, 30, TFT_WHITE);
  tft.setTextColor(TFT_BLACK);
  tft.setTextSize(3);
  tft.drawNumber(light, 170, 195);
  tft.setTextSize(2);
  tft.drawString("Lux", 240, 200);
}
// Callback function for MQTT messages
void callback(char* topic, byte* message, unsigned int length) {
  String messageTemp;
  
  for (int i = 0; i < length; i++) {
  messageTemp += (char)message[i];
  }
  if (String(topic) == "welcome/topic") {
    // Display welcome screen and sensor data if the welcome message is recieved
    displayWelcomeScreen(tft);
    displayHeader(tft); 
    tft.setTextColor(TFT_BLACK);
    tft.setTextSize(2);
    tft.drawString("Temperature", 10, 65); 
    tft.drawString("Humidity", 25, 160);
    tft.drawString("Soil moisture", 160, 65);
    tft.drawString("Light", 200, 160);

    // Read sensor values on the screen 
     int temperature = round(dht.readTemperature());
    int humidity = round(dht.readHumidity());
    int light = round(analogRead(WIO_LIGHT));
    int sensorValue = analogRead(soilMoistureSensorPin);
    int soilMoistureValue = map(sensorValue, 0, 1023, 0, 100);

    displayTemperature(tft, temperature);
    displayHumidity(tft, humidity);
    displaySoilMoisture(tft, soilMoistureValue);
    displayLight(tft, light);

    oldTemperature = temperature;
    oldHumidity = humidity;
    oldLight = light;
    oldSoilMoistureValue = soilMoistureValue;

    // Unsubscribe after the first welcome message
    if (!welcomeMessageReceived) {
        client.unsubscribe("welcome/topic");
        welcomeMessageReceived = true;
      }
    }
  }


void setup() {
  Serial.begin(9600);
  pinMode(WIO_LIGHT, INPUT);
  pinMode(WIO_BUZZER, OUTPUT);
  dht.begin();
  tft.begin();
  tft.setRotation(3);
  setup_wifi();
  client.setServer(mqtt_server, mqtt_port);
  client.setCallback(callback); // Set MQTT callback

}

void reconnect() {
  while (!client.connected()) {
    Serial.print("Attempting MQTT connection...");
    if (client.connect("ESP8266Client")) {
      Serial.println("connected");
      if (!welcomeMessageReceived) {
        client.subscribe("welcome/topic"); // Subscribe to the welcome topic
      }
      client.subscribe("sensor/data"); // Subscribe to the sensor data topic
    } else {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void loop() {
  if (WiFi.status() == WL_CONNECTED) {
    if (!client.connected()) {
      reconnect();
    }
    client.loop();
  }

 int temperature = round(dht.readTemperature());
 int humidity = round(dht.readHumidity());
 int light = round(analogRead(WIO_LIGHT));
 int sensorValue = analogRead(soilMoistureSensorPin);
 int soilMoistureValue = map(sensorValue, 0, 1023, 0, 100);

  if ((oldTemperature != temperature || oldHumidity != humidity || oldLight != light || oldSoilMoistureValue != soilMoistureValue) && welcomeMessageReceived==true) {
     displayTemperature(tft, temperature);
     displayHumidity(tft, humidity);
     displaySoilMoisture(tft, soilMoistureValue);
     displayLight(tft, light);
     
    if (WiFi.status() == WL_CONNECTED) {
      String payload = String(temperature) + "," + String(humidity) + "," + String(light) + "," + String(soilMoistureValue);
      Serial.println("Sensor data: "+ payload);
      client.publish("sensor/data", payload.c_str());
    }
    oldTemperature = temperature;
    oldHumidity = humidity;
    oldLight = light;
    oldSoilMoistureValue = soilMoistureValue;
  } 

  delay(500);
}

