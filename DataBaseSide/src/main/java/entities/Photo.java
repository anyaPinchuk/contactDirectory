package entities;

public class Photo {
    private Long id;
    private String name;
    private Long contactId;

    public Photo() {
    }

    public Photo(String name, Long contactId) {
        this.name = name;
        this.contactId = contactId;
    }

    public Photo(Long id, String name, Long contactId) {
        this.id = id;
        this.name = name;
        this.contactId = contactId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getContactId() {
        return contactId;
    }

    public void setContactId(Long contactId) {
        this.contactId = contactId;
    }
}
