// Loner Luca
// 5B IA
// 27/12/2021
package FTP;

import java.net.*;
import java.util.concurrent.ExecutorService;
import java.io.IOException;

public class Server implements Runnable {

	public static boolean serverClose = true;
	private static ServerSocket wscmd = null;
	public static ExecutorService pool;
	public static int epsvPort = 1024;
	public static String ftpPath = "";

	public void run() {

		// Three way handshake
		try {
			wscmd = new ServerSocket(21);
			wscmd.setSoTimeout(2000);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Welcoming socket error!");
		}

		// in ascolto per creare nuovi thread
		while (serverClose) {
			Socket cmd = null;
			try {
				cmd = wscmd.accept();
				pool.execute(new FTPThread(cmd));
				System.out.println("Pool OK");
			} catch (Exception e) {
				continue;
			}
		}
		System.out.println("Server is down");
	}

	public static void Shutdown() {
		FTPThread.clientClose = false;
		pool.shutdownNow();
		serverClose = false;
		FTPThread.clientClose = false;
		try {
			wscmd.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}