package com.rollout.pcremoteclient.udptest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.rollout.pcremoteclient.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity {
	ImageView imageView;
	byte[] ack_data;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		init();
		try {
			final XDatagramSocket datagramSocket = new XDatagramSocket(6969);
			Thread thread = new Thread(){
				@Override
				public void run(){
					String s = "Hello";
					byte[] arr = s.getBytes();
					try {
						DatagramPacket datagramPacket = new DatagramPacket(arr,arr.length, InetAddress.getByName("192.168.0.8"),6969);
						datagramSocket.send(datagramPacket);
						Log.i("hehe","Sent it");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					datagramSocket.beginReceiving();
					final ByteArrayOutputStream[] byteArrayOutputStream = new ByteArrayOutputStream[1];
					datagramSocket.addPacketListener(new PacketListener("file","Aditya","yes"){
						@Override
						public void onPacketReceive(DatagramPacket datagramPacket, Data_Object data_Object) {
							if(data_Object.getContinuous_id().equals("1")){
								byteArrayOutputStream[0] = new ByteArrayOutputStream();
								try {
									byteArrayOutputStream[0].write(data_Object.getData());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							else if(data_Object.getContinuous_id().equals("-1")){
								try {
									byteArrayOutputStream[0].write(data_Object.getData());
									byte[] image_array = byteArrayOutputStream[0].toByteArray();
									BitmapFactory.Options options = new BitmapFactory.Options();
									options.inMutable = true ;
									final Bitmap bmp = BitmapFactory.decodeByteArray(image_array,0,image_array.length,options);
									runOnUiThread(new Runnable() {
										@Override
										public void run() {
											imageView.setImageBitmap(bmp);
										}
									});
								} catch (IOException e) {
									e.printStackTrace();
								}
							}else{
								try {
									byteArrayOutputStream[0].write(data_Object.getData());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							byte[] ack_data = new byte[1024];
							Data_Object data_Object2 = new Data_Object("ack", null, null, null, null, null, null);
							ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
							ObjectOutputStream objectOutputStream;
							try {
								objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
								objectOutputStream.writeObject(data_Object2);
								ack_data = byteArrayOutputStream.toByteArray();
								DatagramPacket ack_DatagramPacket = new DatagramPacket(ack_data, ack_data.length,
										datagramPacket.getAddress(), datagramPacket.getPort());
								datagramSocket.send(ack_DatagramPacket);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					});
				}
			};
			thread.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public void init(){
		imageView = (ImageView) findViewById(R.id.iter);
		Data_Object data_object = new Data_Object("ack",null,null,null,null,null,null);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(data_object);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ack_data = byteArrayOutputStream.toByteArray();
	}
	public DatagramPacket ack_packet(DatagramPacket datagramPacket){
		DatagramPacket ack = new DatagramPacket(ack_data,ack_data.length,datagramPacket.getAddress(),datagramPacket.getPort());
		return ack;
	}
}
