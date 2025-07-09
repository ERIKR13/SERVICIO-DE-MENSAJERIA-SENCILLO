/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;

/**
 *
 * @author FRANCISCO JAVIER ARELLANO GONZALEZ ICO 9I EXAMEN SEGUNDO PARCIAL
 * "DETERMINAR SI ES UNA MATRIZ SIMETRICA O ASIMETRICA"
 */
public class Cliente extends JFrame {

    private JTextArea areaEntrada, areaResultado;
    private JButton enviarBtn, limpiarBtn, salirBtn;

    public Cliente() {
        super("Cliente");
        setSize(450, 420);
        setLocationRelativeTo(null); // Centrar ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel etiquetaEntrada = new JLabel("Matriz a ingresar:", SwingConstants.CENTER);
        JLabel etiquetaResultado = new JLabel("Resultado:", SwingConstants.CENTER);

        areaEntrada = new JTextArea(15, 15);
        areaResultado = new JTextArea(15, 15);
        areaResultado.setEditable(false);

      
        areaEntrada.setFont(new Font("Courier New", Font.PLAIN, 14));
        areaResultado.setFont(new Font("Courier New", Font.PLAIN, 14));

       
        JPanel panelCentro = new JPanel(new GridLayout(4, 1));
        panelCentro.add(etiquetaEntrada);
        panelCentro.add(new JScrollPane(areaEntrada));
        panelCentro.add(etiquetaResultado);
        panelCentro.add(new JScrollPane(areaResultado));


        enviarBtn = new JButton("Enviar");
        limpiarBtn = new JButton("Limpiar");
        salirBtn = new JButton("Salir");

        enviarBtn.addActionListener(e -> enviarDatos());
        limpiarBtn.addActionListener(e -> areaEntrada.setText(""));
        limpiarBtn.addActionListener(e -> areaResultado.setText(""));
        salirBtn.addActionListener(e -> {
            try {
                Socket socket = new Socket("localhost", 8002);
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
                dos.writeUTF("FIN"); // Señal de desconexión para el servidor
                dos.close();
                socket.close();
            } catch (IOException ex) {
                // Ignorar si no está conectado
            }
            System.exit(0);
        });

        
        JPanel panelBotones = new JPanel();
        panelBotones.add(enviarBtn);
        panelBotones.add(limpiarBtn);
        panelBotones.add(salirBtn);

        
        add(panelCentro, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void enviarDatos() {
        try (Socket socket = new Socket("localhost", 8002)) {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            String[] filas = areaEntrada.getText().split("\n");
            int n = filas.length;
            int[][] matriz = new int[n][n];

            for (int i = 0; i < n; i++) {
                String[] valores = filas[i].trim().split("\\s+");
                if (valores.length != n) {
                    areaResultado.setText("Error: La matriz debe ser cuadrada.");
                    return;
                }
                for (int j = 0; j < n; j++) {
                    matriz[i][j] = Integer.parseInt(valores[j]);
                }
            }

            dos.writeInt(n);
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    dos.writeInt(matriz[i][j]);
                }
            }

            String respuesta = dis.readUTF();

            
            StringBuilder matrizStr = new StringBuilder("Matriz enviada:\n");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    matrizStr.append(String.format("%5d", matriz[i][j]));
                }
                matrizStr.append("\n");
            }
            matrizStr.append("\n").append(respuesta);
            areaResultado.setText(matrizStr.toString());

        } catch (Exception ex) {
            areaResultado.setText("Error al conectar con el servidor: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new Cliente();
    }
}
