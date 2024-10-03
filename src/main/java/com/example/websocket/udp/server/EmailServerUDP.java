package com.example.websocket.udp.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author Desktop
 */
public class EmailServerUDP {

	public static String listFilesInUserFolder(String username) throws IOException {
		Path userFolderPath = Paths.get("server/accounts/" + username);
		if (Files.exists(userFolderPath) && Files.isDirectory(userFolderPath)) {
			return Files.list(userFolderPath)
					.filter(Files::isRegularFile)
					.map(Path::getFileName)
					.map(Path::toString)
					.collect(Collectors.joining(", "));
		}
		return "No files found";
	}

	public static String sendEmail(String email, String emailContent,String senTo, String title) {

		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");
		String formattedDateTime = now.format(formatter);
		String sendMailString = "Người gửi: "+ email+"\n"+ "Tiêu đề: " + title+"\n"+"Nội dung: "+emailContent;
		if (emailContent.isEmpty()) {
			return "ERROR: Email content is empty.";
		}

		Path accountPath = Paths.get("server/accounts/"+senTo);

		if (!Files.exists(accountPath)) {
			return "ERROR: Account does not exist.";
		}

		try {

			// Tạo file mới với tên theo thời gian gửi email
			String emailFileName = "from_"+email+"_"+title+"_" + formattedDateTime  + ".txt"; // Tên file dựa trên thời gian
			Files.write(accountPath.resolve(emailFileName), sendMailString.getBytes());
			System.out.println("["+formattedDateTime+"] "+email+" đã được gửi một nội dung đến " + senTo + ".");
			return "Email sent successfully.";
		} catch (IOException e) {
			return "ERROR: " + e.getMessage();
		}
	}

	public static void registerFolder(String email,String password) throws IOException{
		Path path = Paths.get("server/accounts/"+email);
		if (!Files.exists(path)) {
			Files.createDirectories(path);
			System.out.println(password);
			System.out.println("Thư mục cho tài khoản " + email + " đã được tạo.");
			String textString= email+":"+password+"\n\"Thank you for using this service.\"";
			Files.write(path.resolve(email+".txt"), textString.getBytes());
			System.out.println("File "+email+".txt đã được tạo cho tài khoản " + email + ".");
		} else {
			System.out.println("Tài khoản " + email + " đã tồn tại.");
		}
	}
	public static boolean checkUsernameRegister(String username) {
		Path accountPath = Paths.get("server/accounts/" + username);
		return Files.exists(accountPath);
	}

	public static boolean checkUsernameExists(String email,String password) {
		Path accountPath = Paths.get("server/accounts/" + email+"/"+email + ".txt"); // Đảm bảo file có đuôi .txt

		// Kiểm tra xem file có tồn tại không
		if (!Files.exists(accountPath)) {
			return false; // File không tồn tại
		}

		// Đọc nội dung file và kiểm tra username và password
		try (BufferedReader reader = Files.newBufferedReader(accountPath)) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] credentials = line.split(":");
				if (credentials.length == 2) {
					String storedUsername = credentials[0].trim();
					String storedPassword = credentials[1].trim();

					// Kiểm tra xem username và password có khớp không
					if (storedUsername.equals(email) && storedPassword.equals(password)) {
						return true; // Tìm thấy username và password khớp

					}
				}
			}

		} catch (IOException e) {
			System.err.println("Lỗi khi đọc file: " + e.getMessage());
		}

		return false;
	}

	private static String handleRequest(String request) {

		String[] requestParts = request.split(" ",6);
		String command = requestParts[0];
		String username = (requestParts.length > 1) ? requestParts[1] : "";
		String password = (requestParts.length > 5) ? requestParts[5] : "";
		String emailContent = (requestParts.length > 2) ? requestParts[2] : "";
		String senToString =(requestParts.length >3) ? requestParts[3]: "" ;
		String titleString =(requestParts.length >4) ? requestParts[4]: "" ;

		try {
			switch (command) {
				case "CREATE_ACCOUNT":
					registerFolder(username,password);
					return "Account created";
				case "CHECK_USERNAME":
					return checkUsernameExists(username,password) ? "EXISTS" : "NOT_EXISTS";
				case "SEND_EMAIL":
					return sendEmail(username, emailContent,senToString,titleString);
				case "DISPLAY_EMAIL":
					return listFilesInUserFolder( username);
				default:
					return "UNKNOWN_COMMAND";
			}
		} catch (IOException e) {
			return "ERROR: " + e.getMessage();
		}
	}


	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		try {
			DatagramSocket socket = new DatagramSocket(9875);
			byte[] receiveData = new byte[1024];
			System.out.println("server running...");

			while(true){
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				// Nhận một gói tin

				socket.receive(receivePacket);

				String clientRequest  = new String(receivePacket.getData(), 0, receivePacket.getLength());

				InetAddress clientAddress = receivePacket.getAddress();
				int clientPort = receivePacket.getPort();



				String response = handleRequest(clientRequest);
				byte[] sendData = response.getBytes();
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
				socket.send(sendPacket);

			}
			//socket.close();
		} catch (Exception e) {
			e.printStackTrace();;
		}
	}


}