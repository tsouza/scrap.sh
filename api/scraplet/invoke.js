'use script';

import java from 'java';

class InvokeScrapletService {

  handle(event, context) {
    var javaLangSystem = java.import('java.lang.System');
    javaLangSystem.out.printlnSync('Hello World');
    context.succeed();
  }

}

module.exports = new InvokeScrapletService();
