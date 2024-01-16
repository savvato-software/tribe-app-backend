package com.savvato.tribeapp.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class StorageServiceImplTest {
    @TestConfiguration
    static class StorageServiceTestContextConfiguration {
        @Bean
        public StorageService storageService() {
            return new StorageService();
        }
    }

    @Autowired
    StorageService storageService;

    @MockBean
    ResourceTypeService resourceTypeService;

    @Test
    public void getDefaultFilename() {
        String resourceType = "Profile";
        String resourceId = "1";
        String expectedFilename = resourceType + "Picture_" + resourceId + ".jpg";
        String actualFilename = storageService.getDefaultFilename(resourceType, resourceId);
        assertEquals(expectedFilename, actualFilename);
    }

    @Test
    public void storeHappyPath() {
        String resourceType = "Profile";
        byte[] content = new byte[1];
        MultipartFile file = new MockMultipartFile("test file", content);
        String dir = "/";
        String filename = "";
        when(resourceTypeService.getDirectoryForResourceType(anyString())).thenReturn(dir);
        storageService.store(resourceType, file, filename);
    }

    @Test
    public void isFileExistingHappyPath() throws IOException {
        String resourceType = "testResourceType";
        String filename = "testFile.txt";
        String directoryPath = System.getProperty("java.io.tmpdir") + File.separator + "testFiles";

        // Create the directory if it doesn't exist
        File directory = new File(directoryPath);
        directory.mkdirs();

        // Create a temporary file in the specified directory
        File tempFile = new File(directory, filename);
        tempFile.createNewFile();

        when(resourceTypeService.getDirectoryForResourceType(anyString())).thenReturn(directoryPath);

        try {
            long result = storageService.isFileExisting(resourceType, filename);
            assertTrue(result > 0);
        } finally {
            // Clean up: Delete the temporary file
            tempFile.delete();

            // Clean up: Delete the temporary directory
            directory.delete();
        }
    }
}
