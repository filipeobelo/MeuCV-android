package com.beloinc.meucv;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase db;
    private Cursor cursor;
    private ListView listView;
    private ArrayList<Integer> controlArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState != null) {
            controlArray = savedInstanceState.getIntegerArrayList("controlArray");
        }
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = findViewById(R.id.list_category);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        try {
            SQLiteOpenHelper meucvDatabaseHelper = new MeuCvDatabaseHelper(this);
            db = meucvDatabaseHelper.getReadableDatabase();
            cursor = db.query("CURRICULUM", new String[]{
                            "_id",
                            MeuCvDatabaseHelper.DB_CATEGORY },
                    null, null, null, null, null);
            CursorAdapter listAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_activated_1, cursor,
                    new String[]{ MeuCvDatabaseHelper.DB_CATEGORY }, new int[]{android.R.id.text1}, 0);
            listView.setAdapter(listAdapter);
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    updateUI(i);
                }
            };
            listView.setOnItemClickListener(itemClickListener);
        } catch (SQLiteException e) {
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
        getSupportFragmentManager().addOnBackStackChangedListener(new android.support.v4.app.FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager fm = getSupportFragmentManager();
                if (controlArray.size() > 1 && controlArray.get(controlArray.size() - 1).equals(controlArray.get(controlArray.size() - 2))) {
                    Fragment fragment = fm.findFragmentByTag("visible_fragment");
                    FragmentTransaction ft = fm.beginTransaction();
                    ft.remove(fragment);
                    ft.commit();
                    fm.popBackStack();
                    controlArray.remove(controlArray.size()-1);
                }  else if (controlArray.size() != fm.getBackStackEntryCount()) {
                     controlArray.remove(controlArray.size()-1);
                     if (!controlArray.isEmpty()) {
                         listView.setItemChecked(controlArray.get(controlArray.size() - 1), true);
                     } else {
                         listView.clearChoices();
                     }
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_cv:
                FragmentManager fragmentManager = getSupportFragmentManager();
                if (fragmentManager.getBackStackEntryCount() != 0) {
                    controlArray.clear();
                    listView.clearChoices();
                    for (Fragment fragment:fragmentManager.getFragments()){
                        fragmentManager.beginTransaction().remove(fragment).commit();
                    }
                    fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
                Intent intent = new Intent(this, CvChangeActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("controlArray", controlArray);
    }


    private void updateUI (int position) {
        controlArray.add(position);
        LogoFragment logoFragment = new LogoFragment();
        logoFragment.setListPosition(position);
        DescriptionFragment descriptionFragment = new DescriptionFragment();
        descriptionFragment.setListPosition(position);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container_logo, logoFragment, "visible_fragment");
        ft.replace(R.id.fragment_container_description, descriptionFragment, "visible_fragment");
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }



    @Override
    protected void onDestroy() {
        cursor.close();
        db.close();
        super.onDestroy();
    }
}
