@echo off

start cmd /k "ipconfig && mosquitto -v -c "C:\Program Files\mosquitto\mosquitto.conf""
mosquitto_sub -t sensor/data