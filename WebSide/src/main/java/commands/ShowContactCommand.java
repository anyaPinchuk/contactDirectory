package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import converters.PhoneConverter;
import dao.PhotoDAO;
import dto.ContactDTO;
import dto.PhoneDTO;
import dto.PhotoDTO;
import entities.*;
import exceptions.GenericDAOException;
import exceptions.MessageError;
import services.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShowContactCommand extends FrontCommand {
    private ContactConverter contactConverter;
    private AddressConverter addressConverter;
    private PhoneConverter phoneConverter;
    private ContactService contactService = new ContactService();
    private AddressService addressService = new AddressService();
    private AttachmentService attachmentService = new AttachmentService();
    private PhoneService phoneService = new PhoneService();
    private PhotoService photoService = new PhotoService();

    public ShowContactCommand() {
        contactConverter = new ContactConverter();
        addressConverter = new AddressConverter();
        phoneConverter = new PhoneConverter();
    }

    @Override
    public void processGet() throws ServletException, IOException {
        LOG.info("get contact by id starting ");
        ContactDTO contactDTO = null;
        Long id = Long.valueOf(request.getParameter("id"));
        Long address_id;
        try {
            Contact contact = contactService.findById(id);
            if (contact != null) {
                contactDTO = contactConverter.toDTO(Optional.of(contact)).orElseThrow(() ->
                        new GenericDAOException("contact wasn't converted"));
                Address address = addressService.findById(id);
                contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).orElseThrow(()
                        -> new GenericDAOException("address wasn't converted")));
                List<PhoneNumber> numberList = phoneService.findAllById(contactDTO.getId());
                List<PhoneDTO> phoneDTOList = numberList.size() != 0 ? numberList.stream().map(number ->
                        phoneConverter.toDTO(Optional.of(number)).get()).collect(Collectors.toList()) : null;
                contactDTO.setPhoneDTOList(phoneDTOList);
                Photo photo = photoService.findById(id);
                if (photo != null) {
                    PhotoDTO photoDTO = new PhotoDTO(photo.getId(), photo.getName());
                    contactDTO.setPhoto(photoDTO);
                }
                List<Attachment> attachments = attachmentService.findAllById(contactDTO.getId());
                request.setAttribute("attachments", attachments);
            }
        } catch (GenericDAOException e) {
            LOG.error("error while processing find contact from ShowContactCommand");
            new MessageError(e.getMessage(), e);
        }
        request.setAttribute("contact", contactDTO);
        forward("showContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
