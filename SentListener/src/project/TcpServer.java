package project;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class TcpServer extends JFrame{
	private Container cp;
	private JTextField SentText;
	private JTextArea ReceiveArea; 
	private JScrollPane sp;
	private JPanel p1,p2;
	private ServerSocket sSocket;
	private Socket cSocket;
	private DataOutputStream dos;
	private DataInputStream dis;
	TcpServer(){
		
		super("Server_Program");
		cp = this.getContentPane();
		SentText = new JTextField(18);
		ReceiveArea = new JTextArea(10,18);
		ReceiveArea.setEditable(false);
		p1 = new JPanel();
		p1.setBorder(BorderFactory.createTitledBorder("Receive content:"));
		p1.add(ReceiveArea);
		sp = new JScrollPane(p1);
		cp.add(sp, BorderLayout.NORTH);
		p2 = new JPanel();
		p2.setBorder(BorderFactory.createTitledBorder("Send content:"));
		p2.add(SentText);
		cp.add(p2, BorderLayout.SOUTH);
		SentText.addActionListener((ActionListener) new SentListener());
		this.setLocation(10, 10);
		this.setSize(250,330);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	private void StartServer() {
		try {
			sSocket = new ServerSocket(1818);
			cSocket = sSocket.accept();
			dos = new DataOutputStream(cSocket.getOutputStream());
			dis = new DataInputStream(cSocket.getInputStream());
			new Thread(new ReceiveThread()).start();
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	class ReceiveThread implements Runnable{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			String tempInfo = null;
			while(true) {
				try {
					tempInfo = "User:" + dis.readUTF() + "\n";		
				} catch (IOException e) {
					e.printStackTrace();
					System.exit(0);
				}
				ReceiveArea.append(tempInfo);
				ReceiveArea.setCaretPosition(ReceiveArea.getText().length());
			}
		}
		
	}
	class SentListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			String tempInfo = SentText.getText().trim();
			SentText.setText("");
			try {
				dos.writeUTF(tempInfo);
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}

			ReceiveArea.append("Me:" + tempInfo + "\n");
			ReceiveArea.setCaretPosition(ReceiveArea.getText().length());
			
		}
	}
	public static void main(String[] args) {
		TcpServer s = new TcpServer();
		s.StartServer();
	}
}
