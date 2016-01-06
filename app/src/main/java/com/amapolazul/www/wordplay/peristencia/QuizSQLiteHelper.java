package com.amapolazul.www.wordplay.peristencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jsmartinez on 17/03/2015.
 */
public class QuizSQLiteHelper extends SQLiteOpenHelper {

    public static final String NOMBRE_TABLA = "preguntas";
    public static final String NOMBRE_COLUMNA_ID = "_id";
    public static final String NOMBRE_COLUMNA_IMAGEN = "imagen";
    public static final String NOMBRE_POPUP_IMAGEN = "popup";
    public static final String NOMBRE_COLUMNA_RESPUESTA_CORRECTA = "respuesta_correcta";

    public static final String QUIZ_PREGUNTA_ACTUAL = "id_pregunta_actual";
    public static final String TABLA_PREGUNTA_ACTUAL = "preguntaactual";


    private static final String DATABASE_NAME = "wordplay.db";
    private static final int DATABASE_VERSION = 2;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + NOMBRE_TABLA + "(" + NOMBRE_COLUMNA_ID
            + " integer primary key autoincrement, " + NOMBRE_COLUMNA_IMAGEN
            + " integer not null," + NOMBRE_COLUMNA_RESPUESTA_CORRECTA
            + " text not null," + NOMBRE_POPUP_IMAGEN
            + " integer not null" +
            ");";

    private static final String DATA_BASE_FASE_2 = "create table " + TABLA_PREGUNTA_ACTUAL + "(" + QUIZ_PREGUNTA_ACTUAL
            +" integer not null" +
            ");";

    public QuizSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
        database.execSQL(DATA_BASE_FASE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NOMBRE_TABLA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLA_PREGUNTA_ACTUAL);
        onCreate(db);
    }
}