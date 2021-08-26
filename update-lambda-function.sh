#!/bin/bash
aws lambda update-function-code --function-name log-gold-rate-v2 \
--zip-file fileb://build/libs/stock-api-v2-1.0.jar
