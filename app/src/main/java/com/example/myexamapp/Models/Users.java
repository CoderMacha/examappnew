package com.example.myexamapp.Models;

public class Users {
    String role, name, email, imageUri, UID;

    public Users() {}

    public Users(String role, String name, String email, String imageUri, String UID) {
        this.role = role;
        this.name = name;
        this.email = email;
        this.imageUri = imageUri;
        this.UID = UID;
    }

    public String getRole() { return role; }

    public void setRole(String role) { this.role = role; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }
}
