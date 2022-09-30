package com.savvato.tribeapp.services;

import com.savvato.tribeapp.constants.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
public class PictureServiceImpl implements PictureService {

    private static final Log logger = LogFactory.getLog(PictureServiceImpl.class);

    @Autowired
    ResourceTypeService resourceTypeService;

    @Override
    public void writeThumbnailFromOriginal(String resourceType, String filename) throws IOException {
        String dir = resourceTypeService.getDirectoryForResourceType(resourceType);

        boolean done = false;
        int retryCount = 0;
        long delay = 2;

        while (!done && retryCount++ < 3) {
            logger.debug("Sleeping.... " + delay);
            try {
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException ie) {

            }
            logger.debug("Woke up!");

            delay *= 2;

            try {
                logger.debug("Just about to get the file " + dir + "/" + filename);
                InputStream is = new FileSystemResource(dir + "/" + filename).getInputStream();

                BufferedImage originalImage = ImageIO.read(is);

                if (originalImage != null) {

                    logger.debug("YES! Found the file.. doing the resize... " + dir + "/" + filename);
                    BufferedImage resizedImage = resizeImage(originalImage, 250, 250);

                    File out = new File(dir + "/" + filename + "_thumbnail");
                    ImageIO.write(resizedImage, "jpg", out);

                    done = true;
                } else {
                    logger.debug("nooo... the file wasn't there yet."+ dir + "/" + filename);
                }
            } catch (IOException ioe) {
                throw new IOException("Expected the file " + dir + "/" + filename + " to be in place, but we got this exception instead!.", ioe);
            }
        }

        if (!done) {
            throw new IOException("Tried waiting for the file to appear, but there was nothing there to thumbnail-ize.");
        }
    }

    public String transformFilenameUsingSizeInfo(String photoSize, String filename) {
        if (photoSize.equals(Constants.PHOTO_SIZE_THUMBNAIL))
            return filename + "_thumbnail";
        else
            return filename; // original
    }

    BufferedImage resizeImage(BufferedImage originalImage, int targetWidth, int targetHeight) throws IOException {
        Image resultingImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_DEFAULT);
        BufferedImage outputImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }
}
