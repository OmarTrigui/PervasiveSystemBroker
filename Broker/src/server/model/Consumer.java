package server.model;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import server.event.callback.OnConsume;
import common.config.Configuration;

public class Consumer<T> implements Runnable {

	private int portNumber;

	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream ois;
	private T objectReceived;
	private boolean _continue = true;

	private OnConsume<T> onConsume;

	public Consumer(int portNumber) {
		this.portNumber = portNumber;
	}

	public void setOnConsume(OnConsume<T> onConsume) {
		this.onConsume = onConsume;
	}

	public void terminate() {
		_continue = false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			serverSocket = new ServerSocket(portNumber);
			serverSocket.setSoTimeout(Configuration.SOCKET_WAIT_TIMEOUT);

			while (_continue) {
				try {
					socket = serverSocket.accept();
					ois = new ObjectInputStream(socket.getInputStream());
					while (null != (objectReceived = (T) ois.readObject())) {
						String[] parts = socket.getRemoteSocketAddress()
								.toString().replace("/", "").split(":");
						onConsume.onConsume(objectReceived, parts[0]);
					}
				} catch (SocketTimeoutException | EOFException ex) {
				} catch (NullPointerException ex) {
					ex.printStackTrace();
				}
			}
			socket.close();
		} catch (NullPointerException ex) {
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}
	}
}
