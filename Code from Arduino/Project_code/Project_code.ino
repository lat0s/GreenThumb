//Libraries 
#include "TFT_eSPI.h" //TFT LCD library 
#include "DHT.h" // DHT sensor library 

//Definitions
//#define DHTPIN 0 //Define signal pin of DHT sensor 
#define DHTPIN PIN_WIRE_SCL //Use I2C port as Digital Port */
#define DHTTYPE DHT11 //Define DHT sensor type 

//Initializations
DHT dht(DHTPIN, DHTTYPE); //Initializing DHT sensor
TFT_eSPI tft; //Initializing TFT LCD
TFT_eSprite spr = TFT_eSprite(&tft); //Initializing buffer

//Define the pin for the soil moisture sensor and its value
const int soilMoistureSensorPin = A0; //Define variable to store soil moisture sensor pin
int soilMoistureValue = 0; //Define variable to store soil moisture sensor value

const int BUZZER_PIN = WIO_BUZZER; //Define buzzer pin

void setup() {

  Serial.begin(9600); //Initialize serial communication
  pinMode(WIO_LIGHT, INPUT); //Set light sensor pin as INPUT
  pinMode(WIO_BUZZER, OUTPUT); //Set buzzer pin as OUTPUT
  
  dht.begin(); //Start DHT sensor 
  tft.begin(); //Start TFT LCD
  tft.setRotation(3); //Set TFT LCD rotation
  spr.createSprite(TFT_HEIGHT,TFT_WIDTH); //Create buffer
}

  // Display the header
void displayHeader() {
  spr.fillSprite(TFT_WHITE); //Fill background with white color
  spr.fillRect(0, 0, 320, 50, TFT_DARKGREEN); //Rectangle fill with dark green 
  spr.setTextColor(TFT_WHITE); //Setting text color
  spr.setTextSize(3);  //Setting text size 
  spr.drawString("Green Thumb", 50, 15); //Drawing a text string 
  spr.drawFastVLine(150, 50, 190, TFT_DARKGREEN); //Drawing a vertical line
  spr.drawFastHLine(0, 140, 320, TFT_DARKGREEN); //Drawing a horizontal line
}

// Display the temperature
void displayTemperature(float temperature) {
  spr.setTextColor(TFT_BLACK);
  spr.setTextSize(2);
  spr.drawString("Temperature", 10, 65);
  spr.setTextSize(3);
  spr.drawNumber(temperature, 50, 95); //Display temperature values
  spr.drawString("C", 90, 95);
}
// Display the humidity
void displayHumidity(float humidity) {
  spr.setTextSize(2);
  spr.drawString("Humidity", 25, 160);
  spr.setTextSize(3);
  spr.drawNumber(humidity, 30, 190); //Display humidity values
  spr.drawString("%RH", 70, 190);
}

// Display the soil moisture
void displaySoilMoisture(int soilMoistureValue) {
  spr.setTextSize(2);
  spr.drawString("Soil moisture", 160, 65);
  spr.setTextSize(3);
  spr.drawNumber(soilMoistureValue, 200, 95); //Display sensor values as percentage  
  spr.drawString("%", 260, 95);
}

// Display the light
void displayLight(int light) {
  spr.setTextSize(2);
  spr.drawString("Light", 200, 160);
  spr.setTextSize(3);
  spr.drawNumber(light, 205, 190);
  spr.drawString("%", 245, 190);
}

void loop() {
  float temperature = dht.readTemperature(); //Assign variable to store temperature 
  float humidity = dht.readHumidity(); //Assign variable to store humidity 
  int light = analogRead(WIO_LIGHT); //Assign variable to store light sensor values

  //Read soil moisture sensor
  int sensorValue = analogRead(soilMoistureSensorPin);
  int soilMoistureValue = map(sensorValue, 1023, 0, 0, 100); //Convert sensor value to percentage
  

   displayHeader(); // call the displayHeader() function
   displayTemperature(temperature); // call the displayTemperature() function
   displayHumidity(humidity); // call the displayHumidity() function
   displaySoilMoisture(soilMoistureValue); //call the displaySoilMoisture() function
   displayLight(light); //call the displayLight() function

     //Condition for low soil moisture, if it's too low, beep the buzzer
  if(soilMoistureValue < 50){
    spr.fillSprite(TFT_RED);
    spr.drawString("Water your plant!",17,100);
    analogWrite(WIO_BUZZER, 150); //beep the buzzer
    delay(1000);
    analogWrite(WIO_BUZZER, 0); //Silence the buzzer
    delay(1000);
  }

  spr.pushSprite(0,0); //Push to LCD
  delay(50);


}

//The following source was used as an inspiration for this code
//Source: https://www.youtube.com/watch?v=NQt-XLcSIwA