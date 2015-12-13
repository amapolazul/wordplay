package com.amapolazul.www.wordplay.peristencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jsmartinez on 21/08/2015.
 */
public class QuizDAO {

    private SQLiteDatabase database;
    private QuizSQLiteHelper dbHelper;
    private String[] allColumns = {
            QuizSQLiteHelper.NOMBRE_COLUMNA_ID,
            QuizSQLiteHelper.NOMBRE_COLUMNA_RESPUESTA_CORRECTA,
            QuizSQLiteHelper.NOMBRE_COLUMNA_IMAGEN,
            QuizSQLiteHelper.NOMBRE_POPUP_IMAGEN
    };

    public QuizDAO(Context context) {
        dbHelper = new QuizSQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long crearPregunta(Pregunta pregunta) {
        ContentValues values = new ContentValues();
        values.put(QuizSQLiteHelper.NOMBRE_COLUMNA_RESPUESTA_CORRECTA, pregunta.getRespuestaCorrecta());
        values.put(QuizSQLiteHelper.NOMBRE_POPUP_IMAGEN, pregunta.getPopupImagen());
        values.put(QuizSQLiteHelper.NOMBRE_COLUMNA_IMAGEN, pregunta.getImagen());
        long insertId = database.insert(QuizSQLiteHelper.NOMBRE_TABLA, null,
                values);
        return insertId;
    }

    /**
     * Devuelvelas preguntas
     *
     * @return
     */
    public List<Pregunta> darPreguntas() {
        String query = "SELECT * FROM " + QuizSQLiteHelper.NOMBRE_TABLA;
        Cursor c = database.rawQuery(query, null);
        List<Pregunta> preguntasList = new ArrayList<Pregunta>();
        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                Pregunta pretuntaResultado = cursorToPregunta(c);
                preguntasList.add(pretuntaResultado);
                c.moveToNext();
            }
            return preguntasList;
        } else {
            return null;
        }
    }

    public Pregunta darPreguntaPorId(String id) {
        String query = "SELECT * FROM " + QuizSQLiteHelper.TABLA_PREGUNTA_ACTUAL + "where _id = ?";
        Cursor c = database.rawQuery(query, new String[]{id});
        Pregunta pretuntaResultado;
        if (c.moveToFirst()) {
            pretuntaResultado = cursorToPregunta(c);
            c.moveToNext();
            return pretuntaResultado;
        } else {
            return null;
        }
    }

    public int darIdPreguntaActual() {
        String query = "SELECT * FROM " + QuizSQLiteHelper.TABLA_PREGUNTA_ACTUAL;
        Cursor c = database.rawQuery(query, null);
        int result;
        if (c.moveToFirst()) {
            result = c.getInt(0);
            c.moveToNext();
            return result;
        } else {
            return 1;
        }
    }

    public int actualizarPreguntaActual(String idPreguntaActual){
        ContentValues values = new ContentValues();
        values.put(QuizSQLiteHelper.QUIZ_PREGUNTA_ACTUAL, idPreguntaActual);
        int update = database.update(QuizSQLiteHelper.TABLA_PREGUNTA_ACTUAL,
                values,  "id_pregunta_actual "+"="+(Integer.parseInt(idPreguntaActual)), null);
        return update;
    }

    public long insertarPreguntaActual(String idPreguntaActual){
        ContentValues values = new ContentValues();
        values.put(QuizSQLiteHelper.QUIZ_PREGUNTA_ACTUAL, idPreguntaActual);
        long insert = database.insert(QuizSQLiteHelper.TABLA_PREGUNTA_ACTUAL,
                null, values);
        return insert;
    }

    public void removeAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(QuizSQLiteHelper.NOMBRE_TABLA, null, null);
        db.delete(QuizSQLiteHelper.TABLA_PREGUNTA_ACTUAL, null, null);
    }

    private Pregunta cursorToPregunta(Cursor cursor) {
        Pregunta pregunta = new Pregunta();
        pregunta.setId(cursor.getLong(0));
        pregunta.setImagen(cursor.getInt(1));
        pregunta.setRespuestaCorrecta(cursor.getString(2));
        pregunta.setPopupImagen(cursor.getInt(3));
        return pregunta;
    }
}
