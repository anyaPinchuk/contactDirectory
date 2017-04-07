package services;

import converters.AddressConverter;
import converters.ContactConverter;
import dto.ContactDTO;
import entities.Address;
import entities.Contact;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchService {
    /**
     * finds contacts with given condition
     *
     * @param contactDTO That is contact suitable for condition
     * @param fromDate   That is initial date
     * @param toDate     That is finite date
     * @return list of found contacts
     */
    public List<ContactDTO> searchContacts(ContactDTO contactDTO, DateTime fromDate, DateTime toDate) {
        ContactService contactService = new ContactService();
        AddressService addressService = new AddressService();
        List<Contact> contacts;
        ContactConverter contactConverter = new ContactConverter();
        AddressConverter addressConverter = new AddressConverter();
        Contact contact = contactConverter.toEntitySearch(contactDTO).get();
        Address address = addressConverter.toEntitySearch(contactDTO.getAddress()).get();
        if (StringUtils.isNotEmpty(address.getCountry()) && StringUtils.isNotEmpty(address.getCity()) &&
                StringUtils.isNotEmpty(address.getStreetAddress()) && StringUtils.isNotEmpty(address.getIndex())) {
            contacts = contactService.findByCriteria(contact, null, fromDate, toDate);
        } else contacts = contactService.findByCriteria(contact, address, fromDate, toDate);
        List<ContactDTO> contactDTOList = contacts.stream().map(obj -> {
            ContactDTO dto = contactConverter.toDTO(Optional.of(obj)).get();
            Address addressObj = addressService.findById(obj.getId());
            dto.setAddress(addressConverter.toDTO(Optional.of(addressObj)).get());
            return dto;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(contactDTOList)) return new ArrayList<>();
        else return contactDTOList;
    }

}
