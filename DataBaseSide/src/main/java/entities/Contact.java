package entities;

public class Contact {
    private Long id;
    private String name;
    private String surname;
    private String thirdName;
    private java.sql.Date dateOfBirth;
    private String gender;
    private String citizenship;
    private String maritalStatus;
    private String webSite;
    private String email;
    private String job;

    public Contact() {
    }

    public Contact(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public Contact(Long id, String name, String surname, String thirdName, java.sql.Date dateOfBirth, String gender,
                   String citizenship, String maritalStatus, String webSite, String email, String job) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.thirdName = thirdName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.citizenship = citizenship;
        this.maritalStatus = maritalStatus;
        this.webSite = webSite;
        this.email = email;
        this.job = job;
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

    public void setDateOfBirth(java.sql.Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public java.sql.Date getDateOfBirth() {
        return dateOfBirth;
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
    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", thirdName='" + thirdName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", citizenship='" + citizenship + '\'' +
                ", maritalStatus='" + maritalStatus + '\'' +
                ", webSite='" + webSite + '\'' +
                ", email='" + email + '\'' +
                ", job='" + job + '\'' +
                '}';
    }
}
