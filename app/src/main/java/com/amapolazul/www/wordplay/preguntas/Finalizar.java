package com.amapolazul.www.wordplay.preguntas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amapolazul.www.wordplay.R;
import com.amapolazul.www.wordplay.peristencia.QuizDAO;

import java.sql.SQLException;

public class Finalizar extends Activity {

    private QuizDAO quizDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalizar);
        try {
            quizDao = new QuizDAO(this);
            quizDao.open();
            quizDao.actualizarPreguntaActual("1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void goToPreguntas(View view) {
        Intent intent = new Intent(this, PreguntasWordPlay.class);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_finalizar, menu);
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
