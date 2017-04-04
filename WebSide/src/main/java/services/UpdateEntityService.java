package services;

import entities.Attachment;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utilities.FileUploadDocuments;

import java.util.List;

public class UpdateEntityService {
    private Logger LOG = LoggerFactory.getLogger(UpdateEntityService.class);
    private AttachmentService attachmentService = new AttachmentService();

    public void updateAttachments(List<Attachment> listForUpdate, Long contactId) throws GenericDAOException {
        LOG.info("update attachments by id {} starting", contactId);
        List<Attachment> attachments = attachmentService.findAllById(contactId);
        if (CollectionUtils.isEmpty(attachments)) return;
        listForUpdate.forEach(obj -> {
            String oldName = attachmentService.findById(obj.getId()).getFileName();
            obj.setContactId(contactId);
            if (attachments.contains(obj)) {
                attachments.remove(obj);
                if (!obj.getFileName().equals(oldName)) {
                    FileUploadDocuments.renameDocument(oldName, obj.getFileName(), contactId);
                }
                attachmentService.updateById(obj.getId(), obj);
            }
        });
        attachments.forEach(object -> {
            attachmentService.deleteById(object.getId());
            FileUploadDocuments.deleteDocument(object.getFileName(), false, contactId);
        });
    }


    public void updatePhoto(Long contactId, String fileName) throws GenericDAOException {
        if (fileName == null) return;
        PhotoService photoService = new PhotoService();
        Photo obj = photoService.findById(contactId);
        if (obj == null) {
            photoService.insert(fileName, contactId);
        } else {
            FileUploadDocuments.deleteDocument(obj.getName(), true, contactId);
            obj.setName(fileName);
            photoService.updatePhoto(contactId, obj);
        }
    }

}
