import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class User {
	private Socket socket = null;
	private SocketAddress socketAdress = null;
	private BufferedReader in = null;
	private BufferedWriter out = null;
	
	private String name = null;
	private AESdecode decode = AESdecode.getInstance();
	
	public String SERVER_IP = "localhost";
	private int SERVER_PORT = 9994;
	private int roomCount = 0;
	private int timeout = 2000;
	int count = 0;
	
	public void setup() {
		try {			
			socket = new Socket();			
			socketAdress= new InetSocketAddress(SERVER_IP , SERVER_PORT);
			//socket = new Socket(SERVER_IP, SERVER_PORT);			
			socket.connect(socketAdress,timeout);
			//socket.setSoTimeout(timeout);		
			if(socket.isConnected()){
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				/*
				 * socket2.setTcpNoDelay(true); socket2.setSendBufferSize(256);
				 */
				
			}

		} catch (Exception e) {			
			handleError(e.getMessage() + "3");
		}
	}
	public boolean isConnected(){
		return socket.isConnected();
	}
	public void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void handleError(String text) {
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		System.out.println(text);
		// System.exit(1);
	}

	public boolean check(String ID, char PW[]) throws Exception {
		boolean value = false;
		String pw = "";
		for (int i = 0; i < PW.length; i++) {
			pw += PW[i];
		}
		// System.out.println(decode.Encrypt(ID + "MySecretNumber19941128" + pw,
		// secretKey));

		write(ID + "MySecretNumber19941128" + pw);
		String tf = read();
		value = Boolean.parseBoolean(tf);
		// System.out.println(value+"444");
		return value;
	}

	public boolean checkEnterRoom(int roomNumber) {
		boolean value = false;
		write("CAN@%I*ENTER*THE!$ROOM" + roomNumber);
		String tfw = read();
		value = Boolean.parseBoolean(tfw);
		System.out.println(value + "받음!!");
		return value;
	}
	public String [][] refreshRoom() {
		write("CAN@%I*ENTER*THE!$ROOM" + -5);
		String roomNumString = read();
		int roomNum = Integer.parseInt(roomNumString);
		roomCount = roomNum;
		String data[][] = new String[roomNum][3];		
		for(int i=0; i<roomNum; i++){
			String rd = read();
			String temp[] = rd.split("&1&1&");
			for(int j=0;j<temp.length; j++){
				data[i][j] = temp[j];
				//System.out.println(data[i][j]);
			}
		}		
		return data;
	}
	public boolean makeRoom(String roomName, String roomPassword){
		boolean value = false;
		//CAN@%I*MAKE*THE!$ROOM
		//&*#^
		write("CAN@%I*MAKE*THE!$ROOM" + roomName + "w22%df!@@rw" + roomPassword);
		String tfw = read();
		value = Boolean.parseBoolean(tfw);
		//System.out.println(value + "받음!!");
		return value;		
	}
	public int getRoomCount(){
		return roomCount;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void write(String text) {
		try {
			out.write(decode.Encrypt(text) + "\n");
			out.flush();
		} catch (Exception e) {
			handleError(e.getMessage() + "2");
		}
	}

	public String read() {
		String text = null;
		try {
			text = in.readLine();
			
			text = decode.Decrypt(text);
			//System.out.println(text + "  " + count++);
		} catch (Exception e) {
			handleError(e.getMessage() + "1");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		return text;
	}

	public void writeFile(File file, JProgressBar bar, JLabel la, JPanel ssp) {
		new FileServiceThread(file, bar, la, ssp).start();
	}
	public void getFile(String fileName,ChattingPanel chatPanel) {
		System.out.println(fileName + "이제 들어갑니다");
		new FileRecivThread(fileName, chatPanel);

	}
	public BufferedWriter getOutputReader() {
		return out;
	}

	public BufferedReader getInputReader() {
		return in;
	}	
	class FileRecivThread extends Thread {//미완성
		private BufferedInputStream dis;
		private BufferedOutputStream dos;
		String fileName;
		File file;
		ChattingPanel chatPanel;
		public FileRecivThread(String fileName,ChattingPanel chatPanel) {
			this.fileName = fileName;
			this.chatPanel = chatPanel;
			file = new File("C:\\Test", fileName);
			if(!file.exists()){
				System.out.println("폴더가없으므로 만듭니다.");
				file.getParentFile().mkdir();
			}
			System.out.println("생성" + fileName);
			this.start();
		}
		public void run(){
			
			System.out.println(fileName+"시작이여");
			
			
			try {
			 //기록할 파일 연결함 
			System.out.println("11");
			Socket socket2 = new Socket(SERVER_IP, SERVER_PORT+1);
			dis = new BufferedInputStream(socket2.getInputStream());
			dos = new BufferedOutputStream(new FileOutputStream(file));
			byte buffer[] = new byte[1048576];
			// 보내온 파일의 끝까지 읽어서 파일로 씀 
			int size = 0;
			System.out.println("22");
				while ((size = dis.read(buffer)) != -1) {
					// Thread.sleep(100);
					System.out.println(size);
					dos.write(buffer, 0, size);
					// dos.flush();
				}
				dis.close();
				dos.close();
				socket2.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}		
			if (file.getName().contains(".jpg") || file.getName().contains(".png")
					|| file.getName().contains(".gif") || file.getName().contains(".jpeg")) {
				/*
				 * Image img = new Image; ImageIO.write(im,
				 * formatName, output)
				 */

				System.out.println("파일임" + file.getPath());
				chatPanel.printHTML("<img src='file:///" + file.getPath() + "' width='300' height='300'><br>",
						"black");
			}
		}
	}
	class FileServiceThread extends Thread {
		File file;
		JProgressBar bar;
		JLabel la;
		JPanel p;
		private BufferedInputStream dis;
		private BufferedOutputStream dos;
		public FileServiceThread(File file, JProgressBar bar, JLabel la, JPanel ssp) {
			this.file = file;
			this.bar = bar;
			this.la = la;
			this.p = ssp;
		}

		public void run() {
			write("I&WILL&FILE@SEND@25F25&@$");
			try {				
				write(file.getName());
				//System.out.println(file.getName());
				Socket socket2 = new Socket(SERVER_IP, SERVER_PORT+1);
				dis = new BufferedInputStream(new FileInputStream(file));
				dos = new BufferedOutputStream(socket2.getOutputStream());
				
				Thread.sleep(100);
				byte buffer[] = new byte[1048576];
				int size = 0;
				int len = (int) (file.length());
				int read = 0;
				int value = 0;

				while ((size = dis.read(buffer)) != -1) {
					read += size;
					dos.write(buffer, 0, size); //
					dos.flush();
					value = (int) ((double) read / len * 100);
					bar.setValue(value);
					la.setText(Integer.toString(read / 1024) + "/" + Integer.toString(len / 1024) + "KB");
					//System.out.println(la.getText());
				}
				bar.setValue(100);
				la.setText("100%");
				//write(file.getName() + "보냄");
				dos.close();
				dis.close();
				socket2.close();
				// System.out.println(b);
				p.setVisible(false);
			//	System.out.println("클라이언트에서 다보냄");
			} catch (Exception e) {
				System.out.println("파일 전송 종료");
				e.printStackTrace();
			}
		}
	}
}
