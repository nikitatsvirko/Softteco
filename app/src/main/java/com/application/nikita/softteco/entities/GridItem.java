package com.application.nikita.softteco.entities;

import java.io.Serializable;

public class GridItem implements Serializable {

    private static final long serialVersionUID = 1L;

    private String mId;
    private String mUserId;
    private String mTitle;

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }
}
