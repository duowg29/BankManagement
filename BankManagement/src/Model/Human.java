package Model;

import java.sql.Date;

public class Human {
    private String fullName;
    private String gender;
    private String dateOfBirth;
    private String address;
    private String phoneNumbers;
    private String email;
    private String citizenID;
    
    public Human() {
    	
    }

    public Human(String fullName, String gender, String dateOfBirth2, String address, String phoneNumbers, String email, String citizenID) {
        this.fullName = fullName;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth2;
        this.address = address;
        this.phoneNumbers = phoneNumbers;
        this.email = email;
        this.citizenID = citizenID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCitizenID() {
        return citizenID;
    }

    public void setCitizenID(String citizenID) {
        this.citizenID = citizenID;
    }
    @Override
    public String toString() {
        return "Human{" +
                "fullName='" + fullName + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumbers='" + phoneNumbers + '\'' +
                ", email='" + email + '\'' +
                ", citizenID='" + citizenID + '\'' +
                '}';
    }

}