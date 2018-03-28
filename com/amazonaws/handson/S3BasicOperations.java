package com.amazonaws.handson;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;


public class S3BasicOperations {

    public static void main(String[] args) throws IOException {

        /*
         * The ProfileCredentialsProvider will return your [default]
         * credential profile by reading from the credentials file
         */
        AmazonS3 s3 = AmazonS3ClientBuilder.standard().withCredentials(new ProfileCredentialsProvider()).withRegion("us-east-1").build();

        String bucketName = "tests3sr1" + UUID.randomUUID();
        String key = "newFileObject";
        StringBuilder buildString = new StringBuilder();

        System.out.println("===========================================");
        System.out.println("Getting Started with Amazon S3");
        System.out.println("===========================================\n");

        try {
            System.out.println("Creating bucket " + bucketName + "\n");
            s3.createBucket(bucketName);
            System.out.println("Listing buckets");
            //Use Of Java 8 ForEach to list the buckets 
            s3.listBuckets().forEach(bucket->System.out.println(bucket.getName()));
            
            //Uploading File
            System.out.println("Uploading a new object to S3 from a file\n");
            s3.putObject(new PutObjectRequest(bucketName, key, createSampleFile()));
            
            //Download File
            System.out.println("Downloading an object");
            S3Object object = s3.getObject(new GetObjectRequest(bucketName, key));
            System.out.println("Content-Type: "  + object.getObjectMetadata().getContentType());
            displayTextInputStream(object.getObjectContent());

           //List All Objects
            System.out.println("Listing objects");
            ObjectListing objectListing = s3.listObjects(new ListObjectsRequest().withBucketName(bucketName));
            objectListing.getObjectSummaries().forEach(summary->{
            	buildString.append(summary.getBucketName());
            	buildString.append(" "+summary.getKey());
            	buildString.append(" "+summary.getSize());
            });

            System.out.println(buildString.toString());

            //Delete Object 
            System.out.println("Deleting an object\n");
            s3.deleteObject(bucketName, key);

            //Delete Bucket
            System.out.println("Deleting bucket " + bucketName + "\n");
            s3.deleteBucket(bucketName);
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }


    private static File createSampleFile() throws IOException {
    	Path newFilePath = Paths.get("src/test/resources/newFile.txt");//File already Exist on my local, so no need to create new file
        return newFilePath.toFile();	
    }

    private static void displayTextInputStream(InputStream input) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        while (true) {
            String line = reader.readLine();
            if (line == null) break;

            System.out.println("    " + line);
        }
        System.out.println();
    }

}
