package com.bespalov.basaclub3.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.bespalov.basaclub3.data.ClubContract.MemberEntry;

import androidx.annotation.Nullable;

public class ClubDbOpenHelper extends SQLiteOpenHelper {

    public ClubDbOpenHelper(@Nullable Context context) {
        super(context, ClubContract.DATABASE_NAME, null, ClubContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE " + MemberEntry.TABLE_NAME + "(" +
                    MemberEntry.KEY_ID + " INTEGER PRIMARY KEY, " +
                    MemberEntry.KEY_FIRST_NAME + " TEXT, " +
                    MemberEntry.KEY_LAST_NAME + " TEXT, " +
                    MemberEntry.KEY_GENDER + "INTEGER NOT NULL, " +
                    MemberEntry.KEY_SPORT + "TEXT" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROPE TABLE IF EXISTS " + ClubContract.DATABASE_NAME);
//        db.insert(db);

    }
}
