package com.facebook.fragment_app;

public class ImageData {
    private String name;
    private String imagePath;
    private String bio;
    private String phone;
    private String address;
    private String email;
    private String gender;

    public ImageData(String name, String imagePath, String bio, String phone, String address, String email, String gender) {
        this.name = name;
        this.imagePath = imagePath;
        this.bio = bio;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

