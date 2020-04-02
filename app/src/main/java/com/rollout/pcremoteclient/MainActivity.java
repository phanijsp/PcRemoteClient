package com.rollout.pcremoteclient;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.SocketHandler;

public class MainActivity extends AppCompatActivity {

	ImageView imageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		Thread thread = new Thread(){
			@Override
			public void run(){
				try {
					Socket socket = new Socket("192.168.0.8",6969);
					ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
					while (true) {
						final byte[] image_bytearr = (byte[])objectInputStream.readObject();
						BitmapFactory.Options options = new BitmapFactory.Options();
						options.inMutable = true;
						final Bitmap bmp = BitmapFactory.decodeByteArray(image_bytearr, 0, image_bytearr.length, options);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								imageView.setImageBitmap(bmp);
								Log.i("hehe", "ahahah");
							}
						});
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	public void init(){
		imageView = (ImageView) findViewById(R.id.imageView);
	}
}
