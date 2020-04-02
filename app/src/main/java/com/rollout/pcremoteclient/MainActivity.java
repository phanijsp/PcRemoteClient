package com.rollout.pcremoteclient;

import androidx.appcompat.app.AppCompatActivity;

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

import java.io.ObjectInputStream;
import java.net.Socket;
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
				String pass_str = ip.getText().toString();
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

	public void connect(String ip_str,String pass_str) {

	}


}
