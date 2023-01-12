#!/bin/sh

# create a resource group in east coast
az group create --resource-group demo --location eastus

#create a Spring Apps service
az spring create --resource-group demo --name person

#create the app
az spring app create --resource-group demo --service person --name pdemo --assign-endpoint true \
  --runtime-version Java_17