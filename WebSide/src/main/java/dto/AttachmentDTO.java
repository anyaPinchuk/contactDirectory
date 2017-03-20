package dto;


import java.sql.Blob;
import java.util.Date;

public class AttachmentDTO extends DTO {
    private Long id;
    private Date dateOfDownload;
    private String fileName;
    private String comment;

    public AttachmentDTO() {
    }

    public AttachmentDTO(Long id, Date dateOfDownload, String fileName, String comment) {
        this.id = id;
        this.dateOfDownload = dateOfDownload;
        this.fileName = fileName;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
