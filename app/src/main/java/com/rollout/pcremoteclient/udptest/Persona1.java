package com.rollout.pcremoteclient.udptest;

import java.net.*;
import java.io.*;

public class Persona1 {
	public static void main(String[] args) {
		try {
			final XDatagramSocket datagramSocket = new XDatagramSocket(6969);
			datagramSocket.beginReceiving();
			datagramSocket.addPacketListener(new PacketListener("update", "Aditya") {
				@Override
				public void onPacketReceive(DatagramPacket datagramPacket, Data_Object data_Object) {
					System.out.println(
							data_Object.getSender() + "\n" + data_Object.getType() + "\n" + data_Object.getData() + "\n"
									+ data_Object.getContinuous() + "\n" + data_Object.getContinuous_filename());
				}
			});
			datagramSocket.addPacketListener(new PacketListener("text") {
				@Override
				public void onPacketReceive(DatagramPacket datagramPacket, Data_Object data_Object) {
					System.out.println(data_Object.getData());
				}
			});
			datagramSocket.addPacketListener(new PacketListener("file", "Aditya", "yes") {
				@Override
				public void onPacketReceive(DatagramPacket datagramPacket, Data_Object data_Object) {
					if (data_Object.getContinuous_id().equals("1")) {
						File file = new File(data_Object.getContinuous_filename());
						try {
							file.createNewFile();
							fileOutputStream = new FileOutputStream(file);
							fileOutputStream.write(data_Object.getData());
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						try {
							fileOutputStream.write(data_Object.getData());
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}