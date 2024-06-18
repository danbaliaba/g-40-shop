package de.ait_tr.g_40_shop.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import de.ait_tr.g_40_shop.service.Interfaces.FileService;
import de.ait_tr.g_40_shop.service.Interfaces.ProductService;
import liquibase.ObjectMetaData;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
public class FileServiceImpl implements FileService {

    private final AmazonS3 client;
    private final ProductService productService;

    public FileServiceImpl(AmazonS3 client, ProductService productService) {
        this.client = client;
        this.productService = productService;
    }


    @Override
    public String upload(MultipartFile file, String productTitle) {
        try {

            String uniqueFileName = generateUniqueFileName(file);

            ObjectMetadata metaData = new ObjectMetadata();
            metaData.setContentType(file.getContentType());

            PutObjectRequest request = new PutObjectRequest(
                    "cohort-40-bucket", uniqueFileName, file.getInputStream(), metaData
            ).withCannedAcl(CannedAccessControlList.PublicRead);

            client.putObject(request);

            String url = client.getUrl("cohort-40-bucket", uniqueFileName).toString();

            productService.attachImage(url, productTitle);

            return url;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String generateUniqueFileName(MultipartFile file){

        String sourceFileName = file.getOriginalFilename();
        int doiIndex = sourceFileName.lastIndexOf(".");
        String fileName = sourceFileName.substring(0, doiIndex);
        String extension = sourceFileName.substring(doiIndex);

        return String.format("%s-%s%s", file, UUID.randomUUID(), extension);
    }
}
