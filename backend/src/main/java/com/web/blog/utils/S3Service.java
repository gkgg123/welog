package com.web.blog.utils;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Component
public class S3Service {
    private AmazonS3 s3Client;

    private String accessKey= KEY.accessKey;

    private String secretKey=KEY.secretKey;

    private String bucket = "javaimg";
    private Regions region = Regions.AP_NORTHEAST_2;

    public S3Service() {
        AWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(region)
                .build();
    }

    public String upload(MultipartFile file, String iid) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/jpeg");
        metadata.setCacheControl("604800");
        s3Client.putObject(new PutObjectRequest(bucket, iid, file.getInputStream(),metadata)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return s3Client.getUrl(bucket, iid).toString();
    }




}
