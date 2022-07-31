#!/bin/bash

# Start minikube
minikube start

### Set docker env
eval $(minikube docker-env)             # unix shells
minikube docker-env | Invoke-Expression # PowerShell

### Load local Docker image into Minikube
minikube image load fp-microservice

### Build container directly in minikube
minikube image build -t fp-microservice .

### Apply app
kubectl apply -f app.yml

### Check that it's running
kubectl get pods
kubectl logs deployment.apps/fp-microservice







