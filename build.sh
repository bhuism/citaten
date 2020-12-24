#!/bin/sh

export COMMIT_SHA=`git rev-parse HEAD`
export SHORT_SHA=`git rev-parse --short HEAD`
export BRANCH_NAME=`git branch --show-current`

mvn -B -Pnative clean spring-boot:build-image && docker push bhuism/citaten-api:latest

cat ./target/classes/git.properties
