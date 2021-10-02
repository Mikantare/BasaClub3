package com.bespalov.basaclub3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bespalov.basaclub3.data.ClubContract;
import com.bespalov.basaclub3.data.ClubContract.MemberEntry;

public class AddMemberActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri currentUri;
    private EditText editLastName;
    private EditText editFirstName;
    private EditText editSport;
    private Spinner spinerGender;
    private int gender = 0;

    private ArrayAdapter spinerAdapter;
    private static final int EDIT_MEMBER_LOADER = 321;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        Intent intent = getIntent();
        currentUri = intent.getData();
        if (currentUri == null) {
            setTitle("Add a member");
        } else {
            setTitle("Edit the Member");
        }

        getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);
        init();

        spinerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGender = (String) adapterView.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selectedGender)) {
                    if (selectedGender.equals("Male")) {
                        gender = MemberEntry.GENDER_MALE;
                    } else if (selectedGender.equals("Female")) {
                        gender = MemberEntry.GENDER_FEMALE;
                    } else {
                        gender = MemberEntry.GENDER_UNKNOW;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                gender = 0;
            }
        });
    }

    public void init() {
        editLastName = findViewById(R.id.editLastName);
        editFirstName = findViewById(R.id.editFirstName);
        editSport = findViewById(R.id.editSport);
        spinerGender = findViewById(R.id.spinerGender);
        spinerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerGender.setAdapter(spinerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_member_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_member:
                insertMember();
                return true;
            case R.id.delete_member:
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertMember() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String sport = editSport.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(MemberEntry.KEY_FIRST_NAME, firstName);
        values.put(MemberEntry.KEY_LAST_NAME, lastName);
        values.put(MemberEntry.KEY_SPORT, sport);
        values.put(MemberEntry.KEY_GENDER, gender);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, values);

        if (uri == null) {
            Toast.makeText(this, "Can't query incorrectURI " + uri, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "data saved", Toast.LENGTH_LONG).show();
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String[] projection = {MemberEntry.KEY_ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT};
        return new CursorLoader(this, currentUri, projection,
                null, null,null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int firstNameIndex = cursor.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
            int lastNameIndex = cursor.getColumnIndex(MemberEntry.KEY_LAST_NAME);
            int genderIndex = cursor.getColumnIndex(MemberEntry.KEY_GENDER);
            int sportIndex = cursor.getColumnIndex(MemberEntry.KEY_SPORT);

            String firstName = cursor.getString(firstNameIndex);
            String lastNAme = cursor.getString(lastNameIndex);
            String sport = cursor.getString(sportIndex);

            switch (gender) {
                case (MemberEntry.GENDER_MALE):
                    spinerGender.setSelection(MemberEntry.GENDER_MALE);

                    break;
                case (MemberEntry.GENDER_FEMALE):
                    spinerGender.setSelection(MemberEntry.GENDER_FEMALE);
                    break;
                case (MemberEntry.GENDER_UNKNOW):
                    spinerGender.setSelection(MemberEntry.GENDER_UNKNOW);
                    break;
            }
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}