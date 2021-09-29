package com.bespalov.basaclub3;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.bespalov.basaclub3.data.ClubContract.MemberEntry;

import org.w3c.dom.Text;

public class MemberCursorAdapter extends CursorAdapter {
    public MemberCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.member_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView cardFirstName = (TextView) view.findViewById(R.id.cardFirstName);
        TextView cardLastName = (TextView) view.findViewById(R.id.cardLastName);
        TextView cardSport = (TextView) view.findViewById(R.id.cardSport);

        String firstName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_FIRST_NAME));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_LAST_NAME));
        String sport = cursor.getString(cursor.getColumnIndexOrThrow(MemberEntry.KEY_SPORT));
        cardFirstName.setText(firstName);
        cardLastName.setText(lastName);
        cardSport.setText(sport);
    }
}
