package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "faqs")
public class FaqEntity implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "answer")
    private String answer;

    public FaqEntity() {
    }

    @androidx.room.Ignore
    public FaqEntity(long id, String question, String answer) {
        this.id = id;
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
