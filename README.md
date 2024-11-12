<div align="center">
<img src="https://github.com/lat0s/GreenThumb/blob/main/assets/logo.png?raw=true" width="128" height="128">
</div>

# Contents

1. [Introduction](#introduction)
2. [Features](#features)
3. [Prerequisites](#prerequisits)
4. [Purpose and Benefits of the App](#PurposeandBenefitsoftheapp)
5. [Hardware and Software Architecture](#HardwareandSoftwareArchitecture)
6. [Team Contributions](#ThemaincontributionsofeachMember)
7. [Installation Guide](#installation)
8. [Usage](#Usage)
9. [Team Information](#TeamInformation)
10. [Libraries User](#LibrariesUsed)
11. [Wiki](#wiki)

## Introduction

Green Thumb is a simple plant monitoring system designed to help plant enthusiasts of all levels better understand and care for their plants. By using a Wio Seeed Terminal with MQTT and Grove Sensors, Green Thumb allows users to monitor their plants' temperature, soil moisture, and light conditions, providing care recommendations and notifications to help them provide the optimal conditions for their plants.

## Features

- **Plant Database**: Discover a vast collection of plants with detailed information in reagrds to suggested care conditions they should be kept in and some plant specific recommendations for plant care.
- **Care Recommendations**: Get personalized care recommendations for your plants based on the specified environment you want them in compared to the real time enviroment they are in.
- **Plant Monitoring**: Track your plants' growth and health by tracking their growing conditions in real time.
- **Dark Mode**: Enjoy a more comfortable viewing experience with the added option to switch to dark mode.
- **Settings**: Take the control over your MQTT connection from inside the settings, get the access to our carefully created plant database, access our application tutorial, change the app theme and a quick link to email us about your queries and feeback of the app.
- **Tutorial**: Learn how to use the app and care for your plants with a step-by-step tutorial.
- **Contact Support**: Have a question or issue? Contact our support team for fast and friendly assistance.
- **Feedback**: Share your thoughts and suggestions to help us improve the app and provide the best experience possible.

## Prerequisites

- The system only runs on **compatible android devices** (latest version preferred)
- The terminal, server and android app require a **stable internet connection** (must be on the same wifi network)
- The grove **sensors must be placed appropriately** in the plants environment
- The machine running the server must have Mosquitto and NodeJS installed

## Purpose and Benefits of the app

The purpose of this app is to help users improve care for their respective plants by providing a monitoring system with real-time data transfer from sensors, and care notifications based on this data.

The Benefits are:

• The app provides the user with real time sensor data including the – current Temperature, Humidity, Soil Moisture, Light conditions.

• Users can create personalised Plant profiles, with names, icons, and customised ranges for normal conditions.
.
• Customised notifications, allowed by the customised ranges for a plant’s normal conditions. Notifications are only sent when sensor readings go beyond their specified range.

• Access to a plant database. Found through the settings page, which leads users to a pdf file created by the developers with the aim of providing users with recommended care conditions for various plants. Providing support for users needing additional information about their plant to customise its care conditions.

• Users can connect to different MQTT Servers, thus, connecting to different sensors by the click of a button and changing the configuration servers inside the app settings.

• A dark theme for low light environments and for those simply preferring a darker screen.

• Access to the data history recorded by each sensor with the click of a button, thus, having a history of conditions the plant has been kept in.

## Hardware and Software Architecture

![System Design](https://github.com/lat0s/GreenThumb/blob/main/assets/systemdesign.png)

**Hardware Architecture:** <br>
The project’s hardware architecture consists of the following components:

A Seeduino WIO terminal, the system’s main control element, it is a microcontroller with integrated Wi-Fi useful for IoT applications. It controls sensor data processing, Wi-Fi network connectivity, and MQTT server management. It manages communication with external MQTT servers, enabling the system to send and receive commands from a distance.

Grove Sensors: Grove sensors are sensors that can be integrated with the Seeduino Wio terminal.The system makes use of these sensors thanks to which the user is able to monitor temperature, soil moisture and light condition.The DHT11 grove module sensor measures the humidity and temperature of the surrounding air, the WIO recieves this data and displays on the TFT.

Furthermore, the system also includes a soil moisture sensor. Users may easily view real-time sensor readings for temperature, humidity, soil moisture and light on the Wio terminals TFT display, a graphical user interface. The collected sensor data is essentials for applications including plant monitoring or controlled watering system features.
Overall, the project's hardware architecture allows for effective data collecting, processing, and visualisation, therefore offering a complete solution.

**Software Architecture** <br>

![System Design](https://github.com/lat0s/GreenThumb/blob/main/assets/Android_App.png)

The project’s software architecture consists of the following components:

An Android app, the front end of the entire system, is an android appliation build just for the user to control and manage its plants. The android application consists connection setup to the MQTT/Mosquito broker and a connection to the NodeJS server to display the historic app data. It allows user to create and manage various plant objects in the app.

A NodeJS Server, a server used to store the historic sensor data from the terminal that is eventually displayed on the app for the user to keep the track of the exact conditions the plant has been kept in throughout. The data from the terminal is sent to the broker which then sends it to the NodeJS server to store.

MQTT/Mosquito, a broker user to communicate between almost the entire system transmitting data throughout and helping things to flow in the right way. It is the centre most component of out system.

Furthermore, the code uploaded on the microcontroller plays an important part in the terminal to work in the right way, thus making sure that the things are good from the get go.

## The main contributions of each Member

**Georgios Panormitis Latos** <br>
George Was responsible for a majority portion of the code inside the app and had significant contributions to the hardware code. He implemented various design patterns, the app background and most of the app’s UI. Additionally, he helped various other group members with coding issues. He had a major role as the go-to person for help in issues of all sorts, be it for the front end or the backend of the project. He consistently presented with solutions to unforeseen problems and could be described as the pillar of the project.

**Sam Hardingham** <br>
Sam took care of git, created issues and helped all group members to solve git related problems. He was also responsible for creating the first skeleton of the app, after creating a Figma design of the app’s UI. Furthermore, he established to connect to a real-time server that stores the sensor data, thus, providing the user with a history of the plant’s conditions. Sam also worked on the Wiki page of the project in its entirety, had some key contributions in the ReadME file of the project and made the app turorial and functionality video.

**Nishchya Arya** <br>
Nishchya took care of managerial duties for the group, while having a significant contribution towards the front end of the app. He created the settings activity, implementing features such as Different themes, MQTT settings from the app and creating an app tutorial. He also had a major contribution in app's planning stage, and the weekly checkins with the TAs, while simultaneously addressing conflicts and bringing problems onto notice. Nishchya also worked on the app documention including the diagrams and made the ReadMe file almost entirely.

**Mesimaaria Alastalo** <br>
Mesimaaria was responsible for the notification feature (as a whole) through the existing MQTT connection, comparing the received sensor data to the users desired care conditions. She created the UI for notifications and contributed significantly towards the applications documentation. She also actively participated with in-person discussions, proving as a notably valuable asset by making key points to arguments and steering the project. Her main hardware contribution was refactoring Mohammad’s original Arduino code for the sensors and the terminal.

**Simone Graziosi** <br>
Simone took care of the MQTT connection towards the app, and played a significant role in coding and refactoring the hardware code. He had a very important role in the brainstorming process of the application as a whole and participated actively in team meetings.

**Mohammad Mohammad** <br>
Mohammad made nessecary changes to the wiki page.He edited the Home page , made open Resources , app features and design pages. He also made and Organized labels, researched mqtt, Received and connected sensors and hardware. Educated other team members about hardware connectivity. His main contribution was sensors data history feature in the app.

## Installation

**Android Studio Setup:**

- Download and install Android Studio from the official website (https://developer.android.com/studio).
- Follow the installation wizard and configure Android Studio according to your system requirements.
- Once installed, open Android Studio and set up the necessary SDKs and emulators.
- Clone the code from the GitLab.

**Arduino IDE Setup:**

- Download and install the Arduino IDE from the official website (https://www.arduino.cc/en/software).
- Follow the installation instructions for your specific operating system.
- Launch the Arduino IDE once installed.
- Open the sketch contained in the "Arduino" folder of the project repository
- Change the wifi credentials at the top to correspond to your home wifi

**Wio Terminal Setup:**

- Connect your Wio Terminal to your computer using a USB cable.
- Install the necessary drivers for the Wio Terminal if prompted by your operating system.

**Sensor Connections:**

- Identify the pins on your Wio Terminal for the Humidity, Temperature, Moisture, and Light sensors.
- Connect the sensors to the appropriate pins on the Wio Terminal, ensuring proper wiring and connections.

**Library Installation inside the Arduino:**

- Open the Arduino IDE.
- Go to "Sketch" > "Include Library" > "Manage Libraries".
- Search for and install the required libraries for your sensors and any additional functionality you need.
- Close the library manager once the installation is complete.

**GitHub Repository:**

- Clone repository from GitHub.

**NodeJS**

- Open the server.js file.
- Change the mqtt.connnect to your machine's IPV4 address.
- (Line 10 of server.js file)

**Mosquitto**

- Run the startMQTT batch file in the greenThumbNodeJS folder
- Make sure the code in the batch file is directed to your mosquitto installation folder

**Cloning the Project:**

- Open a terminal or command prompt on your computer.
- Navigate to the directory where you want to clone the project.
- Run the following command to clone the project repository: git clone <repository-url>
- Replace <repository-url> with the URL of your GitLab repository.

**Running the Project:**

- Open the cloned project in Android Studio.
- Connect your Android device to your computer or start an emulator.
- Build the project by clicking on the "Build" button in Android Studio.
- Run the project by clicking on the "Run" button in Android Studio.
- Follow the on-screen instructions to deploy the app on your Android device or emulator.

## Usage

- 1. Upload the Arduino code onto your Wio Terminal
- 2. Start the MQTT server with the startMQTT batch file
- 3. Start the nodeJS server with the startServer batch file
- 4. Open the application on your android device and input your mqtt credentials in the settings page
- 5. You will receive a confirmation message on the app and welcome screen on the terminal if everything is connected!

## Team Information

- Georgios Panormitis Latos | [latos](https://git.chalmers.se/latos)
- Sam Hardingham | [samha](https://git.chalmers.se/samha)
- Mohammad Mohammad | [mohamoh](https://git.chalmers.se/mohamoh)
- Mesimaaria Alastalo | [alastalo](https://git.chalmers.se/alastalo)
- Nishchya Arya | [nishchya](https://git.chalmers.se/nishchya)
- Simone Graziosi | [graziosi](https://git.chalmers.se/graziosi)

## Libraries Used

The following libraries were used in the development of the entire system. (Might be outdated)

- 'androidx.recyclerview:recyclerview:1.3.0'
- 'com.google.code.gson:gson:2.8.9'
- 'io.github.florent37:shapeofview:1.4.7'
- "androidx.viewpager2:viewpager2:1.0.0"
- 'com.google.android.material:material:1.8.0'
- 'androidx.appcompat:appcompat:1.4.1'
- 'org.eclipse.paho:org.eclipse.paho.client.mqttv3:1.1.0'
- 'org.eclipse.paho:org.eclipse.paho.android.service:1.1.1'
- 'androidx.multidex:multidex:2.0.1'
- 'androidx.localbroadcastmanager:localbroadcastmanager:1.0.0'
- 'androidx.constraintlayout:constraintlayout:2.1.4'
- 'junit:junit:4.13.2'
- 'androidx.test.ext:junit:1.1.5'
- 'androidx.test.espresso:espresso-core:3.5.1'

### Project status - Completed

## [Presentation Video](https://www.youtube.com/watch?v=ro6_CHGE1a4)

## [Wiki](https://git.chalmers.se/courses/dit113/2023/group-2/group-2/-/wikis/home)

<div align="center">
<img src="assets\footer.svg">
</div>
