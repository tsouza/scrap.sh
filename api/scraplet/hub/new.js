import Promise from 'bluebird';
import crypto from 'crypto';

export default {
  on: "NULL.NEW",
  handler: (lambda, dynamodb, apiGateway, s3) =>
    (oldVal, newVal) =>
      dynamodb.updateItemAsync({
        TableName: "scraplet",
        Key: {
          ApiKey: { S: newVal.ApiKey },
          Name: { S: newVal.Name }
        },
        UpdateExpression:
          "SET #Status = :pending_validation " +
          "ADD Version :increment",
        ConditionExpression: "Version = :version AND #Status = :new",
        ExpressionAttributeNames: {
          "#Status": "Status",
        },
        ExpressionAttributeValues: {
          ":pending_validation": { S: "PENDING_VALIDATION" },
          ":new": { S: "NEW" },
          ":version": { N: newVal.Version + "" },
          ":increment": { N: "1" }
        }
      })
}
