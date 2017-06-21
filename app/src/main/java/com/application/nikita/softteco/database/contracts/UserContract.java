package com.application.nikita.softteco.database.contracts;

import android.provider.BaseColumns;

public class UserContract {

    public UserContract() {}

    public static abstract class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_USER_NAME = "name";
        public static final String COLUMN_NAME_USER_USERNAME = "username";
        public static final String COLUMN_NAME_USER_EMAIL = "email";
        public static final String COLUMN_NAME_USER_ADDRESS = "address_id";
        public static final String COLUMN_NAME_USER_PHONE = "phone";
        public static final String COLUMN_NAME_USER_WEBSITE = "website";
        public static final String COLUMN_NAME_USER_COMPANY = "company_id";
    }
}
