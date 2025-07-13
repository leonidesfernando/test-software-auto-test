#!/bin/bash
echo "creating bucket"
awslocal s3api create-bucket --bucket initial-step-bucket
