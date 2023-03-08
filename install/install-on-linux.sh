#!/bin/bash

echo '[Info] Installing Config Manager v1.0.0'
cd ..
./gradlew jar
cd build/libs
export CONFIG_MANAGER_DIRECTORY=${HOME}/ConfigManager
mkdir $CONFIG_MANAGER_DIRECTORY
cp config-manager.jar $CONFIG_MANAGER_DIRECTORY
echo '' >> ~/.bash_profile
echo 'alias cmanager="java -jar ${HOME}/ConfigManager/config-manager.jar"' >> ~/.bash_profile
echo '[Info] Config Manager has been successfully installed.'
echo '[Info] Now, you can use the command:'
echo '[Info] cmanager <option> <application> <profile>'
