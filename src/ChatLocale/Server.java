package ChatLocale;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.io.IOException;

public class Server implements Runnable {

	private static ServerSocket ws = null;
	public static ExecutorService pool;
	public static List<String> messages = new ArrayList<String>();
	public static boolean serverClose = true;
	public static List<ChatThread> client = new ArrayList<ChatThread>();
	String ip;

	public Server() {

	}

	public void run() {

		SocketAddress socketAddress = new InetSocketAddress(1212);
		try {
			ws = new ServerSocket();
			ws.bind(socketAddress);
			ws.setSoTimeout(2000);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Welcoming socket error!");
		}

		// in ascolto per creare nuovi thread
		while (serverClose) {
			Socket cmd = null;
			ChatThread c;
			try {
				cmd = ws.accept();
				c = new ChatThread(cmd);
				client.add(c);
				pool.execute(c);
				System.out.println("Pool OK");
			} catch (Exception e) {
				continue;
			}
		}
		System.out.println("Server is down");
	}
}