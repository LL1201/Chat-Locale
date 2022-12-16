package ChatLocale;

import java.io.*;
import java.net.*;

public class ChatThread implements Runnable {
    private Socket s = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String str = null;
    public static boolean clientClose = true;

    public ChatThread(Socket cmd) {
        this.s = cmd;
    }

    public ChatThread(Socket cmd, String ip) {
        this.s = cmd;
    }

    public void run() {
        // timeout per lo spegimento del server e la disconnessione dei client
        try {
            s.setSoTimeout(4500);
        } catch (SocketException e1) {
            e1.printStackTrace();
        }

        // risposta iniziale del server e assegnazione dei vari stream e buffer
        try {
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            out.println("Loner Chat Locale" + "\n");
            GUI.txtChat.append("Connected host: " + s.getInetAddress() + s.getPort() + "\n");
            System.out.println("Connected host: " + s.getInetAddress() + s.getPort());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Unable to get I/O stream on socket!");
        }

        // finché clientclose rimane true (comando di server acceso) il ciclo continua
        while (clientClose) {
            try {
                str = in.readLine();
            } catch (Exception e) {
                continue;
            }

            if (!str.equals(null)) {
                System.out.println(str + "\n");
                GUI.txtChat.append(str + "\n");
                for (ChatThread client : Server.client) {
                    client.StampaMessaggio(str);
                }
            }
        }
        System.out.println("Client is down");
    }

    public void StampaMessaggio(String msg) {
        // GUI.txtChat.append(msg);
        out.println(msg);
    }
}
