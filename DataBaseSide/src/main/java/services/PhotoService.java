package services;

import dao.AddressDAO;
import dao.PhotoDAO;
import entities.Address;
import entities.Contact;
import entities.Photo;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

public class PhotoService {
    private PhotoDAO photoDAO = new PhotoDAO();
    private static final Logger LOG = Logger.getLogger(ContactService.class);

    public Photo findById(Long id) {
        try {
            return photoDAO.findById(id).get();
        } catch (GenericDAOException e) {
            LOG.error("error while processing get Photo by id in PhotoService");
            e.printStackTrace();
        }
        return null;
    }

    public void updatePhoto(Long id, Photo photo) {
        try {
            photoDAO.updateById(id, photo);
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
    }

    public Long insert(String fileName) {
        try {
            return photoDAO.insert(new Photo(fileName));
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteById(Long address_id) {
        try {
            photoDAO.deleteById(address_id);
        } catch (GenericDAOException e) {
            e.printStackTrace();
        }
    }
}
