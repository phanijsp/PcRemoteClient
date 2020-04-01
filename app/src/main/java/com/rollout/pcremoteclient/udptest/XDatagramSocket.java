package com.rollout.pcremoteclient.udptest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.util.ArrayList;

public class XDatagramSocket extends DatagramSocket {
    Thread receivethread;
    ArrayList<PacketListener> packetListeners;
    int buffer_size = 1024*60;

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

                            // Type
                            if (!packetListener.getType().equals("-1") && packetListener.getSender().equals("-1")
                                    && packetListener.getContinuous().equals("-1")
                                    && packetListener.getContinuous_filename().equals("-1")
                                    && packetListener.getContinuous_id().equals("-1")
                                    ) {
                                if (packetListener.getType().equals(data_Object.getType())) {
                                    packetListener.onPacketReceive(datagramPacket, data_Object);
                                }
                            }
                            // Type, Sender
                            else if (!packetListener.getType().equals("-1") && !packetListener.getSender().equals("-1")
                                    && packetListener.getContinuous().equals("-1")
                                    && packetListener.getContinuous_filename().equals("-1")
                                    && packetListener.getContinuous_filesize().equals("-1")
                                    && packetListener.getContinuous_id().equals("-1")
                                    ) {
                                if (packetListener.getType().equals(data_Object.getType())
                                        && packetListener.getSender().equals(data_Object.getSender())) {
                                    packetListener.onPacketReceive(datagramPacket, data_Object);
                                }
                            } 
                            //Type, Sender, Continuous
                            else if (!packetListener.getType().equals("-1")
                                    && !packetListener.getSender().equals("-1")
                                    && !packetListener.getContinuous().equals("-1")
                                    && packetListener.getContinuous_filename().equals("-1")
                                    && packetListener.getContinuous_filesize().equals("-1")
                                    && packetListener.getContinuous_id().equals("-1")
                                    ) {
                                if (packetListener.getType().equals(data_Object.getType())
                                        && packetListener.getSender().equals(data_Object.getSender())
                                        && packetListener.getContinuous().equals(data_Object.getContinuous())) {
                                    packetListener.onPacketReceive(datagramPacket, data_Object);
                                }
                            }

                            // Continuous, Continuous_filename, Continuous_filesize
                            else if (packetListener.getType().equals("-1") && packetListener.getSender().equals("-1")
                                    && !packetListener.getContinuous().equals("-1")
                                    && !packetListener.getContinuous_filename().equals("-1")
                                    && !packetListener.getContinuous_filesize().equals("-1")
                                    && packetListener.getContinuous_id().equals("-1")
                                    ) {
                                if (packetListener.getContinuous().equals(data_Object.getContinuous())
                                        && packetListener.getContinuous_filename()
                                                .equals(data_Object.getContinuous_filename())
                                        && packetListener.getContinuous_filesize()
                                                .equals(data_Object.getContinuous_filesize())) {

                                    packetListener.onPacketReceive(datagramPacket, data_Object);
                                }
                            }
                            // Continuous, Continuous_filename, Continuous_filesize, Continuous_id
                            else if (packetListener.getType().equals("-1") && packetListener.getSender().equals("-1")
                                    && !packetListener.getContinuous().equals("-1")
                                    && !packetListener.getContinuous_filename().equals("-1")
                                    && !packetListener.getContinuous_filesize().equals("-1")
                                    && !packetListener.getContinuous_id().equals("-1")
                                    ) {
                                if (packetListener.getContinuous().equals(data_Object.getContinuous())
                                        && packetListener.getContinuous_filename()
                                                .equals(data_Object.getContinuous_filename())
                                        && packetListener.getContinuous_filesize()
                                                .equals(data_Object.getContinuous_filesize())
                                        && packetListener.getContinuous_id().equals(data_Object.getContinuous_id())) {
                                    packetListener.onPacketReceive(datagramPacket, data_Object);
                                }
                            } else {
                                System.out.println("Why ?");
                            }

                        }
                        // System.out.println(datagramPacket.getAddress());
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