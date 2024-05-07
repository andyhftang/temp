#!/bin/bash

# Set your deployment name and namespace
DEPLOYMENT_NAME="your-deployment-name"
NAMESPACE="your-namespace"

# Get the list of pods in the deployment
POD_LIST=$(kubectl get pods -n $NAMESPACE -l app=$DEPLOYMENT_NAME -o jsonpath='{.items[*].metadata.name}')

# Execute command in each pod
for POD in $POD_LIST; do
    echo "Executing command in pod $POD..."
    kubectl exec -n $NAMESPACE $POD -- your-command-to-execute
done
