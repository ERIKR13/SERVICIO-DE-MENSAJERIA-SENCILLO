/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.*;
import java.net.*;
import javax.swing.*;
/**
 *
 * @author FRANCISCO JAVIER ARELLANO GONZALEZ 
 * ICO 9I 
 * EXAMEN SEGUNDO PARCIAL "DETERMINAR SI ES UNA MATRIZ SIMETRICA O ASIMETRICA"
 */
public class Servidor extends JFrame {
  private JTextArea areaTexto;
    private JButton iniciarBtn, finalizarBtn, salirBtn;
    private ServerSocket serverSocket;
    private Socket socket;
    private boolean activo = false;

    public Servidor() {
        super("Servidor");
        setSize(400, 300);
        setLocationRelativeTo(null); // Centrar ventana
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        areaTexto = new JTextArea();
        JScrollPane scroll = new JScrollPane(areaTexto);

        iniciarBtn = new JButton("Iniciar conexión");
        finalizarBtn = new JButton("Finalizar conexión");
        salirBtn = new JButton("Salir");

        iniciarBtn.addActionListener(e -> iniciarServidor());
        finalizarBtn.addActionListener(e -> cerrarServidor());
        salirBtn.addActionListener(e -> {
            cerrarServidor();
            System.exit(0);
        });

        JPanel botones = new JPanel();
        botones.add(iniciarBtn);
        botones.add(finalizarBtn);
        botones.add(salirBtn);

        add(scroll, "Center");
        add(botones, "South");
        setVisible(true);
    }

    private void iniciarServidor() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8002);
                activo = true;
                areaTexto.append("Servidor iniciado en puerto 8002\n");

                while (activo) {
                    socket = serverSocket.accept();
                    areaTexto.append("Cliente conectado.\n");

                    try {
                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                        int tam = dis.readInt();
                        int[][] matriz = new int[tam][tam];

                        StringBuilder matrizStr = new StringBuilder();
                        for (int i = 0; i < tam; i++) {
                            for (int j = 0; j < tam; j++) {
                                matriz[i][j] = dis.readInt();
                                matrizStr.append(String.format("%5d", matriz[i][j]));
                            }
                            matrizStr.append("\n");
                        }

                        areaTexto.append("Matriz recibida:\n" + matrizStr.toString());

                        boolean simetrica = MatrizSimetrica.esSimetrica(matriz);
                        String resultado = simetrica ? "La matriz es simétrica." : "La matriz es asimétrica.";
                        areaTexto.append("Resultado: " + resultado + "\n");

                        dos.writeUTF(resultado);

                        // Esperar a que el cliente cierre su app
                        try {
                            dis.read();
                        } catch (IOException ex) {
                            areaTexto.append("Cliente desconectado.\n");
                        }

                        dis.close();
                        dos.close();
                        socket.close();

                    } catch (IOException e) {
                        areaTexto.append("Error durante la comunicación con el cliente.\n");
                    }
                }

            } catch (IOException ex) {
                areaTexto.append("Error en el servidor: " + ex.getMessage() + "\n");
            }
        }).start();
    }

    private void cerrarServidor() {
        try {
            activo = false;
            if (serverSocket != null) serverSocket.close();
            areaTexto.append("Servidor cerrado.\n");
        } catch (IOException e) {
            areaTexto.append("Error al cerrar el servidor.\n");
        }
    }

    public static void main(String[] args) {
        new Servidor();
    }
}


    

