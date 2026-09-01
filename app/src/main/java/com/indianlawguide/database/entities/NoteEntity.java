package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(
    tableName = "notes",
    indices = {@Index(value = {"law_id"})}
)
public class NoteEntity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "law_id")
    private long lawId;

    @ColumnInfo(name = "law_title")
    private String lawTitle;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    public NoteEntity(long lawId, String lawTitle, String note, long createdAt) {
        this.lawId = lawId;
        this.lawTitle = lawTitle;
        this.note = note;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getLawId() {
        return lawId;
    }

    public void setLawId(long lawId) {
        this.lawId = lawId;
    }

    public String getLawTitle() {
        return lawTitle;
    }

    public void setLawTitle(String lawTitle) {
        this.lawTitle = lawTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
