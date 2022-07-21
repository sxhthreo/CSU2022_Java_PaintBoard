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
	static List<Socket> list = new ArrayList<Socket>();  // 存储客户端

	public void run() {
		try {
			server = new ServerSocket(10111);
			while(true) {
				socket = server.accept();
				list.add(socket);
				// 开启文件传输线程
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
	//发录音
	public void run() {
		try {
			DataInputStream input = null;
			DataOutputStream output = null;
			DataOutputStream fileWriter = null;
			input = new DataInputStream(nowSocket.getInputStream());  // 输入流
			while (true) {
				// 获取文件名字和文件长度
				String textName = input.readUTF();
				long textLength = input.readLong();
				// 发送文件名字和文件长度给所有客户端
				for(Socket socket: ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream());  // 输出流
					if(socket != nowSocket) {  // 发送给其它客户端
						output.writeUTF(textName);
						output.flush();		   //清空输出流
						output.writeLong(textLength);	//写入文件长度
						output.flush();
					}
				}
				// 发送文件内容
				int length = -1;
				long curLength = 0;
				byte[] buff = new byte[1024];
				File userFile = new File("接收文件\\教师");		//自己也要保存一份并播放
				if(!userFile.exists()) {  // 新建当前用户的文件夹
					userFile.mkdirs();
				}
				String file_path = "接收文件\\教师\\"+ textName;
				File file = new File(file_path);
				fileWriter = new DataOutputStream(new FileOutputStream(file));
				while ((length = input.read(buff)) > 0) {
					curLength += length;
					for(Socket socket: ServerFileThread.list) {
						output = new DataOutputStream(socket.getOutputStream());  // 输出流
						if(socket != nowSocket) {  // 发送给其它客户端
							output.write(buff, 0, length);
							output.flush();
						}
					}
					fileWriter.write(buff, 0, length);
					fileWriter.flush();
					if(curLength == textLength) {  // 强制退出，已经发完了
						break;
					}
				}
				MyRecorder play_record = new MyRecorder();	//播放录音
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
			// 发送文件名字和文件长度给所有客户端
			for(Socket socket: ServerFileThread.list) {
				output = new DataOutputStream(socket.getOutputStream());  // 输出流
				output.writeUTF(file.getName());
				output.flush();		   //清空输出流
				output.writeLong(file.length());	//写入文件长度
				output.flush();
			}
			// 发送文件内容
			int length = -1;
			long curLength = 0;
			byte[] buff = new byte[1024];
			while ((length = input.read(buff)) > 0) {
				curLength += length;
				for(Socket socket: ServerFileThread.list) {
					output = new DataOutputStream(socket.getOutputStream());  // 输出流
					output.write(buff, 0, length);
					output.flush();
				}
				if(curLength == file.length()) {  // 强制退出，已经发完了
					break;
				}
			}
		} catch (Exception e) {}
	}
}