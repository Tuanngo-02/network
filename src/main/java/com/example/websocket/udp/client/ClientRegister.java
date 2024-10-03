package com.example.websocket.udp.client;

import com.example.websocket.udp.server.EmailServerUDP;

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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.awt.event.ActionEvent;

public class ClientRegister extends JFrame {

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
					ClientRegister frame = new ClientRegister();
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

	private void sendRegistrationRequest(String username,String password) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress serverAddress = InetAddress.getByName("192.168.43.47");
		String message = "CREATE_ACCOUNT " + username+"     "+password;
//		System.out.println(message);
		byte[] sendData = message.getBytes();
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9875);
		clientSocket.send(sendPacket);
		clientSocket.close();
	}
	 
	public ClientRegister() {
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 469, 254);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("REGISTER");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(175, 0, 87, 29);
		contentPane.add(lblNewLabel);
		
		email = new JTextField();
		email.setBounds(104, 39, 330, 37);
		contentPane.add(email);
		email.setColumns(10);
		
		password = new JTextField();
		password.setColumns(10);
		password.setBounds(104, 91, 330, 37);
		contentPane.add(password);
		
		JLabel lblNewLabel_1 = new JLabel("Email:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1.setBounds(1, 36, 93, 38);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_1_1 = new JLabel("Password:");
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel_1_1.setBounds(1, 91, 93, 38);
		contentPane.add(lblNewLabel_1_1);
		
		JButton btnNewButton = new JButton("Register");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (EmailServerUDP.checkUsernameRegister(email.getText())) {
                        JOptionPane.showMessageDialog(null, "Tên người dùng đã tồn tại. Vui lòng chọn tên khác.");
                    } else {
					sendRegistrationRequest(email.getText(),password.getText());
					JOptionPane.showMessageDialog(null, "Đăng ký thành công. Vui lòng đăng nhập.");
					ClientLogin clientLogin = new ClientLogin();
					clientLogin.setVisible(true);
					dispose();
                    }
				} catch (Exception e2) {
					// TODO: handle exception
				}
				
				}
		});
		btnNewButton.setBounds(32, 149, 265, 47);
		contentPane.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Login");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ClientLogin clientLogin = new ClientLogin();
				clientLogin.setVisible(true);
				dispose();
			}
		});
		btnNewButton_1.setBounds(307, 149, 138, 47);
		contentPane.add(btnNewButton_1);
	}

}
