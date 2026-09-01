package com.indianlawguide.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "quizzes")
public class QuizEntity implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "question")
    private String question;

    @ColumnInfo(name = "option1")
    private String option1;

    @ColumnInfo(name = "option2")
    private String option2;

    @ColumnInfo(name = "option3")
    private String option3;

    @ColumnInfo(name = "option4")
    private String option4;

    @ColumnInfo(name = "answer")
    private int answer;

    @ColumnInfo(name = "category")
    private String category;

    public QuizEntity() {
    }

    @androidx.room.Ignore
    public QuizEntity(long id, String question, String option1, String option2, String option3, String option4, int answer, String category) {
        this.id = id;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.answer = answer;
        this.category = category;
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

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
