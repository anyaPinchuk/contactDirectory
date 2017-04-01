package services;

import entities.Attachment;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;
import utilities.FileUploadDocuments;

import java.util.List;

public class UpdateEntityService {
    private Logger LOG = Logger.getLogger(UpdateEntityService.class);
    private AttachmentService attachmentService = new AttachmentService();

    public void updateAttachments(List<Attachment> listForUpdate, Long contact_id) throws GenericDAOException {
        List<Attachment> attachments = attachmentService.findAllById(contact_id);
        if (CollectionUtils.isEmpty(attachments)) return;
        listForUpdate.forEach(obj -> {
            String oldName = attachmentService.findById(obj.getId()).getFileName();
            obj.setContactId(contact_id);
            if (attachments.contains(obj)) {
                attachments.remove(obj);
                if (!oldName.equals(obj.getFileName())) {
                    FileUploadDocuments.renameDocument(oldName, obj.getFileName(), contact_id);
                }
                attachmentService.updateById(obj.getId(), obj);
            }
        });
        attachments.forEach(object -> {
            attachmentService.deleteById(object.getId());
            FileUploadDocuments.deleteDocument(object.getFileName(), false, contact_id);
        });
    }


    public void updatePhoto(Long contact_id, String fileName) throws GenericDAOException {
        if (fileName == null) return;
        PhotoService photoService = new PhotoService();
        Photo obj = photoService.findById(contact_id);
        if (obj == null) {
            photoService.insert(fileName, contact_id);
        } else {
            FileUploadDocuments.deleteDocument(obj.getName(), true, contact_id);
            obj.setName(fileName);
            photoService.updatePhoto(contact_id, obj);
        }
    }

}
