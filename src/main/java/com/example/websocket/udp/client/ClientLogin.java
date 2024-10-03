package com.example.websocket.udp.client;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.ActionEvent;

public class ClientLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField email;
	private JTextField password;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientLogin frame = new ClientLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
//192.168.43.47
	private boolean checkUsernameExists(String username,String password) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress serverAddress = InetAddress.getByName("192.168.43.47");
		String message = "CHECK_USERNAME "+ username+"    " + password;
		byte[] sendData = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9875);
		clientSocket.send(sendPacket);
//         if(EmailServerUDP.checkUsernameExists(username, password)) {
//				ClientGUI clientGUI = new ClientGUI(email.getText());
//				clientGUI.setVisible(true);
//				dispose();
//			}else {
//				JOptionPane.showMessageDialog(null, "Sai tên đăng nhập hoặc mật khẩu.");
//			}
		// Nhận phản hồi từ server
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
		clientSocket.close();


		return response.equals("EXISTS");
	}

	/**
	 * Create the frame.
	 */
	public ClientLogin() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 469, 285);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("LOGIN");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblNewLabel.setBounds(190, 3, 82, 38);
		contentPane.add(lblNewLabel);

		email = new JTextField();
		email.setBounds(132, 51, 323, 50);
		contentPane.add(email);
		email.setColumns(10);

		password = new JTextField();
		password.setColumns(10);
		password.setBounds(132, 111, 323, 50);
		contentPane.add(password);

		JLabel lblNewLabel_1 = new JLabel("Email:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(10, 56, 104, 45);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Password:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1_1.setBounds(10, 111, 104, 45);
		contentPane.add(lblNewLabel_1_1);

		JButton btnNewButton = new JButton("Login");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					//checkUsernameExists( email.getText(), password.getText());
					if(checkUsernameExists(email.getText(), password.getText())) {
						ClientGUI clientGUI = new ClientGUI(email.getText());
						clientGUI.setVisible(true);
					}else {
						System.out.println("loi");
					}

				} catch (Exception e2) {
					// TODO: handle exception
				}

			}



		});
		btnNewButton.setBounds(26, 179, 295, 59);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Register");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientRegister clientRegister = new ClientRegister();
				clientRegister.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setBounds(331, 181, 114, 57);
		contentPane.add(btnNewButton_1);
	}
}