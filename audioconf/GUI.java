package audioconf;

import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.JToggleButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class GUI {

	private JFrame frmAudioConference;
	
	private JTextField txtAddclient;
	
	private DefaultListModel listModelClients = new DefaultListModel<>();
	private JList listClients;
	
	private JToggleButton tglbtnConnect;
	private JToggleButton tglbtnMuteMicrophone;
	private JToggleButton tglbtnMuteSpeakers;
	
	private Client client;
	private JLabel lblLocalPort;
	private JTextField txtPort;
	private JCheckBox chckbxMulticast;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmAudioConference.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAudioConference = new JFrame();
		frmAudioConference.setTitle("Audio Conference");
		frmAudioConference.setBounds(100, 100, 601, 339);
		frmAudioConference.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		txtAddclient = new JTextField();
		txtAddclient.setText("localhost:5001");
		txtAddclient.setColumns(10);

		listClients = new JList(listModelClients);
		
		JButton btnAddClient = new JButton("Add Client");
		btnAddClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				listModelClients.addElement(txtAddclient.getText());
				if (activeConnection()) {
					setClientsFromList();
					client.restartSendingPipe();
				}
			}
		});
		
		JScrollPane scrollPane = new JScrollPane();
		
		tglbtnMuteMicrophone = new JToggleButton("Mute Mic");
		tglbtnMuteMicrophone.setEnabled(false);
		tglbtnMuteMicrophone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = tglbtnMuteMicrophone.getModel().isSelected();
				if (activeConnection()) {
					if (selected) {
						tglbtnMuteMicrophone.setText("Unmute Mic");
						client.stopSendingPipe();
					} else {
						tglbtnMuteMicrophone.setText("Mute Mic");
						client.startSendingPipe();
					}
				}
			}
		});
		
		tglbtnMuteSpeakers = new JToggleButton("Mute Speakers");
		tglbtnMuteSpeakers.setEnabled(false);
		tglbtnMuteSpeakers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boolean selected = tglbtnMuteSpeakers.getModel().isSelected();
				if (activeConnection()) {
					if (selected) {
						tglbtnMuteSpeakers.setText("Unmute Speakers");
						client.stopListeningPipe();
					} else {
						tglbtnMuteSpeakers.setText("Mute Speakers");
						client.startListeningPipe();
					}
				}
			}
		});
		
		tglbtnConnect = new JToggleButton("Connect");
		tglbtnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				boolean selected = tglbtnConnect.getModel().isSelected();
				if (selected) {
					tglbtnConnect.setText("Disconnect");
					client = new Client();
					System.out.println(listModelClients.toString());
					setClientsFromList();
					client.setPort(Integer.parseInt(txtPort.getText()));
					client.setMulticast(chckbxMulticast.isSelected());
					client.start();
					tglbtnMuteSpeakers.setEnabled(true);
					tglbtnMuteMicrophone.setEnabled(true);
				} else {
					tglbtnConnect.setText("Connect");
					client.stop();
					tglbtnMuteSpeakers.setEnabled(false);
					tglbtnMuteMicrophone.setEnabled(false);
				}
			}
		});
		
		JLabel lblOptions = new JLabel("Options");
		
		JLabel lblConnection = new JLabel("Connection");
		
		JButton btnRemoveClient = new JButton("Remove Client");
		btnRemoveClient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (listClients.getSelectedValue() != null) {
					listModelClients.removeElement(listClients.getSelectedValue());
					if (activeConnection()) {
						setClientsFromList();
						client.restartSendingPipe();
					}
				}
			}
		});
		
		lblLocalPort = new JLabel("Local Port");
		
		txtPort = new JTextField();
		txtPort.setText("5001");
		txtPort.setColumns(10);
		
		JButton btnSetPort = new JButton("Set Port");
		btnSetPort.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (activeConnection()) {
					client.setPort(Integer.parseInt(txtPort.getText()));
					client.restartListeningPipe();
				}
			}
		});
		
		chckbxMulticast = new JCheckBox("Multicast");
		GroupLayout groupLayout = new GroupLayout(frmAudioConference.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnRemoveClient, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 272, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(txtAddclient, GroupLayout.PREFERRED_SIZE, 154, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(btnAddClient)))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tglbtnMuteSpeakers, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tglbtnMuteMicrophone, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tglbtnConnect, GroupLayout.DEFAULT_SIZE, 291, Short.MAX_VALUE))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addGap(31)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(chckbxMulticast)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(lblLocalPort)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(btnSetPort)))
							.addGap(13))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblOptions))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblConnection)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(txtAddclient, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnAddClient)
						.addComponent(lblLocalPort)
						.addComponent(txtPort, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnSetPort))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chckbxMulticast)
							.addPreferredGap(ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
							.addComponent(lblConnection)
							.addGap(10)
							.addComponent(tglbtnConnect)
							.addGap(24)
							.addComponent(lblOptions)
							.addGap(12)
							.addComponent(tglbtnMuteMicrophone)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(tglbtnMuteSpeakers)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnRemoveClient)
					.addGap(17))
		);
		
		scrollPane.setViewportView(listClients);
		frmAudioConference.getContentPane().setLayout(groupLayout);
	}
	
	private boolean activeConnection() {
		return tglbtnConnect.getModel().isSelected();
	}
	
	private void setClientsFromList() {
		String clients = listModelClients.toString();
		client.setClients(clients.substring(1, clients.length()-1).replaceAll("\\s",""));
	}
}
