package com.bespalov.basaclub3.data;

import android.provider.BaseColumns;

public final class ClubContract {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "memberDB";

    private ClubContract () {

    }

    public static final class MemberEntry implements BaseColumns {

        public static final String TABLE_NAME = "members";

        public static final String KEY_ID = BaseColumns._ID;
        public static final String KEY_FIRST_NAME = "firstName";
        public static final String KEY_LAST_NAME = "lastName";
        public static final String KEY_GENDER = "gender";
        public static final String KEY_SPORT = "sport";

        public static final int GENDER_UNKNOW = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;

    }


}


