package com.example.fidelitycorporation.Entities;


import java.io.Serializable;
import java.util.Date;

public class Client implements Serializable {
    private int id;
    private String firstName,lastName;
    private String birthDate;
    private String points;
    private String uriProfilePicture;
    private boolean statusBirthdate;
    private String phonenumber;
    private String country;
    private String city;
    private String address;
    private String image;

    public Client()  {
    }

    public Client(String firstName, String lastName, String birthDate, String points, String uriProfilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.points = points;
        this.uriProfilePicture = uriProfilePicture;
    }

    public Client(String firstName, String lastName, String points, String uriProfilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.points = points;
        this.uriProfilePicture = uriProfilePicture;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatusBirthdate() {
        return statusBirthdate;
    }

    public void setStatusBirthdate(boolean statusBirthdate) {
        this.statusBirthdate = statusBirthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getUriProfilePicture() {
        return uriProfilePicture;
    }

    public void setUriProfilePicture(String uriProfilePicture) {
        this.uriProfilePicture = uriProfilePicture;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", points='" + points + '\'' +
                ", uriProfilePicture='" + uriProfilePicture + '\'' +
                ", statusBirthdate=" + statusBirthdate +
                ", phonenumber='" + phonenumber + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", address='" + address + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}

