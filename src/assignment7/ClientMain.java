package assignment7;

import java.io.*;
import java.lang.reflect.*;
import java.net.*; 
import javax.swing.*; 
import java.awt.*; 
import java.awt.event.*;

public class ClientMain { 
	private JTextArea incoming; 
	private JTextField outgoing;
	private JTextField clientName;
	private BufferedReader reader; 
	private PrintWriter writer;
	private static String myPackage;
	
	public void run() throws Exception {
		initView(); 
		setUpNetworking();
	}
	static {
	    myPackage = ClientMain.class.getPackage().toString().split(" ")[1];
	    } 
	public void initView() {//can ignore and replace with GUI
		JFrame frame = new JFrame("Client A"); 
		JPanel mainPanel = new JPanel(); 
		incoming = new JTextArea(15, 50); 
		incoming.setLineWrap(true); 
		incoming.setWrapStyleWord(true); 
		incoming.setEditable(false); 
		JScrollPane qScroller = new JScrollPane(incoming);
		
qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

		outgoing = new JTextField(20); 
		JButton sendButton = new JButton("Send"); 
		sendButton.addActionListener(new SendButtonListener()); 
		
//		clientName=new JTextField(20);
//		JButton startConvo= new JButton("Start");
//		startConvo.addActionListener(new StartConvoListener());
		
		//JButton groupButton=new JButton("Make Group");
		//groupButton.addActionListener();
		
		mainPanel.add(qScroller); 
		mainPanel.add(outgoing); 
		mainPanel.add(sendButton);
//		mainPanel.add(clientName);
//		mainPanel.add(startConvo);
		frame.getContentPane().add(BorderLayout.CENTER, mainPanel); 
		frame.setSize(650, 500); 
		frame.setVisible(true);
	} 
	private void setUpNetworking() throws Exception {
		@SuppressWarnings("resource") 
		Socket sock = new Socket("127.0.0.1", 4242);
		InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
		reader = new BufferedReader(streamReader); 
		writer = new PrintWriter(sock.getOutputStream()); 
		System.out.println("networking established"); 
		Thread readerThread = new Thread(new IncomingReader()); 
		readerThread.start();
	}
	class SendButtonListener implements ActionListener { 
		public void actionPerformed(ActionEvent ev) {
			writer.println(outgoing.getText()); 
			writer.flush();
			outgoing.setText("");
			outgoing.requestFocus();
		}
	}
/*	class StartConvoListener implements ActionListener{
		public void actionPerformed(ActionEvent ev){
			Class<?> myClient = null;
			Constructor<?> constructor = null;
			Object instanceOfMyClient = null;
	        String client_class_name=myPackage+"."+clientName.getText();
	        try {
				myClient = Class.forName(client_class_name); 	// Class object of specified name
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			try {
				constructor = myClient.getConstructor();		// No-parameter constructor object
				instanceOfMyClient = constructor.newInstance();	// Create new object using constructor
			} catch (InstantiationException e1) {											// various exceptions 
				// Do whatever is needed to handle the various exceptions here -- e.g. rethrow Exception
				e1.printStackTrace();
			} 
			catch (NoSuchMethodException e) {
				e.printStackTrace();
			} 
			catch (SecurityException e) {
				e.printStackTrace();
			}
			catch(IllegalAccessException e2){
				e2.printStackTrace();
			}
			catch(IllegalArgumentException e3){
				e3.printStackTrace();
			} 
			catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			ClientMain me = (ClientMain)instanceOfMyClient;
	        
		}
	}*/
	public static void main(String[] args) {
		try {
			new ClientMain().run();
		} catch (Exception e) {
			e.printStackTrace(); 
		}
	} 
	
	class IncomingReader implements Runnable {
		public void run() { 
			String message; 
			try {
				while ((message = reader.readLine()) != null) {
					incoming.append(message + "\n"); 
					}
			} catch (IOException ex) { 
				ex.printStackTrace(); 
			}
		}
	}
}
