package com.bespalov.basaclub3.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.bespalov.basaclub3.data.ClubContract.*;


public class ClubContentProvider extends ContentProvider {

    ClubDbOpenHelper dbOpenHelper;

    private static final int MEMBERS = 111;
    private static final int MEMBERS_ID = 222;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBERS, MEMBERS);
        uriMatcher.addURI(ClubContract.AUTHORITY, ClubContract.PATH_MEMBERS + "/#", MEMBERS_ID);
    }

    @Override
    public boolean onCreate() {
        dbOpenHelper = new ClubDbOpenHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();

        Cursor cursor ;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                   cursor = db.query(MemberEntry.TABLE_NAME,
                           projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEMBERS_ID:
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                Toast.makeText(getContext(), "Incorrect URI: " + uri, Toast.LENGTH_LONG).show();
                throw new IllegalArgumentException("Can't query incorrectURI " + uri);

        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

}
