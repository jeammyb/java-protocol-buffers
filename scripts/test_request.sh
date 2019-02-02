#!/bin/bash

# this script use curl to make a request test to the simple-app application.

curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"name":"Test","id":"1"}' \
  http://localhost:8080/api/person
