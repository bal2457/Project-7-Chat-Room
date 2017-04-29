/* Project 7 Chat Room <MyClass.java>
 * EE422C Project 7 submission by
 * Bryan Leon
 * bal2457
 * 16238
 * Daniel Laveman
 * del824
 * 16230
 * Slip days used: <0>
 * Spring 2017
 */
package assignment7;

import java.io.OutputStream; 
import java.io.PrintWriter;
import java.util.*;

public class ClientObserver extends PrintWriter implements Observer {
	public ClientObserver(OutputStream outputStream) {
		super(outputStream);
	} 
	@Override 
	public void update(Observable o, Object arg) { 
/*		String str=arg.toString();
		if(arg.getClass().isArray()){
			if(this.)
			ArrayList<String> args=(ArrayList<String>) arg;
			for(int i=0;i<args.size();i++){
				this.println(args.get(i));
			}
		}*/
		this.println(arg); //writer.println(arg); 
		this.flush(); //writer.flush(); }
	}
/*	@Override
	public String toString(){
		return ClientMain.getName();
	}*/
}