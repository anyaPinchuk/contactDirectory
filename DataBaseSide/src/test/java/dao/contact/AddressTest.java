package dao.contact;

import dao.AddressDAO;
import dao.ContactDAO;
import entities.Address;
import entities.Contact;
import exceptions.GenericDAOException;
import exceptions.MessageError;
import org.junit.Test;

public class AddressTest {
    @Test
    public void add() {
        AddressDAO addressDAO = new AddressDAO();
        Address address = new Address(null, "Belarus", "Minsk", null, null);
        try {
            long id = addressDAO.insert(address);
            System.out.println(id);
        } catch (GenericDAOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void update() {
        AddressDAO addressDAO = new AddressDAO();
        Address address;
        try {
            address = addressDAO.findById(2L).get();
            address.setIndex("247500");
            addressDAO.updateById(2L, address);
        } catch (GenericDAOException e) {
            new MessageError(e.getMessage(), e);
        }
    }

    @Test
    public void delete() {
        AddressDAO addressDAO = new AddressDAO();
        try {
            addressDAO.deleteById(2L);
        } catch (GenericDAOException e) {
            new MessageError(e.getMessage(), e);
        }
    }
}
