package com.savvato.tribeapp.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@Service
public class StorageService {

	private static final Log logger = LogFactory.getLog(StorageService.class);
	
	@Autowired
	private ResourceTypeService resourceTypeService;
    
//    public static final String[] arr = {"static/images/color-block-1.jpg", "static/images/color-block-2.jpg", "static/images/color-block-3.jpg",
//    		"static/images/color-block-4.jpg", "static/images/color-block-5.jpg", "static/images/color-block-6.jpg", "static/images/color-block-7.jpg"};

    public String getDefaultFilename(String resourceType, String resourceId) {
    	return resourceType + "Picture_" + resourceId + ".jpg";
    }

	public void store(String resourceType, MultipartFile file, String filename) {
    	String dir = resourceTypeService.getDirectoryForResourceType(resourceType);

		logger.debug("******* ****  **** about to do StorageService::store() " + dir + "/" + filename);

    	try {
    		File fdir = new File(dir);
    		fdir.mkdirs();
    	} catch (Exception e) {
    		throw e;
    	}

    	try {
			file.transferTo(new File(dir + "/" + filename));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	logger.debug("*********     *** successfully exiting the store method .. " + dir + "/" + filename);
    }

//    public Resource loadAsResource(String resourceType, String filename) {
//		String dir = resourceTypeService.getDirectoryForResourceType(resourceType);
//    	logger.debug("*******         Returning a file resource at: " + dir + "/" + filename);
//
//    	 TODO: The ideal thing to do here would be to have a bean for each resourceType, and
//    	  that bean would return a FileSystemResource.
//
//    	Resource rtn = new FileSystemResource(dir + "/" + filename);
//
//    	if (isFileExisting(resourceType, filename) == 0) {
//
//    		if (resourceType.equals("profile") || resourceType.equals("topic")) {
//	    		int _hash = getSumOfEachDigitAddedUp(filename.hashCode());
//	    		rtn = new ClassPathResource(arr[_hash]);
//    		}
//    	}
//
//    	return rtn;
//    }
    
    /**
     * If the given file exists, it returns its timestamp, otherwise 0 (false).
     *
     * @param filename
     * @return its timestamp, otherwise 0 (false).
     */
    public long isFileExisting(String resourceType, String filename) {
		long rtn = 0;
		String dir = resourceTypeService.getDirectoryForResourceType(resourceType);

    	try {
    		File f = new File(dir + "/" + filename);

			if (f.exists())
				rtn = f.lastModified();

    	} catch (Exception e) {
    		throw e;
		}
    	
    	logger.debug("***************}}  checking isFound: " + dir + "/" + filename + " --> " + rtn + " " + (rtn != 0));
    	
    	return rtn;
    }
    
    public boolean delete(String resourceType, String filename) {
    	boolean rtn = false;
		String dir = resourceTypeService.getDirectoryForResourceType(resourceType);

		logger.debug("***************}}  Deleting: " + dir + "/" + filename);

		try {
    		File f = new File(dir + "/" + filename);
    		rtn = f.delete();
    	} catch (Exception e) {
    		throw e;
    	}

    	return rtn;
    }
    

    public byte[] loadAsByteArray(String resourceType, String filename) {
		String dir = resourceTypeService.getDirectoryForResourceType(resourceType);
    	logger.debug("*******         Returning byte array of file at: " + dir + "/" + filename);

    	byte[] rtn = null;
    
	    try {
	    	// try to load the requested image
	    	rtn = readFile(new FileSystemResource(dir + "/" + filename));
	    } catch (FileNotFoundException fnfe) {
//	    	try {
//	    		 if it can't be found, send back our default image
//	    		int _hash = getSumOfEachDigitAddedUp(filename.hashCode());
//	    		rtn = readFile(new ClassPathResource(arr[_hash]));
//	    	} catch (IOException ioe) {
//	    		logger.error(ioe);
//	    	}
			logger.error("Could not find file: " + dir + "/" + filename);
	    } catch (IOException ioe) {
	    	// something other than a missing requested file happened.. :(
	    	logger.error("Something bad happened with the file " + dir + "/" + filename, ioe);
	    }
	    
	    return rtn;
    }
    
//    private int getSumOfEachDigitAddedUp(int ii) {
//    	String str = ii +"";
//
//    	int total = 0;
//    	char[] charArray = str.toCharArray();
//
//    	for (int i = 0; i < charArray.length; i++) {
//    		try {
//    			total += Integer.parseInt(charArray[i]+"");
//    		} catch (NumberFormatException nfe) {
    			// do nothing
//    		}
//    	}
//
//    	return total % 7;
//    }
    
	private byte[] readFile(Resource resource) throws IOException {
		byte[] rtn;

		InputStream stream = resource.getInputStream();
		long size = resource.getFile().length();
		
		rtn = convertStreamToByteArray(stream, size);
		return rtn;
	}

    public byte[] convertStreamToByteArray(InputStream stream, long size) throws IOException {

        // check to ensure that file size is not larger than Integer.MAX_VALUE.
        if (size > Integer.MAX_VALUE) {
            return new byte[0];
        }

        byte[] buffer = new byte[(int)size];
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        int line = 0;
        // read bytes from stream, and store them in buffer
        while ((line = stream.read(buffer)) != -1) {
            // Writes bytes from byte array (buffer) into output stream.
            os.write(buffer, 0, line);
        }
        stream.close();
        os.flush();
        os.close();
        return os.toByteArray();
    }    
}
