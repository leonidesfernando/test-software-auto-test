#!/bin/bash

awslocal secretsmanager create-secret \
  --name "test-software/db-credentials" \
  --description "TestSoftware Database credentials on Localstack" \
  --region "eu-west-1" \
  --secret-string file:///etc/localstack/secrets/db-secrets.json
