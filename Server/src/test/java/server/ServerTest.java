package server;

import org.junit.Before;
import org.junit.Test;
import server.gameRoom.GameRoomService;

import java.net.InetSocketAddress;
import java.net.ServerSocket;

import static org.junit.Assert.*;

public class ServerTest {

    @Before
    public void setUp(){
    }

    @Test
    public void run() throws Exception {
        GameRoomService gameRoomService = GameRoomService.getInstance();
        ServerSocket serverSocket = null;

        try {
            String host = "127.0.0.1";
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