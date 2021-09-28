package com.bespalov.basaclub3.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
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

        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match) {
            case MEMBERS:
                cursor = db.query(MemberEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case MEMBERS_ID:
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(MemberEntry.TABLE_NAME,
                        projection, selection, selectionArgs, null, null, sortOrder);
                break;

            default:
                Toast.makeText(getContext(), "Incorrect URI: " + uri, Toast.LENGTH_LONG).show();
                throw new IllegalArgumentException("Can't query incorrectURI " + uri);

        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        dataValidations(contentValues);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                long id = db.insert(MemberEntry.TABLE_NAME, null, contentValues);

                if (id == -1) {
                    Log.e("Insert metod", "insertion of data in the table failed for " + uri);
                    return null;
                }
                return ContentUris.withAppendedId(uri, id);

            default:
                throw new IllegalArgumentException("Can't udate this uri: " + uri);

        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {


        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                return db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);

            case MEMBERS_ID:
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.delete(MemberEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Can't delete this uri: " + uri);

        }
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        dataValidations(contentValues);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                return db.update(MemberEntry.TABLE_NAME, contentValues, selection, selectionArgs);

            case MEMBERS_ID:
                selection = MemberEntry.KEY_ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return db.update(MemberEntry.TABLE_NAME, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Can't query incorrectURI " + uri);

        }

    }

    @Override
    public String getType(Uri uri) {

        int match = uriMatcher.match(uri);

        switch (match) {
            case MEMBERS:
                return MemberEntry.CONTENT_MULTIPLE_ITEMS;
            case MEMBERS_ID:
                return MemberEntry.CONTENT_SINGLE_ITEMS;
            default:
                throw new IllegalArgumentException("Unknow Uri: " + uri);

        }
    }
private void dataValidations (ContentValues contentValues) {
    if (contentValues.containsKey(MemberEntry.KEY_FIRST_NAME)) {
        String firstName = contentValues.getAsString(MemberEntry.KEY_FIRST_NAME);
        if (firstName.equals("")) {
            throw new IllegalArgumentException("you have to input first name");
        }
    }
    if (contentValues.containsKey(MemberEntry.KEY_LAST_NAME)) {
        String lastName = contentValues.getAsString(MemberEntry.KEY_LAST_NAME);
        if (lastName.equals("")) {
            throw new IllegalArgumentException("you have to input last name");
        }
    }
    if (contentValues.containsKey(MemberEntry.KEY_SPORT)) {
        String sport = contentValues.getAsString(MemberEntry.KEY_SPORT);
        if (sport.equals("")) {
            throw new IllegalArgumentException("you have to input sport name");
        }
    }



}
}
