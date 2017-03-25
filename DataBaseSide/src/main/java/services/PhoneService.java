package services;

import dao.PhoneDAO;
import entities.PhoneNumber;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

import java.util.List;

public class PhoneService {
    private PhoneDAO phoneDAO = new PhoneDAO();
    private static final Logger LOG = Logger.getLogger(ContactService.class);

    public List<PhoneNumber> findAllById(Long id){
        try {
            return phoneDAO.findAllById(id);
        } catch (GenericDAOException e) {
            LOG.error("error while processing get all phones by id in PhoneService");
            e.printStackTrace();
        }
        return null;
    }

    public void insertPhone(List<PhoneNumber> numbersForInsert, Long contactId) {
        if (numbersForInsert.size() == 0) return;
        numbersForInsert.forEach(obj -> {
            try {
                obj.setContact_id(contactId);
                phoneDAO.insert(obj);
            } catch (GenericDAOException e) {
                LOG.error("error while processing insert phone in InsertEntityService");
                e.printStackTrace();
            }
        });
    }

    public void updatePhones(Long contact_id, List<PhoneNumber> phoneNumbersForUpdate) throws GenericDAOException {
        List<PhoneNumber> numbers = phoneDAO.findAllById(contact_id);
        phoneNumbersForUpdate.forEach(obj -> {
            obj.setContact_id(contact_id);
            try {
                if (numbers.contains(obj)) {
                    phoneDAO.updateById(obj.getId(), obj);
                    numbers.remove(obj);
                }
            } catch (GenericDAOException e) {
                LOG.error("error while processing update phone by id in editContactCommand");
                e.printStackTrace();
            }
        });
        numbers.forEach(obj -> {
            try {
                phoneDAO.deleteById(obj.getId());
            } catch (GenericDAOException e) {
                LOG.error("error while processing update phone by id in editContactCommand");
                e.printStackTrace();
            }
        });
    }
}
