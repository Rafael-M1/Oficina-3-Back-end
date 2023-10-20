package br.edu.ifmt.cba.ifmthub.services;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobItem;

@Service
public class MyBlobService {
	
	private static Logger logger = LogManager.getLogger(MyBlobService.class);
	
	@Value("${blob.storage.connectionstring}")
	private String connectionString;

	public BlobContainerClient containerClient(String azureBlobContainer) {
        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString).buildClient();
        BlobContainerClient container = serviceClient.getBlobContainerClient(azureBlobContainer);
        return container;
    }
	
	public List<String> listFiles(String azureBlobContainer) {
        BlobContainerClient container = containerClient(azureBlobContainer);
        List<String> list = new ArrayList<>();
        for (BlobItem blobItem : container.listBlobs()) {
            list.add(blobItem.getName());
        }
        return list;
    }
	
	public ByteArrayOutputStream downloadFile(String blobitem, String azureBlobContainer) {
        BlobContainerClient containerClient = containerClient(azureBlobContainer);
        BlobClient blobClient = containerClient.getBlobClient(blobitem);
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        blobClient.downloadStream(os);
        return os;
    }

    public String storeFile(String filename, InputStream content, long length, String azureBlobContainer) {
        BlobClient client = containerClient(azureBlobContainer).getBlobClient(filename);
        if (client.exists()) {
        	logger.warn("The file was already located on azure");
        } else {
            client.upload(content, length);
        }
        logger.info("Azure store file END");
        return "File uploaded with success!";
    }
}
