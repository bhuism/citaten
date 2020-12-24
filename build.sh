#!/bin/sh

mvn -B -Pnative clean spring-boot:build-image && docker push bhuism/citaten-api:latest

