package entities;

public class Address {
    private Long id;
    private String country;
    private String city;
    private String streetAddress;
    private String index;

    public Address() {
    }

    public Address(Long id, String country, String city, String streetAddress, String index) {
        this.id = id;
        this.country = country;
        this.city = city;
        this.streetAddress = streetAddress;
        this.index = index;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
