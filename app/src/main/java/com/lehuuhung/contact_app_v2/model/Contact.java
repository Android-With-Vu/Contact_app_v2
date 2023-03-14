package com.lehuuhung.contact_app_v2.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Contact {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo
    private String name;

    @ColumnInfo
    private String mobile;

    @ColumnInfo
    private String email;

    @ColumnInfo
    private String home;

    @ColumnInfo
    private byte[] image;

    public Contact(String name, String mobile, String email, String home) {
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.home = home;
    }
    public Contact(Contact contact) {
        this.name = contact.name;
        this.mobile = contact.mobile;
        this.email = contact.email;
        this.home = contact.home;
    }
    public Contact() {

    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
