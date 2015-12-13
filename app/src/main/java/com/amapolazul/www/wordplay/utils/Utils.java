package com.amapolazul.www.wordplay.utils;

import java.util.Random;
import java.util.TreeSet;

/**
 * Created by jsmartinez on 06/12/2015.
 */
public class Utils {

    private static Random random;

    public static int darNumeroAleatorioAbecedario(){
        random = new Random();
        int randomNum = random.nextInt((27));
        return randomNum;
    }

    public static TreeSet<String> darPosicionesLetras(String palabras){
        random = new Random();
        TreeSet<String> arbolLetras = new TreeSet<String>();
        boolean flag;
        for(int i = 0; i < palabras.length(); i++){
            flag = true;
            while(flag){
                int posX = random.nextInt((3));
                int posY = random.nextInt((5));
                String posiciones = posX + "," + posY;
                if(!arbolLetras.contains(posiciones)){
                    arbolLetras.add(posiciones);
                    flag = false;
                }
            }
        }
        return arbolLetras;
    }
}
