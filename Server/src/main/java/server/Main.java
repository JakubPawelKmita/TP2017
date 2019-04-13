package server;

import server.gameRoom.GameRoomService;
import java.net.InetSocketAddress;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args){

        System.out.println("Yes, I'm MAVEN!!! and I work :) " + "\nI was tamed by Jakub, the Maven lord :)" );

        GameRoomService gameRoomService = GameRoomService.getInstance();
        String host = "127.0.0.1";
        if (args.length > 0)
            host = args[0];

        initializeAndRunServer(gameRoomService, host);

        //test change

    }

    private static void initializeAndRunServer(GameRoomService gameRoomService, String givenHost) {
        ServerSocket serverSocket = null;

        try {
            String host = givenHost;
            int port = 8080; //TODO hardcoded values, maybe read the from args, but in most cases it would be like that, so for easy testing leaving it as it is
            InetSocketAddress isa = new InetSocketAddress(host, port);
            serverSocket =  new ServerSocket();
            serverSocket.bind(isa);
        } catch(Exception exc) {
            exc.printStackTrace();
            System.exit(1);
        }

        new Thread(new Server(serverSocket, gameRoomService)).start();
    }
}
