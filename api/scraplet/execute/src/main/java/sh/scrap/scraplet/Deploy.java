package sh.scrap.scraplet;

import com.amazonaws.services.lambda.AWSLambdaAsync;
import com.amazonaws.services.lambda.AWSLambdaAsyncClient;
import com.amazonaws.services.lambda.runtime.Context;

import java.io.InputStream;
import java.io.OutputStream;

public class Deploy {

    private final AWSLambdaAsync lambda = new AWSLambdaAsyncClient();

    public void execute(InputStream input, OutputStream output, Context context) throws Exception {


    }

}
