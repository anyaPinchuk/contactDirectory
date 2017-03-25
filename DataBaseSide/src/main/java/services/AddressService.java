package services;

import dao.AddressDAO;
import entities.Address;
import exceptions.GenericDAOException;
import org.apache.log4j.Logger;

public class AddressService {
    private AddressDAO addressDAO = new AddressDAO();
    private static final Logger LOG = Logger.getLogger(ContactService.class);

    public Address findById(Long contact_id){
        try {
            return addressDAO.findById(contact_id)
                    .orElseThrow(()->new GenericDAOException("address was not found"));
        } catch (GenericDAOException e) {
            LOG.error("error while processng get address by id in AddressService");
            e.printStackTrace();
        }
        return null;
    }

    public AddressDAO getAddressDAO() {
        return addressDAO;
    }

    public void setAddressDAO(AddressDAO addressDAO) {
        this.addressDAO = addressDAO;
    }
}
