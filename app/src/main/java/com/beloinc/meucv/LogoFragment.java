package com.beloinc.meucv;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogoFragment extends Fragment {
    private int imageId;
    private SQLiteDatabase db;
    private Cursor cursor;


    public LogoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logo, container, false);
        if (savedInstanceState != null) {
            imageId = savedInstanceState.getInt("imageId");
        }
        try {
            SQLiteOpenHelper meucvDatabaseHelper = new MeuCvDatabaseHelper(inflater.getContext()); //fragment is not child of Context, need to get context from LayoutInflater
            db = meucvDatabaseHelper.getReadableDatabase();
            cursor = db.query("CURRICULUM", new String[]{MeuCvDatabaseHelper.DB_IMAGE_RESOURCE_ID},
                    "_id = ?", new String[]{Integer.toString(imageId)}, null, null, null);
            if (cursor.moveToFirst()) {
                int imageResourceId = cursor.getInt(0);
                ImageView imageView = view.findViewById(R.id.logo_image);
                imageView.setImageResource(imageResourceId);
                }
        } catch (SQLiteException e) {
            Toast.makeText(inflater.getContext(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();
        // Inflate the layout for this fragment
        return view;
    }


    public void setListPosition (int id) {
        this.imageId = id + 1; //ListView position starts at 0, database starts at 1;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("imageId", imageId);
    }
}
