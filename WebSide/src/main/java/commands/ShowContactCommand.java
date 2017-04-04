package commands;

import converters.AddressConverter;
import converters.ContactConverter;
import converters.PhoneConverter;
import dto.ContactDTO;
import dto.PhoneDTO;
import dto.PhotoDTO;
import entities.*;
import exceptions.GenericDAOException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import services.*;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ShowContactCommand extends FrontCommand {
    private ContactConverter contactConverter = new ContactConverter();
    private AddressConverter addressConverter = new AddressConverter();
    private PhoneConverter phoneConverter = new PhoneConverter();
    private ContactService contactService = new ContactService();
    private AddressService addressService = new AddressService();
    private AttachmentService attachmentService = new AttachmentService();
    private PhoneService phoneService = new PhoneService();
    private PhotoService photoService = new PhotoService();

    @Override
    public void processGet() throws ServletException, IOException {
        ContactDTO contactDTO = null;
        String paramId = request.getParameter("id");
        LOG.info("show contact starting with parameter id {}", paramId);
        if (StringUtils.isEmpty(paramId.trim())) forward("unknown");
        try {
            Long id = Long.valueOf(paramId);
            Contact contact = contactService.findById(id);
            if (contact != null) {
                contactDTO = contactConverter.toDTO(Optional.of(contact)).orElseThrow(() ->
                        new GenericDAOException("contact wasn't converted"));
                Address address = addressService.findById(id);
                contactDTO.setAddress(addressConverter.toDTO(Optional.of(address)).orElseThrow(()
                        -> new GenericDAOException("address wasn't converted")));
                List<PhoneNumber> numberList = phoneService.findAllById(contactDTO.getId());
                List<PhoneDTO> phoneDTOList = CollectionUtils.isNotEmpty(numberList) ? numberList.stream().map(number ->
                        phoneConverter.toDTO(Optional.of(number)).get()).collect(Collectors.toList()) : new ArrayList<>();
                contactDTO.setPhoneDTOList(phoneDTOList);
                Photo photo = photoService.findById(id);
                if (photo != null) {
                    PhotoDTO photoDTO = new PhotoDTO(photo.getId(), photo.getName());
                    contactDTO.setPhoto(photoDTO);
                }
                List<Attachment> attachments = attachmentService.findAllById(contactDTO.getId());
                request.setAttribute("attachments", attachments);
            }
        } catch (NumberFormatException | GenericDAOException e) {
            forward("unknown");
            LOG.error("error while processing show contact from ShowContactCommand");
        }
        request.setAttribute("contact", contactDTO);
        forward("showContact");
    }

    @Override
    public void processPost() throws ServletException, IOException {

    }
}
