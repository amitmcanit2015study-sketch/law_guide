package com.indianlawguide.models;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    private String name;
    private String iconEmoji;
    private int iconColorRes;
    private int lawCount;

    public CategoryModel(String name, String iconEmoji, int iconColorRes, int lawCount) {
        this.name = name;
        this.iconEmoji = iconEmoji;
        this.iconColorRes = iconColorRes;
        this.lawCount = lawCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconEmoji() {
        return iconEmoji;
    }

    public void setIconEmoji(String iconEmoji) {
        this.iconEmoji = iconEmoji;
    }

    public int getIconColorRes() {
        return iconColorRes;
    }

    public void setIconColorRes(int iconColorRes) {
        this.iconColorRes = iconColorRes;
    }

    public int getLawCount() {
        return lawCount;
    }

    public void setLawCount(int lawCount) {
        this.lawCount = lawCount;
    }
}
