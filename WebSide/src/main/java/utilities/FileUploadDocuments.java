package utilities;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Properties;

public class FileUploadDocuments {
    private static final Logger LOG = Logger.getLogger(FileUploadDocuments.class);

    public static String getFileDirectory() {
        Properties prop = new Properties();
        InputStream input = null;
        try {
            String filename = "documents.properties";
            input = FileUploadDocuments.class.getClassLoader().getResourceAsStream(filename);
            if (input == null) {
                LOG.info("Sorry, unable to find " + filename);
                return null;
            }
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return prop.getProperty("upload.location.photo");
    }

    public static void readFile() {

    }

    public static String saveFile(HttpServletRequest request, FileItem item) {

        if (!ServletFileUpload.isMultipartContent(request)) {
            LOG.error("Error: Form must has enctype=multipart/form-data.");
            return null;
        }

        // configures upload settings

        // constructs the directory path to store upload file
        // this path is relative to application's directory
        String uploadPath = getFileDirectory();
        File uploadDir;
        // creates the directory if it does not exist
        if (uploadPath != null) {
            uploadDir = new File(uploadPath);
        } else {
            LOG.error("Directory property is null");
            return null;
        }
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = null;
        try {
            // parses the request's content to extract file data

            String fileName = new File(item.getName()).getName();
            filePath = uploadPath + File.separator + fileName;
            File storeFile = new File(filePath);
            // saves the file on disk
            item.write(storeFile);

        } catch (
                Exception ex)

        {
            LOG.error(ex.getMessage());
        }
        return filePath;
    }
}
