package com.example.android.loggingjournal.JournalDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.provider.SyncStateContract;

import com.example.android.loggingjournal.JournalDatabase.Journal;
import com.example.android.loggingjournal.JournalDatabase.JournalDAO;
import com.example.android.loggingjournal.util.Constants;
import com.example.android.loggingjournal.util.DateRoomConverter;


@Database(entities = {Journal.class}, version = 1, exportSchema = false)
@TypeConverters({DateRoomConverter.class})
public abstract class JournalDatabase extends RoomDatabase {


    public abstract JournalDAO getJournalDAO();

    private static JournalDatabase journalDB;

    public static JournalDatabase getInstance(Context context) {
        if (null == journalDB) {
            journalDB = buildDatabaseInstance(context);
        }
        return journalDB;
    }

    private static JournalDatabase buildDatabaseInstance(Context context) {
        return Room.databaseBuilder(context.getApplicationContext(),
                JournalDatabase.class,
                Constants.DB_NAME)
                .allowMainThreadQueries().build();
    }

    public void cleanUp(){
        journalDB = null;
    }

}
