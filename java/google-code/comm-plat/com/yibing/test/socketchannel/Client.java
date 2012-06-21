package com.yibing.test.socketchannel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SocketChannel;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LinkedList<FutureTask<String>> taskList = new LinkedList<FutureTask<String>>();
		for (int i = 0; i < 3; ++i) {
			ClientTask ct = new ClientTask();
			FutureTask<String> ft = new FutureTask<String>(ct);
			taskList.add(ft);
			new Thread(ft).start();
		}
		
		ListIterator<FutureTask<String>> it = taskList.listIterator();
		while (it.hasNext()) {
			try {
				System.out.println(it.next().get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}
	
}

class ClientTask implements Callable<String> {

	@Override
	public String call() throws Exception {
		StringBuilder result = null;
		try {
			InetSocketAddress addr = new InetSocketAddress("127.0.0.1", 11009);
			SocketChannel sc = SocketChannel.open(addr);
			
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			String msg = new String("Hello from client!");
			for (int i = 0; i < msg.length(); ++i) {
				buffer.putChar(msg.charAt(i));
			}
			buffer.flip();
			int bytesWritten = sc.write(buffer);
			
			buffer.clear();
			int bytesRead = sc.read(buffer);
			sc.close();			
			result = new StringBuilder("Send " + bytesWritten + "\nReceive " + bytesRead + " bytes from server: ");
			buffer.flip();
			CharBuffer cb = buffer.asCharBuffer();
			for (int i = 0; i < cb.length(); ++i) {
				result.append(cb.charAt(i));
			}
			
            Thread.sleep(100);
			sc.close();			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result.toString();
	}
	
}
