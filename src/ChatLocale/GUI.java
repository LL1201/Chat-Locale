package ChatLocale;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.apache.commons.validator.routines.InetAddressValidator;

import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowEvent;
import javax.swing.ScrollPaneConstants;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtNomeChat;
	JButton btnDisconnetti = new JButton("Disconnettiti");
	JButton btnInvia = new JButton("INVIA");
	JButton btnAvviaServer = new JButton("Avvia il processo server");

	public ChatClient chatClient;
	private JTextField txtIpServer;
	public static JTextArea txtChat;
	public static JTextArea txtMessaggio;
	public static ExecutorService poolClient = Executors.newFixedThreadPool(10);

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GUI() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (!btnAvviaServer.isEnabled())
					Server.Shutdown();
				else if (btnDisconnetti.isEnabled())
					JOptionPane.showMessageDialog(new JFrame(), "Disconnettiti dal server!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
			}
		});
		addWindowFocusListener(new WindowFocusListener() {
			public void windowGainedFocus(WindowEvent e) {
				Notifica.windowFocus = false;
			}

			public void windowLostFocus(WindowEvent e) {
				Notifica.windowFocus = true;
			}
		});
		setResizable(false);
		setTitle("Loner Chat Locale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 594, 585);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		txtNomeChat = new JTextField();
		txtNomeChat.setBounds(167, 145, 148, 20);
		contentPane.add(txtNomeChat);
		txtNomeChat.setColumns(10);

		JLabel lblNewLabel = new JLabel("Nome con cui comparirai:");
		lblNewLabel.setBounds(10, 148, 171, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("LONER CHAT LOCALE");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel_1.setBounds(190, 11, 208, 20);
		contentPane.add(lblNewLabel_1);

		btnInvia.setEnabled(false);
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InviaMessaggio(txtMessaggio.getText());
				txtMessaggio.setText("");
			}
		});
		btnInvia.setBounds(451, 478, 117, 60);
		contentPane.add(btnInvia);

		JButton btnConnetti = new JButton("Connettiti");
		btnConnetti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InetAddressValidator validator = InetAddressValidator.getInstance();
				if (validator.isValidInet4Address(txtIpServer.getText())) {
					if (!txtNomeChat.getText().equals("")) {
						try {
							/*
							 * SocketFactory factory = SSLSocketFactory.getDefault();
							 * try {
							 * SSLSocket socket = (SSLSocket) factory.createSocket(txtIpServer.getText(),
							 * 1212);
							 * socket.setEnabledCipherSuites(new String[] { "TLS_AES_128_GCM_SHA256" });
							 * socket.setEnabledProtocols(new String[] { "TLSv1.3" });
							 * chatClient = new ChatClient(socket, txtNomeChat.getText());
							 * poolClient.execute(chatClient);
							 * } catch (Exception ex) {
							 * ex.printStackTrace();
							 * }
							 */
							Socket socket = new Socket(txtIpServer.getText(), 1212);
							chatClient = new ChatClient(socket, txtNomeChat.getText());
							new Thread(chatClient).start();
							System.out.println("Pool OK");
							btnConnetti.setEnabled(false);
							btnDisconnetti.setEnabled(true);
							txtMessaggio.setEnabled(true);
							txtIpServer.setEnabled(false);
							txtNomeChat.setEnabled(false);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(new JFrame(), "Server non disponibile", "Inane warning",
									JOptionPane.WARNING_MESSAGE);
						}
					} else {
						JOptionPane.showMessageDialog(new JFrame(), "Inserisci un nome valido!", "Inane warning",
								JOptionPane.WARNING_MESSAGE);
					}

				} else
					JOptionPane.showMessageDialog(new JFrame(), "Indirizzo IP non valido!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
			}
		});
		btnConnetti.setBounds(325, 111, 124, 54);
		contentPane.add(btnConnetti);

		btnAvviaServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					UserManagement um = new UserManagement();
					um.setVisible(true);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				Server.serverClose = true;
				Server.pool = Executors.newFixedThreadPool(50);
				new Thread(new Server()).start();
				txtNomeChat.setEnabled(true);
				btnAvviaServer.setEnabled(false);
				btnConnetti.setEnabled(true);
				txtChat.append("Processo server avviato. In attesa di connessione");
				txtIpServer.setEnabled(false);
				txtNomeChat.setEnabled(false);
				btnConnetti.setEnabled(false);
			}
		});
		btnAvviaServer.setBounds(10, 52, 558, 46);
		contentPane.add(btnAvviaServer);

		btnDisconnetti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatClient.Disconnetti();
				btnConnetti.setEnabled(true);
				txtIpServer.setEnabled(true);
				txtNomeChat.setEnabled(true);
				btnDisconnetti.setEnabled(false);
			}
		});

		btnDisconnetti.setEnabled(false);
		btnDisconnetti.setBounds(459, 111, 109, 54);
		contentPane.add(btnDisconnetti);

		JLabel lblNewLabel_2 = new JLabel("Indirizzo IP server:");
		lblNewLabel_2.setBounds(10, 117, 134, 14);
		contentPane.add(lblNewLabel_2);

		txtIpServer = new JTextField();
		txtIpServer.setColumns(10);
		txtIpServer.setBounds(167, 114, 148, 20);
		contentPane.add(txtIpServer);

		txtChat = new JTextArea();
		txtChat.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtChat.setBounds(10, 176, 558, 291);
		txtChat.setEditable(false);
		contentPane.add(txtChat);

		txtMessaggio = new JTextArea();
		txtMessaggio.setFont(new Font("Monospaced", Font.PLAIN, 14));
		txtMessaggio.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (txtMessaggio.getText().equals("")) {
						JOptionPane.showMessageDialog(new JFrame(), "Impossibile inviare un messaggio vuoto",
								"Inane warning",
								JOptionPane.WARNING_MESSAGE);
					} else {
						InviaMessaggio(txtMessaggio.getText());
					}

				}

				if (txtMessaggio.getText().equals("")) {
					btnInvia.setEnabled(false);
				} else {
					btnInvia.setEnabled(true);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					txtMessaggio.setText("");
				}

				if (txtMessaggio.getText().equals("")) {
					btnInvia.setEnabled(false);
				} else {
					btnInvia.setEnabled(true);
				}
			}
		});
		txtMessaggio.setBounds(10, 176, 558, 291);
		contentPane.add(txtMessaggio);
		txtMessaggio.setWrapStyleWord(true);
		txtMessaggio.setLineWrap(true);

		JScrollPane scrollPane = new JScrollPane(txtChat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setSize(558, 291);
		scrollPane.setLocation(10, 176);
		contentPane.add(scrollPane);

		JScrollPane scrollPaneMsg = new JScrollPane(
				txtMessaggio, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPaneMsg.setSize(431, 60);
		scrollPaneMsg.setLocation(10, 478);
		contentPane.add(scrollPaneMsg);
	}

	private void InviaMessaggio(String msg) {
		chatClient.Invia(msg);
		btnInvia.setEnabled(false);
	}

}
