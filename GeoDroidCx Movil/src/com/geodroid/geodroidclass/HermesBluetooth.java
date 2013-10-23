package com.geodroid.geodroidclass;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.geodroid.geodroidcx.Principal;

import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class HermesBluetooth implements Runnable{
	private BluetoothSocket socket;
	private InputStream ins;
	private OutputStream ons;
	private byte valor;
	private int i=0;
	private Principal pr;
	
	public HermesBluetooth(Principal pr){
		this.pr=pr;
	}
	
	public void run() {
		try{
			socket = pr.dispositivo.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
	  	   	socket.connect();
	  	   	ins = socket.getInputStream();
	  	   	ons = socket.getOutputStream();
	  	  }catch(Exception ex){
	  		Log.i("en otro lado", "Por fin el problema"+ex);
	  	  }
	}
	
	/**
	 * sendInfo
	 * @param message
	 */
	public void sendInfo(String message) {
		byte[] buffer;
		try {
			ons = new DataOutputStream(this.socket.getOutputStream());
		} catch (IOException e) {}
		try {
			buffer = (message).getBytes();
			ons.write(buffer);
			ons.flush();
		} catch (IOException e) {}
	}
	
	/**
	 * receiveInfo
	 * @return
	 */
	public String receiveInfo(){
		try {
			ins = new DataInputStream(this.socket.getInputStream());
		} catch (IOException e) {
			return "BLUETOOTH_INPUTSTREAM_FAIL";
		}
		try {
			char readChar=(char)((DataInputStream) ins).readByte();
			
			while(readChar!='#'){
				readChar=(char)((DataInputStream) ins).readByte();
				}
			Integer cBytas=Integer.parseInt("" + (char)((DataInputStream) ins).readByte())*10;
			Integer cBytes=Integer.parseInt("" + (char)((DataInputStream) ins).readByte())+cBytas;
			
			byte buffer[] = new byte[cBytes];
			ins.read(buffer,0,(cBytes));
			return new String(buffer);
		} catch (IOException e) {
			return "BLUETOOTH_RECIEVE_FAIL";
		}
	}
	
	

}

