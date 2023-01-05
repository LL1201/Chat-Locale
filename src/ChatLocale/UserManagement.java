package ChatLocale;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.DefaultListModel;
import javax.swing.JButton;

public class UserManagement extends JFrame {

	private JPanel contentPane;
	public static JList lstUtentiManagement = new JList();
	DefaultListModel<String> model = new DefaultListModel<String>();
	Timer timer = new Timer();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UserManagement frame = new UserManagement();
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
	public UserManagement() {
		setTitle("Gestione degli utenti");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 158, 214);
		contentPane.add(scrollPane);

		scrollPane.setViewportView(lstUtentiManagement);

		JLabel lblNewLabel = new JLabel("Lista utenti");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 11, 158, 26);
		contentPane.add(lblNewLabel);

		JButton btnEspelli = new JButton("Espelli");
		btnEspelli.setBounds(187, 34, 89, 23);
		contentPane.add(btnEspelli);

		JButton btnMuta = new JButton("Muta");
		btnMuta.setBounds(187, 72, 89, 23);
		contentPane.add(btnMuta);

		lstUtentiManagement.setModel(model);

		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				model.clear();
				for (User user : Server.client) {
					model.addElement(user.name);
				}
			}
		}, 1000, 1000);
	}
}
