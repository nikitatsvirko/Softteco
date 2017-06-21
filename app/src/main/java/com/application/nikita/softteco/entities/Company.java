package com.application.nikita.softteco.entities;

public class Company {

    private String mName;
    private String mCatchPhrase;
    private String mBS;

    public String getName() {
        return mName;
    }

    public String getCatchPhrase() {
        return mCatchPhrase;
    }

    public void setCatchPhrase(String catchPhrase) {
        mCatchPhrase = catchPhrase;
    }

    public String getBS() {
        return mBS;
    }

    public void setBS(String BS) {
        mBS = BS;
    }

    public void setName(String name) {
        mName = name;
    }
}
