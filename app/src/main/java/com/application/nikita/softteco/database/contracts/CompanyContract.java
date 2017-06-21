package com.application.nikita.softteco.database.contracts;


import android.provider.BaseColumns;

public class CompanyContract {

    public CompanyContract() {}

    public static abstract class CompanyEntry implements BaseColumns {
        public static final String TABLE_NAME = "company";
        public static final String COLUMN_NAME_COMPANY_NAME = "name";
        public static final String COLUMN_NAME_COMPANY_CATCH_PHRASE = "catch_phrase";
        public static final String COLUMN_NAME_COMPANY_BS = "bs";
    }
}
