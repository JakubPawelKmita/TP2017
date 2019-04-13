package server.gameRoom;

import server.board.BoardFactory;

import java.util.ArrayList;
import java.util.List;

public class GameRoomService {
    private List<IGameRoom> gameRooms;
    private String myPrefix = "\u001B[33m[GameRoomService] \u001B[0m";
    private int gameRoomCounter = 0;
    private static GameRoomService gameRoomService = null;

    private GameRoomService() {

        gameRooms = new ArrayList<>();
    }

    public static GameRoomService getInstance(){
        if (gameRoomService == null)
            gameRoomService = new GameRoomService();
        return gameRoomService;
    }

    public synchronized int addGameRoomAngGetItsId(int roomType) {
        IGameRoom newGameRoom = new GameRoom(gameRoomCounter, roomType, new BoardFactory());
        gameRooms.add(newGameRoom);
        new Thread(newGameRoom).start();
        logMessage(String.format("created new game room, type: %d id: %d", roomType, gameRoomCounter));
        return gameRoomCounter++;
    }

    public synchronized List<IGameRoom> getGameRooms(){
        return gameRooms;
    }

    public synchronized IGameRoom getGameRoom(int gameRoomId){
        return gameRooms
                .stream()
                .filter(s -> s.getId() == gameRoomId)
                .findFirst()
                .orElse(null);
    }

    public synchronized void deleteAllRooms(){
        gameRooms.clear();
    }

    private void logMessage(String message) {
        System.out.println(myPrefix + message);
    }
}