package com.amazonaws.lambda.demo;
/**
 * Class will be triggered by Lambda function based on the S3 Event
 * This Class is reponsible to copy S3 item to another bucket
 */
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LamdaS3EventHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-east-1").build();
    public LamdaS3EventHandler() {}
    LamdaS3EventHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    public String handleRequest(S3Event event, Context context) {
    	context.getLogger().log("Received event: " + event);
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        s3.copyObject(bucket, key,"sr-img-trascode", key);
        try {
            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
            String contentType = response.getObjectMetadata().getContentType();
            context.getLogger().log("CONTENT TYPE: " + contentType);
            return contentType;
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
           
        }
		return key;
    }
}