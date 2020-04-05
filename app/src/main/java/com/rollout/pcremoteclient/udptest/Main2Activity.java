package com.rollout.pcremoteclient.udptest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rollout.pcremoteclient.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class Main2Activity extends AppCompatActivity {
	ImageView imageView;
	TextView fps_display;
	int fps_count;

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus) {
			hideSystemUI();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main2);
		init();
		start_fps_meter();

		try {
			final DatagramSocket datagramSocket = new DatagramSocket(6969);
			Thread thread = new Thread(){
				@Override
				public void run(){
					Log.i("Thread_log","thread_1");
					final ByteArrayOutputStream[] byteArrayOutputStream = new ByteArrayOutputStream[1];
					while(true){
						byte[] data = new byte[1024*60];
						DatagramPacket datagramPacket = new DatagramPacket(data,data.length);
						try {
							datagramSocket.receive(datagramPacket);
							ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
							ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
							Data_Object data_Object = (Data_Object) objectInputStream.readObject();
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
										final byte[] image_array = byteArrayOutputStream[0].toByteArray();
										BitmapFactory.Options options = new BitmapFactory.Options();
										options.inMutable = true ;
										final Bitmap bmp = BitmapFactory.decodeByteArray(image_array,0,image_array.length,options);
										runOnUiThread(new Runnable() {
											@Override
											public void run() {
												Log.i("Thread_log",String.valueOf(fps_count));
												imageView.setImageBitmap(bmp);
												fps_count++;
											}
										});


								} catch (Exception e) {
									e.printStackTrace();
								}
							}else{
								try {
									byteArrayOutputStream[0].write(data_Object.getData());
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			};
			thread.start();
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	public void init(){
		fps_count=0;
		fps_display = (TextView) findViewById(R.id.fps);
		imageView = (ImageView) findViewById(R.id.iter);
	}
	public void setImageView(Bitmap bitmap){

	}

	public void start_fps_meter(){
		Thread threadx = new Thread(){
			@Override
			public void run(){
				while (true){
					if(fps_count!=0){
						try{
							fps_display.setText(String.valueOf(fps_count));
							fps_count=0;
						}catch (Exception e){
							e.printStackTrace();
						}
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		threadx.start();
	}


	private void hideSystemUI() {
		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(
				View.SYSTEM_UI_FLAG_IMMERSIVE
						| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
						| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
						| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
						| View.SYSTEM_UI_FLAG_FULLSCREEN);
	}
}
