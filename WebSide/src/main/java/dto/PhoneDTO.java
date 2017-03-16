package dto;

public class PhoneDTO extends DTO{
    private Long id;
    private String countryCode;
    private String operatorCode;
    private String number;
    private String numberType;
    private String comment;
    private ContactDTO contactDTO;

    public PhoneDTO() {
    }

    public PhoneDTO(Long id, String countryCode, String operatorCode, String number, String numberType, String comment,
                    ContactDTO contactDTO) {
        this.id = id;
        this.countryCode = countryCode;
        this.operatorCode = operatorCode;
        this.number = number;
        this.numberType = numberType;
        this.comment = comment;
        this.contactDTO = contactDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getNumberType() {
        return numberType;
    }

    public void setNumberType(String numberType) {
        this.numberType = numberType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ContactDTO getContactDTO() {
        return contactDTO;
    }

    public void setContactDTO(ContactDTO contactDTO) {
        this.contactDTO = contactDTO;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }
}
