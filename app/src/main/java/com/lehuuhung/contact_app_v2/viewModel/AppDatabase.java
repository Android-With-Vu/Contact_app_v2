package com.lehuuhung.contact_app_v2.viewModel;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.lehuuhung.contact_app_v2.model.Contact;

@Database(entities = Contact.class, version = 2,  exportSchema = false)
@TypeConverters({BitmapTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract ContactDao contactDAO();

    private static AppDatabase instance;

    public static AppDatabase getInstance(Context context) {
        if(instance == null  ){
            instance = Room.databaseBuilder(context,
                    AppDatabase.class, "contacts_v2")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
