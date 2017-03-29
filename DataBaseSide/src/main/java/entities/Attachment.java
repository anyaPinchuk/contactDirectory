package entities;

public class Attachment {
    private Long id;
    private java.sql.Date dateOfDownload;
    private String fileName;
    private String comment;
    private Long contactId;

    public Attachment(Long id, java.sql.Date dateOfDownload, String fileName, String comment, Long contact_id) {
        this.id = id;
        this.dateOfDownload = dateOfDownload;
        this.fileName = fileName;
        this.comment = comment;
        this.contactId = contact_id;
    }

    public Attachment(String fileName) {
        this.fileName = fileName;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public java.sql.Date getDateOfDownload() {
        return dateOfDownload;
    }

    public void setDateOfDownload(java.sql.Date dateOfDownload) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attachment that = (Attachment) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
