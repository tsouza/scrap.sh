import Promise from 'bluebird';
import AdmZip from 'adm-zip';

export default {
  on: "new",
  listener: (lambda, dynamodb, apiGateway, s3) => {

    var getCodebase = () => s3.getObjectAsync({
      Bucket: "scrap.sh",
      Key: "codebase/service.jar"
    }).then((response) => response.data);

    var makeZip = (scraplet) =>
      (codebase) => {
        var zip = new AdmZip();
        zip.addFile("lib/service.jar", codebase);
        zip.addFile("META-INF/script", new Buffer(scraplet.script));
        return zip.toBuffer();
      }

    return (oldVal, newVal) =>
      getCodebase()
        .then(makeZip(newVal))
        .then((codebase) =>
            lambda.createFunction({
              Code: { ZipFile: codebase },
              FunctioName: newVal.apiKey + "__" + newVal.name,
              Handler: "sh.scrap.scraplet.ScrapletService::handle"
            })
        );
  }
}
