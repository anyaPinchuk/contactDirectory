package converters;

import dto.AttachmentDTO;
import dto.ContactDTO;
import entities.Attachment;
import entities.PhoneNumber;

import java.util.Optional;

public class AttachmentConverter extends Converter<AttachmentDTO, Attachment>{
    @Override
    public Optional<Attachment> toEntity(AttachmentDTO dto) {
        if(dto == null) return Optional.empty();
        return Optional.of(new Attachment(dto.getId(), dto.getDateOfDownload() , dto.getFileName(), dto.getFile(),
                dto.getComment(), dto.getContactDTO().getId()));
    }

    @Override
    public Optional<AttachmentDTO> toDTO(Optional<? extends Attachment> entity) {
        if (entity != null && entity.isPresent())
            return Optional.of(new AttachmentDTO(entity.get().getId(), entity.get().getDateOfDownload(), entity.get().getFileName(),
                    entity.get().getFile(), entity.get().getComment(), null));
        return Optional.empty();
    }
}
