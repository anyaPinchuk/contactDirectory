package dto;

import org.joda.time.DateTime;

public class AttachmentDTO extends DTO {
    private Long id;
    private DateTime dateOfDownload;
    private String fileName;
    private String comment;

    public AttachmentDTO() {
    }

    public AttachmentDTO(Long id, DateTime dateOfDownload, String fileName, String comment) {
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

    public DateTime getDateOfDownload() {
        return dateOfDownload;
    }

    public void setDateOfDownload(DateTime dateOfDownload) {
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
