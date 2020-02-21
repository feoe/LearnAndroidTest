package com.example.sharepreferences1;

import org.litepal.crud.LitePalSupport;

public class Category extends LitePalSupport {
    private int id;
    private  String categoryNmae;
    private int categoryCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategoryNmae() {
        return categoryNmae;
    }

    public void setCategoryNmae(String categoryNmae) {
        this.categoryNmae = categoryNmae;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }
}
