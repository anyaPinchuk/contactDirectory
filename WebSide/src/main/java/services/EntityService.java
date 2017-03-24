package services;

import dao.*;
import org.apache.log4j.Logger;

public abstract class EntityService {
    Logger LOG = Logger.getLogger(UpdateEntityService.class);
    AttachmentDAO attachmentDAO = new AttachmentDAO();
    PhoneDAO phoneDAO = new PhoneDAO();
    PhotoDAO photoDAO = new PhotoDAO();
    ContactDAO contactDAO = new ContactDAO();
    AddressDAO addressDAO = new AddressDAO();

}
