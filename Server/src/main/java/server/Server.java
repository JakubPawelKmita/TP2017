package server;

import server.gameRoom.GameRoomService;
import server.player.PlayerService;

import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private String myPrefix = "\u001B[36m[Server] \u001B[0m"; // used to decorate some "logs" in console
    private ServerSocket serverSocket;
    private GameRoomService gameRoomService;

    public Server(ServerSocket serverSocket, GameRoomService gameRoomService) {
        this.serverSocket = serverSocket;
        this.gameRoomService = gameRoomService;
        System.out.println("Server started");
        System.out.println("listening at port: " + serverSocket.getLocalPort());
        System.out.println("bind address: " + serverSocket.getInetAddress());
    }


    public void run() {
        boolean serverRunning = true;
        int clientID = 0;
        ConnectionBuilder connectionBuilder = new ConnectionBuilder();
        while (serverRunning) {
            try {
                Socket socket = serverSocket.accept();
                logMessage("New client connected");

                connectionBuilder.createInputAndOutputStreams(socket);

                new Thread(new PlayerService(clientID, gameRoomService, connectionBuilder.getIn(),
                                                connectionBuilder.getOut())).start();
                clientID += 1;

            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        try { serverSocket.close(); } catch (Exception exc) {}
    }

    private void logMessage(String message) {
        System.out.println(myPrefix + message);
    }

}
