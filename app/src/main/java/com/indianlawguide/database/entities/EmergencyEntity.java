package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "emergencies")
public class EmergencyEntity implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "number")
    private String number;

    @ColumnInfo(name = "description")
    private String description;

    public EmergencyEntity() {
    }

    @androidx.room.Ignore
    public EmergencyEntity(long id, String name, String number, String description) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
