package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.ResourceTypeConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ResourceTypeServiceImpl implements ResourceTypeService {

    @Value("${app.uploaded.user.resources.directory.root}")
    private String resourcesDirRoot;

    public String getDirectoryForResourceType(String resourceType) {
        String rtn = null;

        if (resourceType.equals(ResourceTypeConstants.RESOURCE_TYPE_PROFILE_IMAGE)) {
            rtn = resourcesDirRoot + "/profile";
        } else {
            throw new IllegalArgumentException("ResourceTypeService was passed an invalid resource type.");
        }

        return rtn;
    }

}
