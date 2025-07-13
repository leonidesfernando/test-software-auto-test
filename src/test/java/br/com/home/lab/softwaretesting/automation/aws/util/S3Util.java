package br.com.home.lab.softwaretesting.automation.aws.util;

import br.com.home.lab.softwaretesting.automation.aws.s3.S3File;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.DeleteObjectResponse;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Object;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public final class S3Util {

    private S3Util(){}

    public static boolean bucketExists(S3Client s3Client, final String bucketName){
        return s3Client
                .listBuckets()
                .buckets()
                .stream()
                .anyMatch(b -> b.name().equals(bucketName));
    }

    public static void createBucketByName(S3Client s3Client, final String bucketName){
        CreateBucketRequest bucketRequest = CreateBucketRequest
                .builder()
                .bucket(bucketName)
                .build();
        s3Client.createBucket(bucketRequest);
    }

    public static void putObject(S3Client s3Client, String bucketName, String fileName, Path file){
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(fileName)
                        .build(),
                RequestBody.fromFile(file)
        );
    }

    public static S3File getObject(S3Client s3Client, String bucketName, String fileBucketName, Path retrievedFilePathName){
        GetObjectRequest objectRequest = GetObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileBucketName)
                .build();
        GetObjectResponse objectResponse = s3Client
                .getObject(objectRequest, ResponseTransformer.toFile(retrievedFilePathName));
        return new S3File(retrievedFilePathName.toAbsolutePath().toString(),objectResponse.contentType());
    }

    public static List<S3File> getAll(S3Client s3Client, String bucketName){
        ListObjectsV2Request request = ListObjectsV2Request
                .builder()
                .bucket(bucketName)
                .build();

        ListObjectsV2Response response;
        List<S3File> files = new ArrayList<>();
        do{
            response = s3Client.listObjectsV2(request);
            for(S3Object content : response.contents()){

            HeadObjectResponse headResponse = s3Client.headObject(HeadObjectRequest.builder()
                    .bucket(bucketName)
                    .key(content.key())
                    .build());
                files.add(new S3File(content.key(), headResponse.contentType()));
            }
        }while (response.isTruncated());
        return files;
    }

    public static boolean deleteObject(S3Client s3Client, String bucketName, String fileBucketName){
        DeleteObjectRequest request = DeleteObjectRequest
                .builder()
                .bucket(bucketName)
                .key(fileBucketName)
                .build();

        DeleteObjectResponse response = s3Client.deleteObject(request);
        return response.sdkHttpResponse().isSuccessful();
    }
}
