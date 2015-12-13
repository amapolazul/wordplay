package com.amapolazul.www.wordplay;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amapolazul.www.wordplay.peristencia.DatabaseStartup;
import com.amapolazul.www.wordplay.peristencia.Pregunta;
import com.amapolazul.www.wordplay.peristencia.QuizDAO;
import com.amapolazul.www.wordplay.preguntas.PreguntasWordPlay;

import java.sql.SQLException;
import java.util.List;


public class MainActivity extends Activity {

    private QuizDAO quizDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            quizDao = new QuizDAO(this);
            quizDao.open();
            int[] imagenes = DatabaseStartup.darImagenesId();
            String[] respuestas = DatabaseStartup.palabrasRespuestas();
            int[] popups = DatabaseStartup.darPopupsId();
            //quizDao.removeAll();
            List<Pregunta> preguntas = quizDao.darPreguntas();
            if(preguntas == null || preguntas.isEmpty()){
                for(int i = 0; i < respuestas.length; i++){

                    Pregunta pregunta = new Pregunta();
                    pregunta.setImagen(imagenes[i]);
                    pregunta.setRespuestaCorrecta(String.valueOf(respuestas[i]));
                    pregunta.setPopupImagen(popups[i]);
                    System.out.println("guardando pregunta " + pregunta.getRespuestaCorrecta() + " " + pregunta.getPopupImagen());
                    quizDao.crearPregunta(pregunta);
                }
                quizDao.insertarPreguntaActual("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void gotoPreguntas(View view){
        Intent intent = new Intent(this, PreguntasWordPlay.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
