package com.application.nikita.softteco.database.contracts;

import android.provider.BaseColumns;

public class AddressContract {

    public AddressContract() {}

    public static abstract class AddressEntry implements BaseColumns{
        public static final String TABLE_NAME = "address";
        public static final String COLUMN_NAME_ADDRESS_STREET = "street";
        public static final String COLUMN_NAME_ADDRESS_SUITE = "suite";
        public static final String COLUMN_NAME_ADDRESS_CITY = "city";
        public static final String COLUMN_NAME_ADDRESS_ZIPCODE = "zipcode";
        public static final String COLUMN_NAME_ADDRESS_LATITUDE = "latitude";
        public static final String COLUMN_NAME_ADDRESS_LONGITUDE = "longitude";
    }
}
