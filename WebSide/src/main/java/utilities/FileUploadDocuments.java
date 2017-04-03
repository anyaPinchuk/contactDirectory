package utilities;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Random;

public class FileUploadDocuments {
    private static final Logger LOG = LoggerFactory.getLogger(FileUploadDocuments.class);

    public static String getFileDirectory(boolean isImage) {
        LOG.info("get File Directory starting, isImage = {}", isImage);
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
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage());
                }
            }
        }
        return isImage ? prop.getProperty("upload.location.photo") : prop.getProperty("upload.location.file");
    }

    public static byte[] readDocument(String fileName, boolean isImage, Long contactId) {
        LOG.info("read document by filename {} starting with contact_id {}", fileName, contactId);
        String uploadPath = getFileDirectory(isImage);
        if (!StringUtils.isNotEmpty(uploadPath)) return null;
        byte[] array = null;
        try {
            if (!StringUtils.isNotEmpty(fileName)) {
                array = Files.readAllBytes(Paths.get(uploadPath + File.separator + "no_avatar.png"));
            } else array = Files.readAllBytes(Paths.get(uploadPath + File.separator + "contact" + contactId +
                    File.separator + fileName));
            return array;
        } catch (Exception e) {
            try {
                if (isImage) array = Files.readAllBytes(Paths.get(uploadPath + File.separator + "no_avatar.png"));
                else array = "<h1>Unable to load content</h1>".getBytes();
            } catch (IOException e1) {
                LOG.error("file no_avatar.png not found");
            }
            LOG.error("{} not found", fileName);
        }
        return array;
    }

    public static boolean renameDocument(String fileName, String newFileName, Long contactId) {
        LOG.info("rename document {} on {} at contact{} starting", fileName, newFileName, contactId);
        if (!StringUtils.isNotEmpty(fileName.trim()) && !StringUtils.isNotEmpty(newFileName.trim())) return false;
        String fileExtension = "";
        String modifiedFileName = "";
        int index = fileName.lastIndexOf('.');
        if (index > 0) {
            fileExtension = fileName.substring(index + 1);
            index = newFileName.lastIndexOf('.');
            if (index > 0)
                modifiedFileName = newFileName.substring(0, index) + "." + fileExtension;
        }
        String uploadPath = getFileDirectory(false);
        if (!StringUtils.isNotEmpty(uploadPath)) return false;
        if (StringUtils.isNotEmpty(modifiedFileName)) {
            File file = new File(uploadPath + File.separator + "contact" + contactId +
                    File.separator + fileName);
            return file.renameTo(new File(file.getParentFile(), modifiedFileName));
        }
        return false;
    }

    public static boolean deleteDocument(String fileName, boolean isImage, Long contact_id) {
        LOG.info("delete document {} starting", fileName);
        String uploadPath = getFileDirectory(isImage);
        if (!StringUtils.isNotEmpty(uploadPath)) return false;
        try {
            Files.delete(Paths.get(uploadPath + File.separator + "contact" + contact_id + File.separator + fileName));
        } catch (IOException e) {
            LOG.error("delete document {} failed", fileName);
            return false;
        }
        return true;
    }

    public static String saveDocument(FileItem item, Long contactId, boolean isImage) {
        LOG.info("save document by contact_id {} starting", contactId);
        if (item == null) return null;
        String uploadPath = getFileDirectory(isImage);
        if (!StringUtils.isNotEmpty(uploadPath)) return null;
        uploadPath += "\\contact" + contactId;
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdir();
        Random random = new Random();
        try {
            String fileName = item.getName();
            File existingFile = new File(uploadPath + File.separator + fileName);
            if (existingFile.exists()){
                String fileExtension = "";
                int index = fileName.lastIndexOf(".");
                if (index > 0) {
                    fileExtension = fileName.substring(index + 1);
                    fileName = fileName +random.nextInt(1000) + "." + fileExtension;
                }
            }
            File storeFile = new File(uploadPath + File.separator + fileName);
            item.write(storeFile);
            return fileName;
        } catch (Exception ex) {
            LOG.error("save file {} failed", item.getName());
            return null;
        }
    }

    public static void deleteDirectory(Long contactId, boolean isImage) {
        LOG.info("delete directory by contact_id {} starting", contactId);
        try {
            String filePath = "";
            if (isImage) filePath = getFileDirectory(true);
            else filePath = getFileDirectory(false);
            if (!StringUtils.isNotEmpty(filePath)) return;
            FileUtils.deleteDirectory(new File(filePath + File.separator + "contact" + contactId));
        } catch (IOException e) {
            LOG.error("delete directory contact{} failed", contactId);
        }
    }
}
