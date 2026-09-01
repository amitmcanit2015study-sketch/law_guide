package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "laws")
public class LawEntity implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

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

    @ColumnInfo(name = "helpline")
    private String helpline;

    @ColumnInfo(name = "emergency_number")
    private String emergencyNumber;

    @ColumnInfo(name = "keywords")
    private String keywords;

    @ColumnInfo(name = "related_topics")
    private String relatedTopics;

    @ColumnInfo(name = "last_updated")
    private String lastUpdated;

    public LawEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getHelpline() {
        return helpline;
    }

    public void setHelpline(String helpline) {
        this.helpline = helpline;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getRelatedTopics() {
        return relatedTopics;
    }

    public void setRelatedTopics(String relatedTopics) {
        this.relatedTopics = relatedTopics;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
