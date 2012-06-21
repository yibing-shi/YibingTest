package com.yibing.test.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class Server {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ServerSocketChannel serverChannel = null;
		Selector selector = null;
		try {
			serverChannel = ServerSocketChannel.open();
			ServerSocket ss = serverChannel.socket();
			InetSocketAddress addr = new InetSocketAddress(11009);
			ss.bind(addr);
			serverChannel.configureBlocking(false);
			selector = Selector.open();
			serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		while (true) {
			try {
				selector.select();
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
			
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = readyKeys.iterator();
			while (it.hasNext()) {
				SelectionKey key = it.next();
				it.remove();
				
				try {
					if (key.isAcceptable()) {
						ServerSocketChannel server = (ServerSocketChannel ) key.channel( );
						SocketChannel client = server.accept();
						System.out.println("Accept a connection from " + client.socket().getRemoteSocketAddress());
						client.configureBlocking(false);
						SelectionKey k = client.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						k.attach(buffer);
					} else if (key.isReadable()) {
						SocketChannel client = (SocketChannel)key.channel();
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						int bytesRead = client.read(buffer);
						if (bytesRead == -1) {
							key.cancel();
						} else {
							buffer.flip();
							CharBuffer cb = buffer.asCharBuffer();
							System.out.print("Client " + client.socket().getRemoteSocketAddress() + " ---> ");
							while(cb.hasRemaining())
								System.out.print(cb.get());
							System.out.println();
							buffer.clear();
						}
					} else if (key.isWritable()) {
						SocketChannel client = (SocketChannel)key.channel();
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						String s = new String("Greeting from server");
						for (int i = 0; i < s.length(); ++i) {
							buffer.putChar(s.charAt(i));
						}
						buffer.flip();
						client.write(buffer);
						
						buffer.flip();
						System.out.print("Client " + client.socket().getRemoteSocketAddress() + " <--- ");
						CharBuffer cb = buffer.asCharBuffer();
						while(cb.hasRemaining())
							System.out.print(cb.get());
						System.out.println();
						buffer.clear();
						
						key.cancel();
					} else {
						System.err.println("Something weird happened!");
						throw new IOException();
					}
				} catch (IOException e) {
					key.cancel();
					try {
						key.channel().close();
					} catch (IOException ex) {
						//do nothing
					}
				}
				
			} // end of while (it.hasNext())
		}// end of while(true)
		
	}

}
