#!/bin/bash
mvn clean package -DskipTests
mv ./target/cranfield-1.0.jar .