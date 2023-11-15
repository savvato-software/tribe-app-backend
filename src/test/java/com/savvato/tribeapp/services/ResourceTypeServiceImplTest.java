package com.savvato.tribeapp.services;


import com.savvato.tribeapp.constants.ResourceTypeConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
public class ResourceTypeServiceImplTest {
    @TestConfiguration
    static class ResourceTypeServiceTestContextConfiguration {
        @Bean
        public ResourceTypeService resourceTypeService() {
            return new ResourceTypeServiceImpl();
        }
    }

    @Autowired
    ResourceTypeService resourceTypeService;
    @Value("${app.uploaded.user.resources.directory.root}")
    private String resourcesDirRoot;

    @Test
    public void getDirectoryForResourceTypeWhenResourceTypeValid() {
        String resourceType = ResourceTypeConstants.RESOURCE_TYPE_PROFILE_IMAGE;
        String expectedResult = resourcesDirRoot + "/profile";

        String actualResult = resourceTypeService.getDirectoryForResourceType(resourceType);
        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void getDirectoryForResourceTypeWhenResourceTypeInvalid() {
        String resourceType = "invalid resource type";
        String errorMessage = "ResourceTypeService was passed an invalid resource type.";
        assertThrows(IllegalArgumentException.class, () -> {
            resourceTypeService.getDirectoryForResourceType(resourceType);
        }, errorMessage);
    }
}
