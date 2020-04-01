package com.rollout.pcremoteclient.udptest;

import java.io.FileOutputStream;
import java.net.*;
import java.util.ArrayList;

public class PacketListener {
	private String type;
	private String sender;
	private String continuous;
	private String continuous_filename;
	private String continuous_filesize;
	private String continuous_id;
	private byte[] data;
	public FileOutputStream fileOutputStream;

	PacketListener(String type) {
		this.type = type;
		this.sender = "-1";
		this.continuous = "-1";
		this.continuous_filename = "-1";
		this.continuous_filesize = "-1";
		this.continuous_id = "-1";
		// this.data="-1";
	}

	PacketListener(String type, String sender) {
		this.type = type;
		this.sender = sender;
		this.continuous = "-1";
		this.continuous_filename = "-1";
		this.continuous_filesize = "-1";
		this.continuous_id = "-1";
		// this.data="-1";
	}

	PacketListener(String type, String sender, String continuous) {
		this.type = type;
		this.sender = sender;
		this.continuous = continuous;
		this.continuous_filename = "-1";
		this.continuous_filesize = "-1";
		this.continuous_id = "-1";
		// this.data="-1";
	}

	PacketListener(String continuous, String continuous_filename, String continuous_filesize, String continuous_id) {
		this.type = "-1";
		this.sender = "-1";
		this.continuous = continuous;
		this.continuous_filename = continuous_filename;
		this.continuous_filesize = continuous_filesize;
		this.continuous_id = continuous_id;
		// this.data="-1";
	}

	public void onPacketReceive(DatagramPacket datagramPacket, Data_Object data_Object) {

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getContinuous() {
		return continuous;
	}

	public void setContinuous(String continuous) {
		this.continuous = continuous;
	}

	public String getContinuous_filename() {
		return continuous_filename;
	}

	public void setContinuous_filename(String continuous_filename) {
		this.continuous_filename = continuous_filename;
	}

	public String getContinuous_filesize() {
		return continuous_filesize;
	}

	public void setContinuous_filesize(String continuous_filesize) {
		this.continuous_filesize = continuous_filesize;
	}

	public String getContinuous_id() {
		return continuous_id;
	}

	public void setContinuous_id(String continuous_id) {
		this.continuous_id = continuous_id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}