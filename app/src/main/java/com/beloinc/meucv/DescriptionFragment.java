package com.beloinc.meucv;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class DescriptionFragment extends Fragment {
    private int listPosition;
    private SQLiteDatabase db;
    private Cursor cursor;

    public DescriptionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        if (savedInstanceState != null) {
            listPosition = savedInstanceState.getInt("listPosition");
        }
        try {
            SQLiteOpenHelper meucvDatabaseHelper = new MeuCvDatabaseHelper(inflater.getContext()); //fragment is not child of Context, need to get context from LayoutInflater
            db = meucvDatabaseHelper.getReadableDatabase();
            cursor = db.query("CURRICULUM", new String[]{MeuCvDatabaseHelper.DB_DESCRIPTION},
                    "_id = ?", new String[]{Integer.toString(listPosition)}, null, null, null);
            if (cursor.moveToFirst()) {
                String description = cursor.getString(0);
                TextView textView = view.findViewById(R.id.description_text);
                textView.setText(description);
            }
        } catch (SQLiteException e) {
            Toast.makeText(inflater.getContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        return view;
    }

    public void setListPosition (int id) {
        this.listPosition = id + 1; //ListView position starts at 0, database starts at 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("listPosition", listPosition);
    }
}
