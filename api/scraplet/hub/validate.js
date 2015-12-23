import Promise from 'bluebird';
import crypto from 'crypto';

export default {
  on: "*.PENDING_VALIDATION",
  handler: (lambda, dynamodb, apiGateway, s3) =>
    (oldVal, newVal) =>
      lambda.invokePromise({
        FunctionName: "scrap-sh-scraplet_validate",
        InvocationType: "RequestResponse",
        LogType: "Tail",
        Payload: JSON.stringify({ Script: newVal.Script })
      })
      .then((response) => JSON.parse(response.Payload.toString()))
      .then((validation) =>
        dynamodb.updateItemAsync({
          TableName: "scraplet",
          Key: {
            ApiKey: { S: newVal.ApiKey },
            Name: { S: newVal.Name }
          },
          UpdateExpression:
            "SET #Status = :result " +
            "ADD Version :increment",
          ConditionExpression:
            "Version = :version AND #Status = :pending_validation",
          ExpressionAttributeNames: {
            "#Status": "Status",
          },
          ExpressionAttributeValues: {
            ":result": { S: validation.Result },
            ":pending_validation": { S: "PENDING_VALIDATION" },
            ":version": { N: newVal.Version + "" },
            ":increment": { N: "1" }
          }
        }))
}
