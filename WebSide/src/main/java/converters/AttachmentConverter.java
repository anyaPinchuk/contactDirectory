package converters;

import dto.AttachmentDTO;
import dto.ContactDTO;
import entities.Attachment;
import entities.PhoneNumber;
import org.joda.time.DateTime;

import java.util.Optional;

public class AttachmentConverter extends Converter<AttachmentDTO, Attachment>{
    @Override
    public Optional<Attachment> toEntity(AttachmentDTO dto) {
        return null;
    }

    @Override
    public Optional<AttachmentDTO> toDTO(Optional<? extends Attachment> entity) {
        if (entity != null && entity.isPresent())
            return Optional.of(new AttachmentDTO(entity.get().getId(),
                    entity.get().getDateOfDownload() == null ? null : new DateTime(entity.get().getDateOfDownload()), entity.get().getFileName(),
                    entity.get().getComment()));
        return Optional.empty();
    }
}
