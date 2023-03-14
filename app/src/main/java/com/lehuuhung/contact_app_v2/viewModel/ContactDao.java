package com.lehuuhung.contact_app_v2.viewModel;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.lehuuhung.contact_app_v2.model.Contact;

import java.util.List;

@Dao
public interface ContactDao {

    @Query("SELECT * FROM Contact")
    List<Contact> getAll();

    @Query("SELECT * FROM Contact where id=:id")
    Contact getContact(int id);

    @Insert
    void insert (Contact... contacts);

    @Update
    void update (Contact... contacts);

    @Query("DELETE FROM Contact where id=:id")
    void delete (int id);
}
