var AWS = require("aws-sdk"), Promise = require("bluebird"),
    async = require("async");

var s3 = Promise.promisifyAll(new AWS.S3()),
    firebase = require("./firebase");

var updateQueue = async.queue(updatePriority, 1),
    currentPriority;

getLatestPriority()
  .then(function(latestPriority) {
    currentPriority = latestPriority;
    return firebase.child("scraplets")
      .orderByPriority()
      .startAt(latestPriority);
  })
  .then(function(ref) {
    ref.on("child_added", enqueuePriorityUpdate);
    ref.on("child_added", function(snapshot) {
      
    });

    ref.on("child_removed", function(snapshot) {

    });
  });

function enqueuePriorityUpdate(snapshot) {
  var priority = snapshot.getPriority();
  if (!currentPriority || priority > currentPriority)
    updateQueue.push(currentPriority = priority);
}

function updatePriority(priority, callback) {
  setLatestPriority(priority)
    .finally(callback);
}

function getLatestPriority() {
  return s3.getObject({
    Bucket: "scrap.sh",
    Key: "scraplet-events-priority"
  }).then(function(data) {
    return +(data.body || "0").toString();
  }).catch(function(err) {
    return setLatestPriority(0)
      .return(0);
  });
}

function setLatestPriority(priority) {
  return s3.putObject({
    Bucket: "scrap.sh",
    Key: "scraplet-events-priority",
    Body: "" + priority;
  });
}
