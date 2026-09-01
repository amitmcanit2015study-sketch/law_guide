package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
    tableName = "history",
    indices = {@Index(value = {"law_id"}, unique = true)}
)
public class HistoryEntity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "law_id")
    private long lawId;

    @ColumnInfo(name = "viewed_at")
    private long viewedAt;

    public HistoryEntity(long lawId, long viewedAt) {
        this.lawId = lawId;
        this.viewedAt = viewedAt;
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

    public long getViewedAt() {
        return viewedAt;
    }

    public void setViewedAt(long viewedAt) {
        this.viewedAt = viewedAt;
    }
}
