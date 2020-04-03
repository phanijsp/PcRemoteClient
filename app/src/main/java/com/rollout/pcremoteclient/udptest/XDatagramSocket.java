package com.rollout.pcremoteclient.udptest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;

public class XDatagramSocket extends DatagramSocket {
	Thread receivethread;
	ArrayList<PacketListener> packetListeners;
	int buffer_size = 1024 * 60;

	public XDatagramSocket(int port) throws SocketException {
		super(port);
	}

	public void beginReceiving() {
		packetListeners = new ArrayList<>();
		receivethread = new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						byte[] buffer = new byte[buffer_size];
						DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
						receive(datagramPacket);
						Data_Object data_Object = getData_Object(datagramPacket);

						for (int i = 0; i < packetListeners.size(); i++) {
							PacketListener packetListener = packetListeners.get(i);
							packetListener.onPacketReceive(datagramPacket,data_Object);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		};
		receivethread.start();
	}

	public Data_Object getData_Object(DatagramPacket datagramPacket) throws IOException, ClassNotFoundException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
		ObjectInputStream ObjectInputStream = new ObjectInputStream(byteArrayInputStream);
		Data_Object data_Object = (Data_Object) ObjectInputStream.readObject();
		return data_Object;
	}

	public void stopReceiving() {
		receivethread.interrupt();
	}

	public void addPacketListener(PacketListener packetListener) {
		packetListeners.add(packetListener);
	}

}