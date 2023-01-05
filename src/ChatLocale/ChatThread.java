package ChatLocale;

import java.io.*;
import java.net.*;

public class ChatThread implements Runnable {
    private Socket s = null;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private String str = null;
    public static boolean clientClose = true;
    private int lstId;
    private String name;

    public ChatThread(Socket cmd) {
        this.s = cmd;
    }

    public ChatThread(Socket cmd, int lstId) {
        this.s = cmd;
        this.lstId = lstId;
    }

    public void run() {
        // timeout per lo spegimento del server e la disconnessione dei client
        try {
            s.setSoTimeout(1500);
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

            if (str.substring(0, 6).equals("ctrlmsg")) {
                name = str.substring(7);
                Server.client.get(lstId).name = name;
            }
            if (!str.equals(null)) {
                System.out.println(str + "\n");
                GUI.txtChat.append(str + "\n");
                for (User client : Server.client) {
                    client.thread.StampaMessaggio(str);
                }
            }
        }
        System.out.println("Client is down");
        try {
            s.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void StampaMessaggio(String msg) {
        // GUI.txtChat.append(msg);
        out.println(msg);
    }
}
