package Teacher;

import Student.MyRecorder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerFileThread extends Thread{
	ServerSocket server = null;
	Socket socket = null;
	static List<Socket> list = new ArrayList<Socket>();  // �洢�ͻ���

	public void run() {
		try {
			server = new ServerSocket(10111);
			while(true) {
				socket = server.accept();
				list.add(socket);
				// �����ļ������߳�
				FileReadAndWrite fileReadAndWrite = new FileReadAndWrite(socket);
				fileReadAndWrite.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

class FileReadAndWrite extends Thread{
	private Socket nowSocket = null;
	public FileReadAndWrite(Socket socket) {
		this.nowSocket = socket;
	}
	//��¼��
	public void run() {
		try {
			DataInputStream input = null;
			DataOutputStream output = null;
			DataOutputStream fileWriter = null;
			input = new DataInputStream(nowSocket.getInputStream());  // ������
			while (true) {
				// ��ȡ�ļ����ֺ��ļ�����
				String textName = input.readUTF();
				long textLength = input.readLong();
				// �����ļ����ֺ��ļ����ȸ����пͻ���
				for(Socket socket: ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream());  // �����
					if(socket != nowSocket) {  // ���͸������ͻ���
						output.writeUTF(textName);
						output.flush();		   //��������
						output.writeLong(textLength);	//д���ļ�����
						output.flush();
					}
				}
				// �����ļ�����
				int length = -1;
				long curLength = 0;
				byte[] buff = new byte[1024];
				File userFile = new File("�����ļ�\\��ʦ");		//�Լ�ҲҪ����һ�ݲ�����
				if(!userFile.exists()) {  // �½���ǰ�û����ļ���
					userFile.mkdirs();
				}
				String file_path = "�����ļ�\\��ʦ\\"+ textName;
				File file = new File(file_path);
				fileWriter = new DataOutputStream(new FileOutputStream(file));
				while ((length = input.read(buff)) > 0) {
					curLength += length;
					for(Socket socket: ServerFileThread.list) {
						output = new DataOutputStream(socket.getOutputStream());  // �����
						if(socket != nowSocket) {  // ���͸������ͻ���
							output.write(buff, 0, length);
							output.flush();
						}
					}
					fileWriter.write(buff, 0, length);
					fileWriter.flush();
					if(curLength == textLength) {  // ǿ���˳����Ѿ�������
						break;
					}
				}
				MyRecorder play_record = new MyRecorder();	//����¼��
				play_record.play(file_path);
			}
		} catch (Exception e) {}
	}

	public static void outFileToClient(String path) {
		try {
			File file = new File(path);
			DataInputStream input = null;
			DataOutputStream output = null;
			input = new DataInputStream(new FileInputStream(file));
			// �����ļ����ֺ��ļ����ȸ����пͻ���
			for(Socket socket: ServerFileThread.list) {
				output = new DataOutputStream(socket.getOutputStream());  // �����
				output.writeUTF(file.getName());
				output.flush();		   //��������
				output.writeLong(file.length());	//д���ļ�����
				output.flush();
			}
			// �����ļ�����
			int length = -1;
			long curLength = 0;
			byte[] buff = new byte[1024];
			while ((length = input.read(buff)) > 0) {
				curLength += length;
				for(Socket socket: ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream());  // �����
					output.write(buff, 0, length);
					output.flush();
				}
				if(curLength == file.length()) {  // ǿ���˳����Ѿ�������
					break;
				}
			}
		} catch (Exception e) {}
	}
}