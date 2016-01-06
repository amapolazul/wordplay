package com.amapolazul.www.wordplay.preguntas;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.amapolazul.www.wordplay.MainActivity;
import com.amapolazul.www.wordplay.R;
import com.amapolazul.www.wordplay.cache.Cache;
import com.amapolazul.www.wordplay.peristencia.Pregunta;
import com.amapolazul.www.wordplay.peristencia.QuizDAO;
import com.amapolazul.www.wordplay.utils.Utils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PreguntasWordPlay extends Activity {

    private ImageView[][] botones;
    private boolean[][] llenado;
    private StringBuffer respuesta;
    private TextView campoRespuesta;

    private QuizDAO quizDao;
    private int indicePrguntaActual;
    private Pregunta preguntaActual;
    private List<Pregunta> preguntas;

    private Animation animation;
    private Dialog busyDialog;

    private MediaPlayer sonidoCorrecto;
    private MediaPlayer sonidoIncorrecta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preguntas_word_play);

        campoRespuesta = (TextView) findViewById(R.id.textoRespuesta);

        botones = new ImageView[3][5];
        llenado = new boolean[3][5];
        inicializarBotones();

        animation = AnimationUtils.loadAnimation(this, R.anim.escalar);

        sonidoCorrecto = MediaPlayer.create(this, R.raw.correcta);
        sonidoIncorrecta = MediaPlayer.create(this, R.raw.incorrecta);

        try {
            quizDao = new QuizDAO(this);
            quizDao.open();
            preguntas = quizDao.darPreguntas();
            indicePrguntaActual = quizDao.darIdPreguntaActual();
            inicializarQuiz();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void inicializarBotones(){
        botones[0][0] = (ImageView)findViewById(R.id.letraUno);
        botones[0][1] = (ImageView)findViewById(R.id.letraDos);
        botones[0][2] = (ImageView)findViewById(R.id.letraTres);
        botones[0][3] = (ImageView)findViewById(R.id.letraCuatro);
        botones[0][4] = (ImageView)findViewById(R.id.letraCinco);
        botones[1][0] = (ImageView)findViewById(R.id.letraSeis);
        botones[1][1] = (ImageView)findViewById(R.id.letraSiete);
        botones[1][2] = (ImageView)findViewById(R.id.letraOcho);
        botones[1][3] = (ImageView)findViewById(R.id.letraNueve);
        botones[1][4] = (ImageView)findViewById(R.id.letraDiez);
        botones[2][0] = (ImageView)findViewById(R.id.letraOnce);
        botones[2][1] = (ImageView)findViewById(R.id.letraDoce);
        botones[2][2] = (ImageView)findViewById(R.id.letraTrece);
        botones[2][3] = (ImageView)findViewById(R.id.letraCatorce);
        botones[2][4] = (ImageView)findViewById(R.id.letraQuince);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_preguntas_word_play, menu);
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

    private Drawable getDrawableByName(String name){
        System.out.println("name letra" + name);
        Resources res = getResources();
        String mDrawableName = name;
        int resID = res.getIdentifier(mDrawableName , "drawable", getPackageName());
        Drawable drawable = res.getDrawable(resID );
        return drawable;
    }

    private void inicializarQuiz(){
        preguntaActual = preguntas.get(indicePrguntaActual);
        ImageView imagenPregunta = (ImageView) findViewById(R.id.imagenPregunta);
        imagenPregunta.setImageResource(preguntaActual.getImagen());
        respuesta = new StringBuffer("");
        campoRespuesta.setText(respuesta.toString());
        llenado = new boolean[3][5];

        String respuesta = preguntaActual.getRespuestaCorrecta();
        TreeSet<String> posiciones = Utils.darPosicionesLetras(respuesta);
        int index = 0;
        for(String posicion: posiciones) {
            String[] posicionVec = posicion.split(",");
            int x = Integer.parseInt(posicionVec[0]);
            int y = Integer.parseInt(posicionVec[1]);
            String letra = String.valueOf(respuesta.charAt(index)).toLowerCase();
            Drawable letraDraw = getDrawableByName(letra);
            botones[x][y].setImageDrawable(letraDraw);
            botones[x][y].setTag(letra);
            llenado[x][y] = true;
            index++;
        }

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 5; j++){
                if(llenado[i][j] == false){
                    String letra = Cache.cacheAbecedario()[Utils.darNumeroAleatorioAbecedario()].toLowerCase();
                    Drawable letraDraw = getDrawableByName(letra);
                    botones[i][j].setImageDrawable(letraDraw);
                    botones[i][j].setTag(letra);
                    llenado[i][j] = true;
                }
            }
        }
    }

    public void limpiarRespuesta(View view){
        respuesta = new StringBuffer("");
        campoRespuesta.setText(respuesta.toString());
    }

    public void comprobarRespuesta(View view){
        String respuestaFinal = campoRespuesta.getText().toString();
        busyDialog = new Dialog(this, R.style.lightbox_dialog);
        busyDialog.setContentView(R.layout.lightbox_dialog);
        if(preguntaActual.getRespuestaCorrecta().equals(respuestaFinal)){
            sonidoCorrecto.start();
            ImageView imagePopUp = (ImageView) busyDialog.findViewById(R.id.imagenPopup);
            imagePopUp.setImageResource(preguntaActual.getPopupImagen());
            busyDialog.show();
            ImageView dismissDialogImageView = (ImageView)busyDialog.findViewById(R.id.cerrarPopup);
            dismissDialogImageView.setOnClickListener(new ImageView.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    busyDialog.dismiss();
                    indicePrguntaActual++;
                    if(indicePrguntaActual < preguntas.size()){
                        quizDao.actualizarPreguntaActual(String.valueOf(indicePrguntaActual));
                        inicializarQuiz();
                    } else {
                        Intent intent = new Intent(PreguntasWordPlay.this, Finalizar.class);
                        startActivity(intent);
                    }
                }
            });

        } else {
            sonidoIncorrecta.start();
        }
    }

    public void cerrar(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void presionaTeclado(View view) {
        view.startAnimation(animation);
        ImageView boton = (ImageView)findViewById(view.getId());
        String tag = (String) boton.getTag();
        switch (tag){
            case "a" :
                respuesta.append("A");
                break;
            case "b" :
                respuesta.append("B");
                break;
            case "c" :
                respuesta.append("C");
                break;
            case "d" :
                respuesta.append("D");
                break;
            case "e" :
                respuesta.append("E");
                break;
            case "f" :
                respuesta.append("F");
                break;
            case "g" :
                respuesta.append("G");
                break;
            case "h" :
                respuesta.append("H");
                break;
            case "i" :
                respuesta.append("I");
                break;
            case "j" :
                respuesta.append("J");
                break;
            case "k" :
                respuesta.append("K");
                break;
            case "l" :
                respuesta.append("L");
                break;
            case "m" :
                respuesta.append("M");
                break;
            case "n" :
                respuesta.append("N");
                break;
            case "ñ" :
                respuesta.append("Ñ");
                break;
            case "o" :
                respuesta.append("O");
                break;
            case "p" :
                respuesta.append("P");
                break;
            case "q" :
                respuesta.append("Q");
                break;
            case "r" :
                respuesta.append("R");
                break;
            case "s" :
                respuesta.append("S");
                break;
            case "t" :
                respuesta.append("T");
                break;
            case "u" :
                respuesta.append("U");
                break;
            case "v" :
                respuesta.append("V");
                break;
            case "w" :
                respuesta.append("W");
                break;
            case "x" :
                respuesta.append("X");
                break;
            case "y" :
                respuesta.append("Y");
                break;
            case "z" :
                respuesta.append("Z");
                break;
        }
        campoRespuesta.setText(respuesta.toString());
    }
}
