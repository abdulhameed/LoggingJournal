package com.example.android.loggingjournal.JournalDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "journal")
public class Journal {
    @PrimaryKey(autoGenerate = true)
    private int journal_id;

    @ColumnInfo(name = "journal_content") // column name will be "journal_content" instead of "content" in table
    private String content;

    private String title;

   // private

    public Journal(String content, String title) {
        this.journal_id = journal_id;
        this.content = content;
        this.title = title;
    }

    public int getJournal_id() {
        return journal_id;
    }

    public void setJournal_id(int note_id) {
        this.journal_id = note_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Journal)) return false;


                 Journal journal = (Journal) o;

        if (journal_id != journal.journal_id) return false;
        return title != null ? title.equals(journal.title) : journal.title == null;
    }

    @Override
    public int hashCode() {
        int result = journal_id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Note{" +
                "note_id=" + journal_id +
                ", content='" + content + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
