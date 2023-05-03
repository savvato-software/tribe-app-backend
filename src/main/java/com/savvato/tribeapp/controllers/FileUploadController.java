package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.constants.ResourceTypeConstants;
import com.savvato.tribeapp.services.PictureService;
import com.savvato.tribeapp.services.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@Slf4j
public class FileUploadController {

	private final StorageService storageService;
	private final PictureService pictureService;

	@Autowired
	public FileUploadController(StorageService storageService, PictureService pictureService) {
		this.storageService = storageService;
		this.pictureService = pictureService;
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}/isFound" }, method = RequestMethod.GET)
	public long fileIsFound(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId) {
		long timestamp = 0;

		if (isValidResourceType(resourceType)) {
			String filename = storageService.getDefaultFilename(resourceType, resourceId);
			timestamp = storageService.isFileExisting(resourceType, filename);
		}

		return timestamp;
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}" }, method = RequestMethod.DELETE)
	public boolean deleteFile(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId) {
		boolean b = false;

		if (isValidResourceType(resourceType)) {
			String filename = storageService.getDefaultFilename(resourceType, resourceId);
			b = storageService.delete(resourceType, filename);
		}

		return b;
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}" }, method = RequestMethod.GET)
	public ResponseEntity<byte[]> serveFile(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId, @RequestParam("photoSize") String photoSize) {
		String filename = storageService.getDefaultFilename(resourceType, resourceId);
		filename = pictureService.transformFilenameUsingSizeInfo(photoSize, filename);

		log.debug("^^^^^ About to call storageservice to load --> " + filename);
		byte[] fileAsByteArray = storageService.loadAsByteArray(resourceType, filename);

		log.debug("^^^^^^ Back from storageservice call to load --> " + filename);
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileAsByteArray);
	}

	@RequestMapping(value = { "/api/resource/{resourceType}/{resourceId}" }, method = RequestMethod.POST)
	public String handleFileUpload(HttpServletRequest request, @PathVariable String resourceType,
			@PathVariable String resourceId, @RequestParam("file") MultipartFile file) {

		if (isValidResourceType(resourceType)) {
			String filename = storageService.getDefaultFilename(resourceType, resourceId);
			log.debug("^^^^ About to call storage service to save --> " + filename);
			storageService.store(resourceType, file, filename);

			try {
				log.debug("^^^^ About to call pictureservice to write thumbnail --> " + filename);
				pictureService.writeThumbnailFromOriginal(resourceType, filename);
			} catch (IOException ioe){
				return "{\"msg\":\"error\"}";
			}

			return "{\"msg\":\"ok\"}";
		}

		log.debug("^^^^^ COULD NOT do the HandleFileUpload!");
		return null;
	}

	private boolean isValidResourceType(String resourceType) {
		return resourceType.equals(ResourceTypeConstants.RESOURCE_TYPE_PROFILE_IMAGE);
	}
}
