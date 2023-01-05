package ChatLocale;

import java.awt.AWTException;
import java.io.*;
import java.net.*;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ChatClient implements Runnable {
    private Socket s = null;
    private PrintWriter out = null;
    private BufferedReader in = null;
    public static boolean clientClose = true;
    private String nomeChat;
    private String str;

    public ChatClient(Socket s, String nomeChat) {
        this.s = s;
        this.nomeChat = nomeChat;
    }

    public void run() {
        // timeout per lo spegimento del server e la disconnessione dei client
        clientClose = true;
        try {
            s.setSoTimeout(1500);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        // risposta iniziale del server e assegnazione dei vari stream e buffer

        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            out.println(nomeChat + ": si è unito alla stanza");
            // GUI.txtChat.append("Connected host: " + s.getInetAddress() + s.getPort());
            // System.out.println("Connected host: " + s.getInetAddress() + s.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to get I/O stream on socket!");
        }

        // finché clientclose rimane true (comando di server acceso) il ciclo continua

        out.println("ctrlmsg" + nomeChat);
        while (clientClose) {
            try {
                str = in.readLine();
            } catch (Exception e) {
                continue;
            }

            if (!str.equals(null)) {
                GUI.txtChat.append(str + "\n");
                GUI.txtChat.setCaretPosition(GUI.txtChat.getDocument().getLength());
                Notifica td = new Notifica();
                try {
                    td.visualizzaNotifica(str + "\n");
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            }

        }
        out.println(nomeChat + ": si è disconnesso dal server \n");

        try {
            s.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GUI.txtChat.append("Ti sei disconnesso dal server \n");
    }

    public void Invia(String messaggio) {
        if (s.isClosed())
            JOptionPane.showMessageDialog(new JFrame(), "Server non disponibile", "Inane warning",
                    JOptionPane.WARNING_MESSAGE);
        else
            try {
                out.println(nomeChat + ": " + messaggio);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(new JFrame(), "Server non disponibile", "Inane warning",
                        JOptionPane.WARNING_MESSAGE);
            }
    }

    public static void Disconnetti() {
        ChatThread.clientClose = false;
        clientClose = false;
    }
}
