package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4(contentEntity = LawEntity.class)
@Entity(tableName = "laws_fts")
public class LawFtsEntity {

    @PrimaryKey
    @ColumnInfo(name = "rowid")
    private long rowid;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "summary")
    private String summary;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "rights")
    private String rights;

    @ColumnInfo(name = "dos")
    private String dos;

    @ColumnInfo(name = "donts")
    private String donts;

    @ColumnInfo(name = "law_name")
    private String lawName;

    @ColumnInfo(name = "section")
    private String section;

    @ColumnInfo(name = "punishment")
    private String punishment;

    @ColumnInfo(name = "keywords")
    private String keywords;

    public LawFtsEntity(long rowid, String title, String category, String summary, String description,
                        String rights, String dos, String donts, String lawName, String section,
                        String punishment, String keywords) {
        this.rowid = rowid;
        this.title = title;
        this.category = category;
        this.summary = summary;
        this.description = description;
        this.rights = rights;
        this.dos = dos;
        this.donts = donts;
        this.lawName = lawName;
        this.section = section;
        this.punishment = punishment;
        this.keywords = keywords;
    }

    public long getRowid() {
        return rowid;
    }

    public void setRowid(long rowid) {
        this.rowid = rowid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getDos() {
        return dos;
    }

    public void setDos(String dos) {
        this.dos = dos;
    }

    public String getDonts() {
        return donts;
    }

    public void setDonts(String donts) {
        this.donts = donts;
    }

    public String getLawName() {
        return lawName;
    }

    public void setLawName(String lawName) {
        this.lawName = lawName;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getPunishment() {
        return punishment;
    }

    public void setPunishment(String punishment) {
        this.punishment = punishment;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
