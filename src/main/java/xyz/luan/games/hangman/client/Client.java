package xyz.luan.games.hangman.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Platform;
import lombok.Getter;
import lombok.Setter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.luan.games.hangman.client.scenes.ClientScene;
import xyz.luan.games.hangman.game.ConfigManager;
import xyz.luan.games.hangman.game.Main;
import xyz.luan.games.hangman.game.MainGameStatus;
import xyz.luan.games.hangman.game.forms.DialogHelper;
import xyz.luan.games.hangman.messaging.client.ClientMessage;
import xyz.luan.games.hangman.messaging.server.ServerMessage;

public class Client extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(Client.class);

	private ObjectInputStream in;
	private ObjectOutputStream out;
	private boolean running;

	@Setter
	private Main mainRef;

	@Getter
	private ClientData data;

	public Client(String ip) throws IOException {
		this(getSocket(ip));
	}

	public Client(Socket socket) throws IOException {
		this.out = new ObjectOutputStream(socket.getOutputStream());
		this.in = new ObjectInputStream(socket.getInputStream());
		this.running = true;
	}

	@Override
	public void run() {
		while (running) {
			try {
				processMessage(readMessage());
			} catch (ServerDownException quitted) {
				exit();
			}
		}
	}

	public void setMode(ClientStatus mode) {
		this.mainRef.setStatus(mode);
	}

	private void processMessage(ServerMessage message) {
		Platform.runLater(() -> {
			message.handle(this);
			mainRef.getScene().consume(message);
		});
	}

	public void sendMessage(ClientMessage message) {
		try {
			out.reset();
			out.writeObject(message);
		} catch (IOException e) {
			throw new RuntimeException(e); // TODO handle properly
		}
	}

	private ServerMessage readMessage() throws ServerDownException {
		try {
			return (ServerMessage) in.readObject();
		} catch (EOFException | SocketException clientQuitted) {
			throw new ServerDownException();
		} catch (ClassNotFoundException | IOException e) {
			throw new RuntimeException(e); // TODO handle properly
		}
	}

	private class ServerDownException extends Exception {
	}

	private static Socket getSocket(String ip) throws UnknownHostException, IOException {
		String[] parts = ip.split(":");
		if (parts.length > 2) {
			throw new IOException("Invalid host! Cannot have more than one ':' in the ip address.");
		}
		String host = parts[0];
		int port;
		if (parts.length > 1) {
			try {
				port = Integer.parseInt(parts[1]);
			} catch (NumberFormatException ex) {
				throw new IOException("Invalid host! Port must be a number.");
			}
		} else {
			port = ConfigManager.general.config().getPort();
		}
		return new Socket(host, port);
	}

	public ClientScene getCurrentScene() {
		return (ClientScene) mainRef.getScene();
	}

	public void exit() {
		dispose();
		Platform.runLater(() -> {
			mainRef.setStatus(MainGameStatus.CONNECT_TO_SERVER);
			DialogHelper.show("Error", "Server is down!"); // TODO i18n
		});
	}

	public void dispose() {
		this.running = false;
		try {
			this.in.close();
			this.out.close();
		} catch (IOException e) {
			logger.error("Unable to close connection", e);
		}
	}

	public void setData(ClientData data) {
		this.data = data;
	}
}
