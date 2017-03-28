package utilities;

import commands.ImageCommand;
import dto.PhotoDTO;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class FileUploadDocuments {
    private static final Logger LOG = Logger.getLogger(FileUploadDocuments.class);

    public static String getFileDirectory(boolean isImage) {
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
            LOG.error(ex.getMessage());
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return isImage ? prop.getProperty("upload.location.photo") : prop.getProperty("upload.location.file");
    }

    public static byte[] readDocument(String fileName, boolean isImage, Long contact_id) {
        String uploadPath = getFileDirectory(isImage);
        byte[] array = null;
        try {
            if (!"".equals(fileName)) {
                array = Files.readAllBytes(Paths.get(uploadPath + File.separator + "no_avatar.png"));
            } else array = Files.readAllBytes(Paths.get(uploadPath + File.separator + "contact" + contact_id +
                    File.separator + fileName));
            return array;
        } catch (Exception e) {
            try {
                if (isImage) array = Files.readAllBytes(Paths.get(uploadPath + File.separator + "no_avatar.png"));
            } catch (IOException e1) {
                LOG.info(e.getMessage());
            }
            LOG.info(e.getMessage());
        }
        return array;
    }

    public static boolean renameDocument(String fileName, String newFileName, Long contact_id) {
        String uploadPath = getFileDirectory(false);
        File file = new File(uploadPath + File.separator + "contact" + contact_id +
                File.separator + fileName);
        return file.renameTo(new File(file.getParentFile(), newFileName));
    }

    public static boolean deleteDocument(String fileName, boolean isImage, Long contact_id) {
        String uploadPath = getFileDirectory(isImage);
        try {
            Files.delete(Paths.get(uploadPath + File.separator + "contact" + contact_id + File.separator + fileName));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean saveDocument(HttpServletRequest request, FileItem item, Long contact_id, boolean isImage) {
        if (!ServletFileUpload.isMultipartContent(request)) {
            LOG.error("Error: Form must has enctype=multipart/form-data.");
            return false;
        }
        String uploadPath = getFileDirectory(isImage);
        File uploadDir;
        if (uploadPath != null) {
            uploadPath += "\\contact" + contact_id;
            uploadDir = new File(uploadPath);
        } else {
            LOG.error("Directory property is null");
            return false;
        }
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        String filePath = null;
        try {
            String fileName = new File(item.getName()).getName();
            filePath = uploadPath + File.separator + fileName;
            File storeFile = new File(filePath);
            item.write(storeFile);

        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return false;
        }
        return true;
    }

    public static void deleteDirectory(Long contact_id, boolean isImage) {
        try {
            String filePath = "";
            if (isImage) filePath = getFileDirectory(true);
            else filePath = getFileDirectory(false);
            FileUtils.deleteDirectory(new File(filePath + File.separator + "contact" + contact_id));
        } catch (IOException e) {
            LOG.error(e.getMessage());
        }
    }
}
