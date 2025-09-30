awslocal logs create-log-group --log-group-name local-lab-log-group

awslocal logs create-log-stream \
  --log-group-name local-lab-log-group \
  --log-stream-name local-lab-log-stream
echo "✅ CloudWatch Logs initialized successfully!"