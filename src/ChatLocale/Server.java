package ChatLocale;

import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;

import java.io.IOException;

public class Server implements Runnable {

	private static ServerSocket ws = null;
	ServerSocketFactory factory = SSLServerSocketFactory.getDefault();
	public static ExecutorService pool;
	public static List<String> messages = new ArrayList<String>();
	public static boolean serverClose = true;
	public static List<User> client = new ArrayList<User>();
	String ip;

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

		/*
		 * try {
		 * SSLServerSocket listener = (SSLServerSocket)
		 * factory.createServerSocket(1212);
		 * listener.setNeedClientAuth(true);
		 * listener.setEnabledCipherSuites(new String[] { "TLS_AES_128_GCM_SHA256" });
		 * listener.setEnabledProtocols(new String[] { "TLSv1.3" });
		 * System.out.println("listening for messages...");
		 * 
		 * while (serverClose) {
		 * Socket cmd = null;
		 * ChatThread c;
		 * try {
		 * cmd = listener.accept();
		 * c = new ChatThread(cmd);
		 * client.add(c);
		 * pool.execute(c);
		 * System.out.println("Pool OK");
		 * } catch (Exception e) {
		 * continue;
		 * }
		 * }
		 * 
		 * } catch (Exception e) {
		 * e.printStackTrace();
		 * }
		 */

		// in ascolto per creare nuovi thread

		while (serverClose) {
			Socket cmd = null;
			ChatThread c;
			try {
				cmd = ws.accept();
				c = new ChatThread(cmd);
				client.add(new User("", c));
				pool.execute(c);
			} catch (Exception e) {
				continue;
			}
		}

		System.out.println("Server is down");
	}

	public static void Shutdown() {
		ChatClient.Disconnetti();
		pool.shutdownNow();
		serverClose = false;
		try {
			ws.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}