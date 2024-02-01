#!/bin/bash

# Source environment variables from .env file
if [ -f .env ]; then
    source .env
else
    echo "Error: .env file not found."
    exit 1
fi

# Remove existing classes
rm -f *.class

# Recompile existing Java files
javac *.java

# Run SasquatchWeather.java
java SasquatchWeather
