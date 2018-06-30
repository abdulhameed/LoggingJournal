package com.example.android.loggingjournal.JournalDatabase;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.loggingjournal.util.Constants;

import java.util.List;

@Dao
public interface JournalDAO {
    @Query("SELECT * FROM " + Constants.TABLE_NAME_JOURNAL)

    List<Journal> getJournals();


    /*
     * Insert the object in database
     * @param journal, object to be inserted
     */
    @Insert
    long insertJournals(Journal journal);

    /*
     * updateJournals the object in database
     * @param journal, object to be updated
     */
    @Update
    void updateJournals(Journal repos);

    /*
     * deleteJournals the object from database
     * @param journal, object to be deleted
     */
    @Delete
    void deleteJournals(Journal journal);

    /*
     * deleteJournals list of objects from database
     * @param journal, array of objects to be deleted
     */
    @Delete
    void deleteJournals(Journal... journals);      // Note... is varargs, here note is an array


}
