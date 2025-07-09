/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

/**
 *
 * @author FRANCISCO JAVIER ARELLANO GONZALEZ 
 * ICO 9I 
 * EXAMEN SEGUNDO PARCIAL "DETERMINAR SI ES UNA MATRIZ SIMETRICA O ASIMETRICA"
 */
public class MatrizSimetrica {
     public static boolean esSimetrica(int[][] matriz) {
        int n = matriz.length;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (matriz[i][j] != matriz[j][i]) {
                    return false;
                }
            }
        }
        return true;
    }
}
