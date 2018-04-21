package com.beloinc.meucv;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MeuCvDatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_CATEGORY = "CATEGORY";
    public static final String DB_DESCRIPTION = "DESCRIPTION";
    public static final String DB_IMAGE_RESOURCE_ID = "IMAGE_RESOURCE_ID";
    private static final String DB_NAME = "meucv";
    private static final int DB_VERSION = 1;

    MeuCvDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateMyDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void updateMyDatabase (SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE CURRICULUM (_id INTEGER PRIMARY KEY AUTOINCREMENT,  "
                    + DB_CATEGORY + " TEXT, "
                    + DB_DESCRIPTION + " TEXT, "
                    + DB_IMAGE_RESOURCE_ID + " INTEGER); "
            );
            insertInformation(db, "Graduação", "Engenharia Mecânica - Unesp", R.drawable.unesp_logo);
            insertInformation(db, "Experiência Profissional", "EMBRAER - Estágio em engenharia de desenvolvimento de produto. " +
                    "Atividades no estágio: Auxiliar na execução de testes de sistema. " +
                    "Auxiliar no projeto de novos produtos. Gestão das atividades da equipe. Acompanhar instalação de equipamentos no avião.",
                    R.drawable.embraer_logo);
            insertInformation(db, "Java and Android", "Java - Conhecimentos de métodos, classes e objetos, hierarquia e polimorfismo, manipulação de exceções." +
                    "\n\nAndroid - Ciclo de vida de atividades, fragmentos, toolbars, banco de dados SQLite. " +
                    "Aprendizado seguindo o livro Head First: Android Development em conjunto com API oficial do android: developer.android.com ",
                    R.drawable.android_java);
            insertInformation(db, "Internacional", "Ensino médio na Irlanda morando com uma família irlandesa por 1 ano.", R.drawable.ireland_map);
        }
    }

    private static void insertInformation (SQLiteDatabase db, String category, String description, int resourceId) {
        ContentValues meucvValues = new ContentValues();
        meucvValues.put(DB_CATEGORY, category);
        meucvValues.put(DB_DESCRIPTION, description);
        meucvValues.put(DB_IMAGE_RESOURCE_ID, resourceId);
        db.insert("CURRICULUM", null, meucvValues);
    }
}