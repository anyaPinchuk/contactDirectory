package dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ContactDTO extends DTO{
    private Long id;
    private String name;
    private String surname;
    private String thirdName;
    private Date dateOfBirth;
    private String gender;
    private String citizenship;
    private String maritalStatus;
    private String webSite;
    private String email;
    private String job;
    private PhotoDTO photo;
    private AddressDTO address;
    private List<PhoneDTO> phoneDTOList;

    public ContactDTO() {
    }

    public ContactDTO(Long id, String name, String surname, String thirdName, Date dateOfBirth, String sex,
                      String citizenship, String maritalStatus, String webSite, String email, String job, AddressDTO address) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.thirdName = thirdName;
        this.dateOfBirth = dateOfBirth;
        this.gender = sex;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
        this.webSite = webSite;
        this.email = email;
        this.job = job;
        this.address = address;
        this.phoneDTOList = new ArrayList<>();
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getThirdName() {
        return thirdName;
    }

    public void setThirdName(String thirdName) {
        this.thirdName = thirdName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCitizenship() {
        return citizenship;
    }

    public void setCitizenship(String citizenship) {
        this.citizenship = citizenship;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public AddressDTO getAddress() {
        return address;
    }

    public void setAddress(AddressDTO address) {
        this.address = address;
    }

    public List<PhoneDTO> getPhoneDTOList() {
        return phoneDTOList;
    }

    public void setPhoneDTOList(List<PhoneDTO> phoneDTOList) {
        this.phoneDTOList = phoneDTOList;
    }

    public PhotoDTO getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoDTO photo) {
        this.photo = photo;
    }
}
