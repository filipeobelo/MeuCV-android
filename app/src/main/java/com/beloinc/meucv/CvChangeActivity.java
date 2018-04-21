package com.beloinc.meucv;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;


public class CvChangeActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private ArrayList<String> descriptions = new ArrayList<>();
    private ArrayList<String> spinnerList = new ArrayList<>();
    private EditText editText;
    private int position = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cv_change);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        editText = findViewById(R.id.edit_description);
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
            descriptions = savedInstanceState.getStringArrayList("descriptions");
            spinnerList = savedInstanceState.getStringArrayList("list");
        } else {
            spinnerList.add("Selecione uma categoria");
            try {
                SQLiteOpenHelper meucvDatabaseHelper = new MeuCvDatabaseHelper(this);
                SQLiteDatabase db = meucvDatabaseHelper.getReadableDatabase();
                Cursor cursor = db.query("CURRICULUM", new String[]{
                                "_id",
                                MeuCvDatabaseHelper.DB_CATEGORY, MeuCvDatabaseHelper.DB_DESCRIPTION},
                        null, null, null, null, null);
                for (int i = 0; i < cursor.getCount(); i++) {
                    if (cursor.moveToPosition(i)) {
                        descriptions.add(cursor.getString(2));
                        spinnerList.add(cursor.getString(1));
                    }
                }
                cursor.close();
                db.close();
            } catch (SQLException e) {
                Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerList){
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(Color.GRAY);
                } else {
                    textView.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
        outState.putStringArrayList("descriptions", descriptions);
        outState.putStringArrayList("list", spinnerList);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if (i > 0) {
            if (position > 0) {
                descriptions.set(position -1, String.valueOf(editText.getText()));
            }
            editText.setText(descriptions.get(i - 1));
            position = i;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void onSave(View view) {
        if (position > 0) {
            descriptions.set(position - 1, String.valueOf(editText.getText()));
        }
        ContentValues contentDescription = new ContentValues();
        SQLiteOpenHelper meucvDatabaseHelper = new MeuCvDatabaseHelper(this);
        try {
            SQLiteDatabase db = meucvDatabaseHelper.getWritableDatabase();
            for (int i = 0; i < descriptions.size(); i++) {
                contentDescription.put(MeuCvDatabaseHelper.DB_DESCRIPTION, descriptions.get(i));
                db.update("CURRICULUM", contentDescription, "_id = ?", new String[]{Integer.toString(i+1)});
            }
            db.close();
            Toast.makeText(this, "Atualizado com sucesso!!", Toast.LENGTH_SHORT).show();
        } catch (SQLException e){
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }
}