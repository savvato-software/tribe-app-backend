package com.savvato.tribeapp.controllers;

import com.savvato.tribeapp.constants.ResourceTypeConstants;
import com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController.DeleteFile;
import com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController.FileIsFound;
import com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController.HandleFileUpload;
import com.savvato.tribeapp.controllers.annotations.controllers.FileUploadController.ServeFile;
import com.savvato.tribeapp.services.PictureService;
import com.savvato.tribeapp.services.StorageService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/resource/{resourceType}/{resourceId}")
@Tag(name = "resource", description = "Files that are uploaded by the user")
@Slf4j
public class FileUploadController {

  private final StorageService storageService;
  private final PictureService pictureService;

  @Autowired
  public FileUploadController(StorageService storageService, PictureService pictureService) {
    this.storageService = storageService;
    this.pictureService = pictureService;
  }

  @FileIsFound
  @GetMapping("/isFound")
  public long fileIsFound(
      HttpServletRequest request,
      @Parameter(description = "The resource type", example = "photo") @PathVariable
          String resourceType,
      @Parameter(description = "The resource ID", example = "1") @PathVariable String resourceId) {
    long timestamp = 0;

    if (isValidResourceType(resourceType)) {
      String filename = storageService.getDefaultFilename(resourceType, resourceId);
      timestamp = storageService.isFileExisting(resourceType, filename);
    }

    return timestamp;
  }

  @DeleteFile
  @DeleteMapping
  public boolean deleteFile(
      HttpServletRequest request,
      @Parameter(description = "The resource type", example = "photo") @PathVariable
          String resourceType,
      @Parameter(description = "The resource ID", example = "1") @PathVariable String resourceId) {
    boolean b = false;

    if (isValidResourceType(resourceType)) {
      String filename = storageService.getDefaultFilename(resourceType, resourceId);
      b = storageService.delete(resourceType, filename);
    }

    return b;
  }

  @ServeFile
  @GetMapping
  public ResponseEntity<byte[]> serveFile(
      HttpServletRequest request,
      @Parameter(description = "The resource type", example = "photo") @PathVariable
          String resourceType,
      @Parameter(description = "The resource ID", example = "1") @PathVariable String resourceId,
      @Parameter(description = "The photo size", example = "120x120") @RequestParam("photoSize")
          String photoSize) {
    String filename = storageService.getDefaultFilename(resourceType, resourceId);
    filename = pictureService.transformFilenameUsingSizeInfo(photoSize, filename);

    log.debug("^^^^^ About to call storageservice to load --> " + filename);
    byte[] fileAsByteArray = storageService.loadAsByteArray(resourceType, filename);

    log.debug("^^^^^^ Back from storageservice call to load --> " + filename);
    return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(fileAsByteArray);
  }

  @HandleFileUpload
  @PostMapping
  public String handleFileUpload(
      HttpServletRequest request,
      @Parameter(description = "The resource type", example = "photo") @PathVariable
          String resourceType,
      @Parameter(description = "The resource ID", example = "1") @PathVariable String resourceId,
      @Parameter(description = "The resource file") @RequestParam("file") MultipartFile file) {

    if (isValidResourceType(resourceType)) {
      String filename = storageService.getDefaultFilename(resourceType, resourceId);
      log.debug("^^^^ About to call storage service to save --> " + filename);
      storageService.store(resourceType, file, filename);

      try {
        log.debug("^^^^ About to call pictureservice to write thumbnail --> " + filename);
        pictureService.writeThumbnailFromOriginal(resourceType, filename);
      } catch (IOException ioe) {
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
