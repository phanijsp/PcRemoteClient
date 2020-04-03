package com.rollout.pcremoteclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.rollout.pcremoteclient.udptest.Data_Object;
import com.rollout.pcremoteclient.udptest.Main2Activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity {

	ImageView imageView;
	EditText ip, pass;
	Button conn;
	LottieAnimationView loading_anim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
	}

	public void init() {
		loading_anim = (LottieAnimationView) findViewById(R.id.loading);
		ip = (EditText) findViewById(R.id.editText_ip);
		pass = (EditText) findViewById(R.id.editText_pass);
		conn = (Button) findViewById(R.id.conn);
		conn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String ip_str = ip.getText().toString();
				String pass_str = pass.getText().toString();
				if (ip_str != null && pass_str != null && ip_str.length() > 0 && pass_str.length() > 0) {
					conn.setVisibility(View.INVISIBLE);
					loading_anim.setVisibility(View.VISIBLE);
					connect(ip_str,pass_str);
				} else {
					Toast.makeText(getApplicationContext(), "Oh, CMON!", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	public void connect(final String ip_str, final String pass_str) {
		Toast.makeText(getApplicationContext(),"Clicked on Connect",Toast.LENGTH_SHORT).show();
		Thread thread = new Thread(){
			@Override
			public void run(){
				try {
					DatagramSocket datagramSocket = new DatagramSocket(6969);
					Data_Object data_object = new Data_Object(null,null,null,null,null,null,pass_str.getBytes());
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
					ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
					objectOutputStream.writeObject(data_object);
					byte[] hshake = byteArrayOutputStream.toByteArray();
					DatagramPacket datagramPacket = new DatagramPacket(hshake,hshake.length, InetAddress.getByName(ip_str),6970);
					datagramSocket.send(datagramPacket);


					byte[] status = new byte[1024*60];
					datagramPacket = new DatagramPacket(status,status.length);
					Log.i("datag","I'm here");
					datagramSocket.receive(datagramPacket);
					ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(status);
					ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
					Data_Object reply_object = (Data_Object) objectInputStream.readObject();
					datagramSocket.close();
					Intent intent = new Intent(MainActivity.this, Main2Activity.class);
					startActivity(intent);
					Log.i("datag",new String(reply_object.getData()));
				} catch (SocketException | UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						loading_anim.setVisibility(View.INVISIBLE);
						conn.setVisibility(View.VISIBLE);
						ip.setText("");
						pass.setText("");
					}
				});
			}

		};
		thread.start();

	}


}
