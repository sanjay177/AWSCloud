package com.amazonaws.lambda.demo;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CreateBucketRequest;

public class LambdaFunctionHandler implements RequestHandler<String, String> {
	//private AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-1").build();

    public String handleRequest(String input, Context context) {
        context.getLogger().log("Input: " + input);
        return "Simple Lambda Function Which says Hello";
    }

}
