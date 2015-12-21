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
          "SET #Status = :validating |" +
          "ADD #Version :increment",
        ConditionExpression: "#Version = :version",
        ExpressionAttributeNames: {
          "#Status": "Status",
          "#Version": "Version"
        },
        ExpressionAttributeValues: {
          ":validating": { S: "VALIDATING" },
          ":version": { N: newVal.Version + "" },
          ":increment": { N: "1" }
        }
      })
      .then(() =>
        lambda.invokePromise({
          FunctionName: "scrap-sh-scraplet_validate",
          InvocationType: "RequestResponse",
          LogType: "Tail",
          Payload: JSON.stringify({
            apiKey: newVal.ApiKey,
            name: newVal.Name
          })}))
      .then((response) => JSON.parse(response.payload.toString()))
      .then((validation) =>
        dynamodb.updateItemAsync({
          TableName: "scraplet",
          Key: {
            ApiKey: { S: newVal.ApiKey },
            Name: { S: newVal.Name }
          },
          UpdateExpression:
            "SET #Status = :result |" +
            "ADD #Version :increment",
          ConditionExpression: "#Version = :version",
          ExpressionAttributeNames: {
            "#Status": "Status",
            "#Version": "Version"
          },
          ExpressionAttributeValues: {
            ":result": { S: validation.result === "VALID" ?
              "READY" : "INVALID" },
            ":version": { N: (newVal.Version + 1) + "" },
            ":increment": { N: "1" }
          }
        }))
}
