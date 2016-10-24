/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cauchy.application;

import java.util.ArrayList;

/**
 *
 * @author miguel
 */
public class registro_entradas {
    private static ArrayList<int[]> entradas = new ArrayList<int[]>();
    
    public static void init(){
        entradas = new ArrayList<int[]>();
    }
    public static int[][] obtener_patrones(){
        int[][] lista = new int[entradas.size()][entradas.get(0).length];
        int pos = 0;
        for(int[] patron : entradas){
            for(int i = 0; i < patron.length; i++){
                lista[pos][i] = patron[i];
            }
            pos++;
        }
        
        return lista;
    }
    
    public static void agregar_patron(int[] patron){
        entradas.add(patron);
    }
    
}
