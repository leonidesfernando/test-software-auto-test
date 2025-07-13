#!/bin/bash
set -e  # Exit on error

REGION="eu-west-1"
echo "Using AWS region: $REGION"

echo "Using AWS region: $REGION"

echo "⏳ Creating IAM Role for Lambda..."
awslocal iam create-role --role-name lambda-execution-role \
  --assume-role-policy-document file:///etc/localstack/init/lambda-trust-policy.json \
   --region $REGION

echo "⏳ Deploying Lambda function..."
awslocal lambda create-function \
    --function-name my-lambda \
    --runtime python3.9 \
    --role arn:aws:iam::000000000000:role/lambda-execution-role \
    --handler lambda_function.lambda_handler \
    --timeout 10 \
    --memory-size 128 \
    --zip-file fileb:///opt/lambda/lambda_function.zip \
     --region $REGION

echo "✅ Lambda function deployed successfully!"
