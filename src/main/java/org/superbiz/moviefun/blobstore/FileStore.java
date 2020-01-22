package org.superbiz.moviefun.blobstore;

import org.apache.tika.Tika;
import org.apache.tika.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static java.lang.ClassLoader.getSystemResource;
import static java.lang.String.format;
import static java.nio.file.Files.readAllBytes;

//@Component
public class FileStore implements BlobStore {

    @Override
    public void put(Blob blob) throws IOException {
        File targetFile = new File(blob.getName());
        targetFile.delete();
        targetFile.getParentFile().mkdirs();
        targetFile.createNewFile();

        try (FileOutputStream outputStream = new FileOutputStream(targetFile)) {
            outputStream.write(IOUtils.toByteArray(blob.getInputStream()));
        }
    }

    @Override
    public Optional<Blob> get(String name) throws IOException {
        File coverFile = new File(name);

        if (coverFile.exists()) {
            byte[] imageBytes = readAllBytes(coverFile.toPath());
            String contentType = new Tika().detect(coverFile.toPath());
            return Optional.of(new Blob(name,  new ByteArrayInputStream(imageBytes), contentType));
            }

        return Optional.empty();
    }

    @Override
    public void deleteAll() {
        // ...
    }



}