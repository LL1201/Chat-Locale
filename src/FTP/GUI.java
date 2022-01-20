package FTP;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.border.MatteBorder;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.util.concurrent.TimeUnit;

public class GUI extends JFrame {
	private JPanel contentPane;
	private JTextField txtPercorso;
	public static JTextArea textAreaLog;

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
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 768, 469);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 752, 22);
		contentPane.add(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmRiavvia = new JMenuItem("Riavvia");
		mnNewMenu.add(mntmRiavvia);

		JMenuItem mntmAvvia = new JMenuItem("Avvia");
		mnNewMenu.add(mntmAvvia);

		JMenuItem mntmArresta = new JMenuItem("Arresta");
		mnNewMenu.add(mntmArresta);

		JMenuItem menuItem = new JMenuItem("New menu item");
		mnNewMenu.add(menuItem);

		JLabel lblSelezionaIFile = new JLabel("Seleziona la cartella di radice da condividere via FTP:");
		lblSelezionaIFile.setBounds(10, 33, 304, 14);
		contentPane.add(lblSelezionaIFile);

		JLabel lblControlloDelServer = new JLabel("Controllo del server FTP");
		lblControlloDelServer.setBounds(553, 33, 145, 14);
		contentPane.add(lblControlloDelServer);

		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBorder(new MatteBorder(1, 1, 1, 1, (Color) new Color(0, 0, 0)));
		panel_1.setBackground(UIManager.getColor("Button.background"));
		panel_1.setBounds(563, 58, 119, 155);
		contentPane.add(panel_1);

		JButton btnAvvia = new JButton("Avvia");
		panel_1.add(btnAvvia);

		JButton btnRiavvia = new JButton("Riavvia");

		panel_1.add(btnRiavvia);

		JLabel lblLogDelServer = new JLabel("Log del server FTP:");
		lblLogDelServer.setBounds(10, 247, 109, 14);
		contentPane.add(lblLogDelServer);

		JPanel panel = new JPanel();
		panel.setBounds(20, 58, 446, 155);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPercorso = new JLabel("Percorso:");
		lblPercorso.setBounds(10, 21, 66, 14);
		panel.add(lblPercorso);

		txtPercorso = new JTextField();
		txtPercorso.setBounds(72, 18, 261, 20);
		panel.add(txtPercorso);
		txtPercorso.setColumns(10);

		JButton btnAggiungi = new JButton("Seleziona");
		btnAggiungi.setBounds(345, 17, 89, 23);
		panel.add(btnAggiungi);

		JButton btnArresta = new JButton("Arresta");
		btnArresta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtPercorso.setEditable(true);
				btnAggiungi.setEnabled(true);
				Server.Shutdown();
			}
		});
		panel_1.add(btnArresta);

		JLabel lblLaCartellaDa = new JLabel(
				"<html>La cartella da selezionare sar\u00E0 quella che conterr\u00E0 tutti i file e le directory che il server FTP condivider\u00E0. Tale cartella sar\u00E0 modificabile e leggibile da tutti gli utenti che ne abbiano il permesso. Si consiglia di selezionare un percorso destinato solo all'uso e al corretto funzionamento del server FTP.</html>");
		lblLaCartellaDa.setBounds(10, 61, 424, 83);
		panel.add(lblLaCartellaDa);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 274, 730, 143);
		contentPane.add(scrollPane);

		textAreaLog = new JTextArea();
		scrollPane.setViewportView(textAreaLog);
		btnAggiungi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Seleziona una cartella");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.showOpenDialog(null);

				txtPercorso.setText(chooser.getSelectedFile().toString());
				Server.ftpPath = txtPercorso.getText();
			}
		});
		btnAvvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.serverClose = true;
				FTPThread.clientClose = true;
				Server.pool = Executors.newFixedThreadPool(50);
				new Thread(new Server()).start();
				txtPercorso.setEditable(false);
				btnAggiungi.setEnabled(false);
				textAreaLog.append(new SimpleDateFormat("hh:mm").format(new Date()) + " " + "Server FTP avviato\n");
			}
		});

		btnRiavvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Server.Shutdown();

				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				Server.serverClose = true;
				FTPThread.clientClose = true;
				Server.pool = Executors.newFixedThreadPool(50);
				new Thread(new Server()).start();
				txtPercorso.setEditable(false);
				btnAggiungi.setEnabled(false);
				textAreaLog.append(new SimpleDateFormat("hh:mm").format(new Date()) + " " + "Server FTP riavviato\n");
			}
		});
	}
}
