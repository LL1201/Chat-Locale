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
import javax.swing.JOptionPane;
import javax.swing.JMenu;
import javax.swing.JLabel;
import java.awt.Color;
import javax.swing.UIManager;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JScrollPane;
import java.util.concurrent.TimeUnit;
import javax.swing.border.LineBorder;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GUI extends JFrame {
	private JPanel contentPane;
	private JTextField txtPercorso;
	public static JTextArea textAreaLog;
	private JTextField txtNomeUtente;
	DefaultListModel model = new DefaultListModel();
	JList listBoxUtenti = new JList();
	private JTextField txtPassword;

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
		setTitle("Loner FTP Server");
		listBoxUtenti.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txtNomeUtente.setText(Server.lstUtenti.get(listBoxUtenti.getSelectedIndex()).name);
				txtPassword.setText(Server.lstUtenti.get(listBoxUtenti.getSelectedIndex()).password);
			}
		});
		listBoxUtenti.setModel(model);
		ListUpdate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 912, 525);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 896, 22);
		contentPane.add(menuBar);

		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);

		JMenuItem mntmRiavvia = new JMenuItem("Riavvia");
		mnNewMenu.add(mntmRiavvia);

		JMenuItem mntmAvvia = new JMenuItem("Avvia");
		mnNewMenu.add(mntmAvvia);

		JMenuItem mntmArresta = new JMenuItem("Arresta");
		mnNewMenu.add(mntmArresta);

		JMenuItem mntmEsci = new JMenuItem("Esci");
		mntmEsci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		mnNewMenu.add(mntmEsci);

		JLabel lblSelezionaIFile = new JLabel("Seleziona la cartella di radice da condividere via FTP:");
		lblSelezionaIFile.setBounds(10, 33, 304, 14);
		contentPane.add(lblSelezionaIFile);

		JLabel lblControlloDelServer = new JLabel("Controllo del server FTP:");
		lblControlloDelServer.setBounds(739, 100, 145, 14);
		contentPane.add(lblControlloDelServer);

		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.WHITE);
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBackground(UIManager.getColor("Button.background"));
		panel_1.setBounds(739, 125, 119, 106);
		contentPane.add(panel_1);

		JButton btnAvvia = new JButton("Avvia");
		panel_1.add(btnAvvia);

		JButton btnRiavvia = new JButton("Riavvia");

		panel_1.add(btnRiavvia);

		JLabel lblLogDelServer = new JLabel("Log del server FTP:");
		lblLogDelServer.setBounds(10, 287, 109, 14);
		contentPane.add(lblLogDelServer);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(20, 59, 419, 204);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblPercorso = new JLabel("Percorso:");
		lblPercorso.setBounds(10, 21, 66, 14);
		panel.add(lblPercorso);

		txtPercorso = new JTextField();
		txtPercorso.setBounds(72, 18, 235, 20);
		panel.add(txtPercorso);
		txtPercorso.setColumns(10);

		JButton btnAggiungi = new JButton("Seleziona");
		btnAggiungi.setBounds(319, 17, 89, 23);
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
		lblLaCartellaDa.setBounds(10, 61, 398, 125);
		panel.add(lblLaCartellaDa);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 313, 848, 161);
		contentPane.add(scrollPane);

		textAreaLog = new JTextArea();
		scrollPane.setViewportView(textAreaLog);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_2.setBounds(451, 59, 270, 204);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JButton btnAggiungiUtente = new JButton("+");
		btnAggiungiUtente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtNomeUtente.getText().equals(""))
					JOptionPane.showMessageDialog(new JFrame(), "Inserisci un username!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				else if (txtPassword.getText().equals(""))
					JOptionPane.showMessageDialog(new JFrame(), "Inserisci una password!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				else if (DoubleUser(txtNomeUtente.getText())) {
					JOptionPane.showMessageDialog(new JFrame(), "Username duplicato!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				} else {
					Server.lstUtenti.add(new Utente(txtNomeUtente.getText(), txtPassword.getText()));
					ListUpdate();
					txtNomeUtente.setText("");
					txtPassword.setText("");
				}
			}
		});
		btnAggiungiUtente.setBounds(12, 165, 52, 26);
		panel_2.add(btnAggiungiUtente);

		JButton btnRimuoviUtente = new JButton("-");
		btnRimuoviUtente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Server.lstUtenti.remove(listBoxUtenti.getSelectedIndex());
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Seleziona un utente!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				}
				ListUpdate();
			}
		});
		btnRimuoviUtente.setBounds(206, 165, 52, 26);
		panel_2.add(btnRimuoviUtente);

		txtNomeUtente = new JTextField();
		txtNomeUtente.setBounds(12, 133, 114, 20);
		panel_2.add(txtNomeUtente);
		txtNomeUtente.setColumns(10);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 12, 246, 82);
		panel_2.add(scrollPane_1);

		scrollPane_1.setViewportView(listBoxUtenti);

		txtPassword = new JTextField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(144, 133, 114, 20);
		panel_2.add(txtPassword);

		JButton btnModifica = new JButton("Modifica");
		btnModifica.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Server.lstUtenti.get(listBoxUtenti.getSelectedIndex()).name = txtNomeUtente.getText();
					Server.lstUtenti.get(listBoxUtenti.getSelectedIndex()).password = txtPassword.getText();
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(new JFrame(), "Seleziona un utente!", "Inane warning",
							JOptionPane.WARNING_MESSAGE);
				}
				ListUpdate();
			}
		});
		btnModifica.setBounds(76, 165, 118, 26);
		panel_2.add(btnModifica);
		
		JLabel lblNomeUtente = new JLabel("Nome utente:");
		lblNomeUtente.setBounds(12, 107, 114, 14);
		panel_2.add(lblNomeUtente);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setBounds(144, 106, 66, 14);
		panel_2.add(lblPassword);

		JLabel lblGestioneDegliUtenti = new JLabel("Gestione degli utenti:");
		lblGestioneDegliUtenti.setBounds(451, 33, 145, 14);
		contentPane.add(lblGestioneDegliUtenti);

		btnAggiungi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogTitle("Seleziona una cartella");
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.showOpenDialog(null);

				try {
					txtPercorso.setText(chooser.getSelectedFile().toString());
					Server.ftpPath = txtPercorso.getText();
				} catch (Exception e1) {

				}
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

	private void ListUpdate() {
		model.clear();
		for (Utente item : Server.lstUtenti) {
			model.addElement(item.name);
		}
	}

	private boolean DoubleUser(String user) {
		for (Utente u : Server.lstUtenti) {
			if (u.name.equals(user)) {
				return true;
			} else
				return false;
		}
		return false;
	}
}
