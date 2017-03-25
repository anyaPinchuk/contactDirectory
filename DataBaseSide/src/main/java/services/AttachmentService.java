package services;

import dao.AttachmentDAO;
import entities.Attachment;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.util.List;

public class AttachmentService {
    private AttachmentDAO attachmentDAO = new AttachmentDAO();
    private static final Logger LOG = Logger.getLogger(ContactService.class);

    public List<Attachment> findAllById(Long id) {
        try {
            return attachmentDAO.findAllById(id);
        } catch (GenericDAOException e) {
            LOG.error("error while processng get all phones by id in PhoneService");
            e.printStackTrace();
        }
        return null;
    }

    public void insertAttachments(List<Attachment> attachmentsForInsert, Long contactId) {
        if (attachmentsForInsert.size() == 0) return;
        attachmentsForInsert.forEach(obj -> {
            try {
                obj.setContact_id(contactId);
                attachmentDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert attachment in InsertEntityService");
                e.printStackTrace();
            }
        });
    }

    public void deleteById(Long id) {
        try {
            attachmentDAO.deleteById(id);
        } catch (GenericDAOException e) {
            LOG.error("error while processing delete attachment by id in editContactCommand");
            e.printStackTrace();
        }
    }

    public Attachment findById(Long id) {
        try {
            return attachmentDAO.findById(id).get();
        } catch (GenericDAOException e) {
            LOG.error("error while processing get Photo by id in PhotoService");
            e.printStackTrace();
        }
        return null;
    }

    public void updateById(Long id, Attachment attachment) {
        try {
            attachmentDAO.updateById(id, attachment);
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
    }
}
