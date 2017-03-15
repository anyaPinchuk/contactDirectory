package dto;


import java.sql.Blob;
import java.util.Date;

public class AttachmentDTO extends DTO {
    private Long id;
    private Date dateOfDownload;
    private String fileName;
    private Blob file;
    private String comment;
    private ContactDTO contactDTO;

    public AttachmentDTO() {
    }

    public AttachmentDTO(Long id, Date dateOfDownload, String fileName, Blob file, String comment, ContactDTO contactDTO) {
        this.id = id;
        this.dateOfDownload = dateOfDownload;
        this.fileName = fileName;
        this.file = file;
        this.comment = comment;
        this.contactDTO = contactDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfDownload() {
        return dateOfDownload;
    }

    public void setDateOfDownload(Date dateOfDownload) {
        this.dateOfDownload = dateOfDownload;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }

    public String getComment() {
        return comment;
    }

    public ContactDTO getContactDTO() {
        return contactDTO;
    }

    public void setContactDTO(ContactDTO contactDTO) {
        this.contactDTO = contactDTO;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
