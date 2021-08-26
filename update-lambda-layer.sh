#!/bin/bash
fullArn=$(aws lambda publish-layer-version --layer-name log-gold-rate-v2 --compatible-runtime java11 \
--zip-file fileb://build/distributions/stock-api-v2-layer.zip | grep LayerVersionArn)

# Setting IFS (input field separator) value as " "
IFS=' '

# Reading the split string into array
read -ra arr <<< "$fullArn"

arn=${arr[1]}

aws lambda update-function-configuration --function-name log-gold-rate-v2 --layers $arn
