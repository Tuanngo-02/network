package com.example.websocket.udp.client;

import java.awt.EventQueue;

import javax.swing.*;
import javax.swing.border.EmptyBorder;


import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;

public class ClientGUI extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField message;
	private JList<String> list;
	private DefaultListModel<String> model;
	private JTextField senTo;
	private JLabel lblNewLabel_2;
	private JTextField title;
	private JLabel lblNewLabel_3;
	private JButton btnNewButton_1;

	private JButton btnNewButton_test;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClientGUI frame = new ClientGUI(null);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static String displayEmail(String email) throws Exception {
		String response="";
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress serverAddress = InetAddress.getByName("192.168.43.47");
		String message = "DISPLAY_EMAIL " + email ;
		byte[] sendData = message.getBytes();
		try {
			// Gửi gói tin đến server
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9875);
			clientSocket.send(sendPacket);

//	            // Nhận phản hồi từ server
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			clientSocket.receive(receivePacket);
			response = new String(receivePacket.getData(), 0, receivePacket.getLength());

		} catch (Exception ex) {


		}
		return response.toString();
	}


	private void sendEmail(String email, String emailContent, String senTo, String title) throws Exception {
		DatagramSocket clientSocket = new DatagramSocket();
		InetAddress serverAddress = InetAddress.getByName("192.168.43.47");


		// Tạo gói tin gửi đến server
		String message = "SEND_EMAIL " + email + " " + emailContent+ " "+ senTo+" "+title;
		byte[] sendData = message.getBytes();

		try {

			// Gửi gói tin đến server
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, 9875);
			clientSocket.send(sendPacket);

//	            // Nhận phản hồi từ server
//	            byte[] receiveData = new byte[1024];
//	            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
//	            clientSocket.receive(receivePacket);
//	            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
//
//	            // Hiển thị phản hồi
//	            model.addElement("Gửi email thành công!: " + response);
		} catch (Exception ex) {

			model.addElement("Error: " + ex.getMessage());
			list.setModel(model);
		}
	}

	/**
	 * Create the frame.
	 */
	public ClientGUI(String email) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 565, 333);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("CLIENT: "+ email);
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNewLabel.setBounds(186, 0, 338, 27);
		contentPane.add(lblNewLabel);

		model = new DefaultListModel<>(); // Khởi tạo mô hình danh sách
		list = new JList<>(model);
		list.setBounds(10, 37, 531, 128);
		contentPane.add(list);

		JButton btnNewButton = new JButton("SEND");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if(message.getText().isEmpty() && senTo.getText().isEmpty() && title.getText().isEmpty()) {
						JOptionPane.showMessageDialog(null, "Vui lòng không để trống!");
					}else {
						sendEmail(email, message.getText(), senTo.getText(), title.getText());
						JOptionPane.showMessageDialog(null, "Gửi email thành công!");
						message.setText("");
						senTo.setText("");
						title.setText("");
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		btnNewButton.setBounds(374, 256, 151, 27);
		contentPane.add(btnNewButton);

		message = new JTextField();
		message.setBounds(102, 256, 262, 27);
		contentPane.add(message);
		message.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Nội dung:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_1.setBounds(10, 256, 82, 27);
		contentPane.add(lblNewLabel_1);

		senTo = new JTextField();
		senTo.setColumns(10);
		senTo.setBounds(102, 183, 262, 27);
		contentPane.add(senTo);

		lblNewLabel_2 = new JLabel("Gửi đến:");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_2.setBounds(10, 183, 82, 27);
		contentPane.add(lblNewLabel_2);

		title = new JTextField();
		title.setColumns(10);
		title.setBounds(102, 219, 262, 27);
		contentPane.add(title);

		lblNewLabel_3 = new JLabel("Tiêu đề:");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNewLabel_3.setBounds(10, 219, 82, 27);
		contentPane.add(lblNewLabel_3);

		btnNewButton_1 = new JButton("Hiển thị mail");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					displayEmail(email);
					// Chuỗi chứa danh sách email
					//String input = "from_haha_26-09-2024_00-41-39.txt, from_tuannnn_26-09-2024_00-33-38.txt, ty.txt";

					String input1 = displayEmail(email);
					// Cắt chuỗi thành mảng tên email
					String[] emails = input1.split(", ");

					// Làm sạch mô hình danh sách trước khi hiển thị
					model.clear();

					// Thêm từng email vào mô hình danh sách
					for (String email : emails) {
						model.addElement(email);
					}

					// Cập nhật JList với mô hình mới
					list.setModel(model);
					//click xem nội dung từ model
					list.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
							if (e.getClickCount() == 1) {  // Xử lý khi nhấp vào mục
								// Lấy chỉ số của mục được chọn
								int index = list.locationToIndex(e.getPoint());

								// Lấy email từ model tại chỉ số đã chọn
								String selectedEmail = model.getElementAt(index);

								// Thực hiện hành động nào đó với email, ví dụ: hiển thị email
								System.out.println("Bạn đã chọn email: " + selectedEmail);
//								showFileContent(selectedEmail, email);
							}
						}
					});

				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_1.setBounds(378, 186, 147, 33);
		contentPane.add(btnNewButton_1);
	}
	private void showFileContent(String fileName, String email) {
		// Tạo một cửa sổ mới để hiển thị nội dung tệp
		JFrame fileWindow = new JFrame("Nội dung của " + fileName);
		fileWindow.setSize(400, 300);
		fileWindow.setLocationRelativeTo(null);

		JTextArea textArea = new JTextArea();
		textArea.setEditable(false);  // Không cho phép chỉnh sửa

		// Đọc nội dung từ tệp và hiển thị trong JTextArea
		try (BufferedReader reader = new BufferedReader(new FileReader("server/accounts/"+email+"/"+fileName))) {
			String line;
			while ((line = reader.readLine()) != null) {
				textArea.append(line + "\n");
			}
		} catch (IOException e) {
			System.out.println(e);
		}

		// Thêm JTextArea vào JScrollPane để có thanh cuộn
		JScrollPane scrollPane = new JScrollPane(textArea);
		fileWindow.add(scrollPane);

		// Hiển thị cửa sổ
		fileWindow.setVisible(true);
	}

}