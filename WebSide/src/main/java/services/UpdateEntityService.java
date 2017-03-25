package services;

import entities.Attachment;
import entities.Contact;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;
import utilities.FileUploadDocuments;

import java.util.List;

public class UpdateEntityService  {
    private Logger LOG = Logger.getLogger(UpdateEntityService.class);
    private AttachmentService attachmentService = new AttachmentService();

    public void updateAttachments(List<Attachment> listForUpdate, Long contact_id) throws GenericDAOException {
        List<Attachment> attachments = attachmentService.findAllById(contact_id);
        listForUpdate.forEach(obj -> {
            obj.setContact_id(contact_id);
            if (attachments.contains(obj)) {
                if (FileUploadDocuments.renameDocument(attachmentService.findById(obj.getId()).getFileName(),
                        obj.getFileName(), contact_id)) {
                    attachmentService.updateById(obj.getId(), obj);
                    attachments.remove(obj);
                } else {
                    LOG.info("the attachment file was not renamed");
                }
            }
        });
        attachments.forEach(object -> {
            attachmentService.deleteById(object.getId());
            FileUploadDocuments.deleteDocument(object.getFileName(), false, contact_id);
        });
    }


    public Long updatePhoto(Contact contact, String fileName) throws GenericDAOException {
        if (fileName == null) return null;
        PhotoService photoService = new PhotoService();
        ContactService contactService = new ContactService();
        Contact contact1 = contactService.findById(contact.getId());
        if (contact1.getPhoto_id() != 0) {
            Photo obj = photoService.findById(contact1.getPhoto_id());
            FileUploadDocuments.deleteDocument(obj.getName(), true, null);
            obj.setName(fileName);
            photoService.updatePhoto(obj.getId(), obj);
        } else {
            photoService.insert(fileName);
        }
        return null;
    }


}
