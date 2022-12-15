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
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class GUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtNomeChat;
	private JTextField txtMessaggio;
	JButton btnDisconnetti = new JButton("Disconnettiti");

	public ChatClient chatClient;
	private JTextField txtIpServer;
	public static JTextArea txtChat;

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
		setTitle("Loner Chat Locale");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 594, 564);
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

		txtMessaggio = new JTextField();
		txtMessaggio.setEnabled(false);
		txtMessaggio.setBounds(10, 478, 431, 36);
		contentPane.add(txtMessaggio);
		txtMessaggio.setColumns(10);

		JButton btnInvia = new JButton("INVIA");
		btnInvia.setEnabled(false);
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chatClient.Invia(txtMessaggio.getText());
				txtMessaggio.setText("");
			}
		});
		btnInvia.setBounds(451, 478, 117, 36);
		contentPane.add(btnInvia);

		JButton btnConnetti = new JButton("Connettiti");
		btnConnetti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InetAddressValidator validator = InetAddressValidator.getInstance();
				if (validator.isValidInet4Address(txtIpServer.getText())) {
					if (!txtNomeChat.getText().equals("")) {
						try {
							Socket socket = new Socket(txtIpServer.getText(), 1212);
							chatClient = new ChatClient(socket, txtNomeChat.getText());
							// Server.client.add(chatClient);
							new Thread(chatClient).start();
							System.out.println("Pool OK");
							btnConnetti.setEnabled(false);
							btnDisconnetti.setEnabled(true);
							txtMessaggio.setEnabled(true);
							btnInvia.setEnabled(true);
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

		JButton btnAvviaServer = new JButton("Avvia il processo server");
		btnAvviaServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.serverClose = true;
				Server.pool = Executors.newFixedThreadPool(50);
				new Thread(new Server()).start();
				txtNomeChat.setEnabled(true);
				btnAvviaServer.setEnabled(false);
				btnConnetti.setEnabled(true);
				txtChat.append("Processo server avviato. In attesa di connessione");
			}
		});
		btnAvviaServer.setBounds(10, 52, 558, 46);
		contentPane.add(btnAvviaServer);

		btnDisconnetti.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ChatClient.Disconnetti();
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
		txtChat.setBounds(10, 176, 558, 291);
		contentPane.add(txtChat);

		JScrollPane scrollPane = new JScrollPane(txtChat, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setSize(558, 291);
		scrollPane.setLocation(10, 176);
		contentPane.add(scrollPane);
	}
}