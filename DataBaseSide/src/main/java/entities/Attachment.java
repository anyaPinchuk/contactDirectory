package entities;

import java.sql.Blob;
import java.util.Date;

public class Attachment {
    private Long id;
    private Date dateOfDownload;
    private String fileName;
    private Blob file;
    //change blob file to path to file
    private String comment;
    private Long contact_id;

    public Attachment(Long id, Date dateOfDownload, String fileName, String comment, Long contact_id) {
        this.id = id;
        this.dateOfDownload = dateOfDownload;
        this.fileName = fileName;
        this.comment = comment;
        this.contact_id = contact_id;
    }

    public Attachment(Long id, Date dateOfDownload, String fileName, Blob file, String comment, Long contact_id) {
        this.id = id;
        this.dateOfDownload = dateOfDownload;
        this.fileName = fileName;
        this.file = file;
        this.comment = comment;
        this.contact_id = contact_id;
    }

    public Long getContact_id() {
        return contact_id;
    }

    public void setContact_id(Long contact_id) {
        this.contact_id = contact_id;
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

    public Blob getFile() {
        return file;
    }

    public void setFile(Blob file) {
        this.file = file;
    }
}
