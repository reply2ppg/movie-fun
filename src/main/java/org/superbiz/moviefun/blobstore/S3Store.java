package org.superbiz.moviefun.blobstore;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;


public class S3Store implements BlobStore {
    private AmazonS3Client s3Client;
    private String photoStorageBucket;

    public S3Store(AmazonS3Client s3Client, String photoStorageBucket) {
        this.s3Client = s3Client;
        this.photoStorageBucket = photoStorageBucket;
    }

    @Override
    public void put(Blob blob) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(blob.getContentType());
        s3Client.putObject(photoStorageBucket, blob.getName(), blob.getInputStream(), metadata);
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        if (s3Client.doesObjectExist(photoStorageBucket, name)) {
            S3Object s3ClientObject = s3Client.getObject(photoStorageBucket, name);

            Blob blob = new Blob(name, s3ClientObject.getObjectContent(), s3ClientObject.getObjectMetadata().getContentType());
            return Optional.of(blob);
        }
        return Optional.empty();
    }

    @Override
    public void deleteAll() {

    }
}
