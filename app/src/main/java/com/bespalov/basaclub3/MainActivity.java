package com.bespalov.basaclub3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.bespalov.basaclub3.data.ClubContract.MemberEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private TextView dataTextView;
    private ListView listViewMembers;

    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        dataTextView = findViewById(R.id.dataTextView);
        listViewMembers = findViewById(R.id.listViewMembers);
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayData();
    }

    private void displayData() {
        String[] projection = {MemberEntry.KEY_ID,
                MemberEntry.KEY_FIRST_NAME,
                MemberEntry.KEY_LAST_NAME,
                MemberEntry.KEY_GENDER,
                MemberEntry.KEY_SPORT};
        Cursor cursor = getContentResolver().query(MemberEntry.CONTENT_URI,
                projection, null, null, null);

//        dataTextView.setText("All members\n\n");
        MemberCursorAdapter memberCursorAdapter = new MemberCursorAdapter(this, cursor, false);
        listViewMembers.setAdapter(memberCursorAdapter);

//        ataTextView.append(MemberEntry.KEY_ID + " " +
//                MemberEntry.KEY_FIRST_NAME + " " +
//                MemberEntry.KEY_LAST_NAME + " " +
//                MemberEntry.KEY_GENDER + " " +
//                MemberEntry.KEY_SPORT);
//        int idIndex = cursor.getColumnIndex(MemberEntry.KEY_ID);
//        int firstNameIndex = cursor.getColumnIndex(MemberEntry.KEY_FIRST_NAME);
//        int lastNameIndex = cursor.getColumnIndex(MemberEntry.KEY_LAST_NAME);
//        int genderIndex = cursor.getColumnIndex(MemberEntry.KEY_GENDER);
//        int sportIndex = cursor.getColumnIndex(MemberEntry.KEY_SPORT);
//
//        while (cursor.moveToNext()) {
//            int currentID = cursor.getInt(idIndex);
//            String currentFirstName = cursor.getString(firstNameIndex);
//            String currentLastName = cursor.getString(lastNameIndex);
//            int currentGender = cursor.getInt(genderIndex);
//            String currentSport = cursor.getString(sportIndex);
//
//            dataTextView.append("\n" +
//                    currentID + " " +
//                    currentFirstName + " " +
//                    currentLastName + " " +
//                    currentGender + " " +
//                    currentSport);
//        }

    }
}