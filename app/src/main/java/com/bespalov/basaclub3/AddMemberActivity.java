package com.bespalov.basaclub3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentResolver;
import android.content.ContentValues;
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

public class AddMemberActivity extends AppCompatActivity {

    private EditText editLastName;
    private EditText editFirstName;
    private EditText editSport;
    private Spinner spinerGender;
    private int gender = 0;

    private ArrayAdapter spinerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        init();
        spinerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedGender = (String) adapterView.getItemAtPosition(i);
                if (!TextUtils.isEmpty(selectedGender)) {
                    if (selectedGender.equals("Male")) {
                        gender = MemberEntry.GENDER_MALE;
                    } if (selectedGender.equals("Famele")) {
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

    public void init () {
        editLastName = findViewById(R.id.editLastName);
        editFirstName = findViewById(R.id.editFirstName);
        editSport = findViewById(R.id.editSport);
        spinerGender = findViewById(R.id.spinerGender);
        spinerAdapter = ArrayAdapter.createFromResource(this,R.array.array_gender, android.R.layout.simple_spinner_item);
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

    private void insertMember () {

        String firstName = editFirstName.getText().toString().trim();
        String lastName = editLastName.getText().toString().trim();
        String sport = editSport.getText().toString().trim();
        ContentValues values = new ContentValues();
        values.put(MemberEntry.KEY_FIRST_NAME, firstName);
        values.put(MemberEntry.KEY_LAST_NAME, lastName);
        values.put(MemberEntry.KEY_SPORT, sport);
        values.put(MemberEntry.KEY_GENDER, gender);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = contentResolver.insert(MemberEntry.CONTENT_URI,values);

        if (uri == null) {
            Toast.makeText(this,"Can't query incorrectURI " + uri, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this,"data saved" , Toast.LENGTH_LONG).show();
        }
    }
}