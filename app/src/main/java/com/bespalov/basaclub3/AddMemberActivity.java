package com.bespalov.basaclub3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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
            invalidateOptionsMenu();
        } else {
            setTitle("Edit the Member");
            getSupportLoaderManager().initLoader(EDIT_MEMBER_LOADER, null, this);
        }


        editLastName = findViewById(R.id.editLastName);
        editFirstName = findViewById(R.id.editFirstName);
        editSport = findViewById(R.id.editSport);
        spinerGender = findViewById(R.id.spinerGender);

        spinerAdapter = ArrayAdapter.createFromResource(this, R.array.array_gender, android.R.layout.simple_spinner_item);
        spinerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinerGender.setAdapter(spinerAdapter);


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

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (currentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.delete_member);
            menuItem.setVisible(false);
        }
        return true;
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
                SaveMember();
                return true;
            case R.id.delete_member:
                shwoDeleteMemberDialog();
                return true;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void SaveMember() {
        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String sport = editSport.getText().toString().trim();

        if (TextUtils.isEmpty(firstName)) {
            Toast.makeText(this, "Input the first name", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Input the last name", Toast.LENGTH_LONG).show();
            return;
        } else if (TextUtils.isEmpty(sport)) {
            Toast.makeText(this, "Input the sport", Toast.LENGTH_LONG).show();
            return;
        } else if (gender == MemberEntry.GENDER_UNKNOW) {
            Toast.makeText(this, "Choose the gender", Toast.LENGTH_LONG).show();
            return;
        }


        ContentValues values = new ContentValues();
        values.put(MemberEntry.KEY_FIRST_NAME, firstName);
        values.put(MemberEntry.KEY_LAST_NAME, lastName);
        values.put(MemberEntry.KEY_SPORT, sport);
        values.put(MemberEntry.KEY_GENDER, gender);

        if (currentUri == null) {
            ContentResolver contentResolver = getContentResolver();
            Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI, values);

            if (uri == null) {
                Toast.makeText(this, "Can't query incorrectURI " + uri, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "data saved", Toast.LENGTH_LONG).show();
            }
        } else {
            int rowsChanged = getContentResolver().update(currentUri, values,
                    null, null);
            if (rowsChanged == 0) {
                Toast.makeText(this, "data saved in the table falied", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Member update", Toast.LENGTH_LONG).show();
            }
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
                null, null, null);
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
            int genderToEdit = cursor.getInt(genderIndex);
            Log.i("MySpiner", "" + genderToEdit);

            spinerGender.setSelection(genderToEdit);
            editFirstName.setText(firstName);
            editLastName.setText(lastNAme);
            editSport.setText(sport);
        }


    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    private void shwoDeleteMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want Delete Member?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMember();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteMember() {
        if (currentUri != null) {
            int rawsDeleted = getContentResolver().delete(currentUri, null, null);
            if (rawsDeleted == 0) {
                Toast.makeText(this, "Deleted data from the table flied", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Member deleted", Toast.LENGTH_LONG).show();
            }
        }
        finish();
    }
}