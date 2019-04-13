package server.player;

import com.google.gson.JsonObject;
import server.messages.MessageFactory;
import server.messages.MessagePayloadValidator;
import server.board.Move;
import server.board.BoardField;
import server.gameRoom.GameRoomService;
import server.gameRoom.IGameRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
/*
* This class will handle clients connection
* Each new client after new connection will cause a new instance of PlayerService
* This class is responsible for handling clients requests, and creating
* and delegating IPlayer instances to selected gameRoom(s)
* */

public class PlayerService implements Runnable, IPlayerService{

    private int id;
    private GameRoomService gameRoomService;
    private IGameRoom currentGameRoom;
    private MessagePayloadValidator messagePayloadValidator;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private MessageFactory messageFactory;
    private String myPrefix = "\u001B[34m[PlayerService] \u001B[0m";
    private IPlayer myPlayer;
    private List<Integer> availableRoomTypes;

    public PlayerService(int id, GameRoomService gameRoomService, BufferedReader in, PrintWriter out){
        this.id = id;
        this.in = in;
        this.out = out;
        this.gameRoomService = gameRoomService;
        messageFactory = new MessageFactory();
        messagePayloadValidator = new MessagePayloadValidator();
        availableRoomTypes = createAvailableRoomTyped();
    }

    public int getID(){
        return id;
    }

    @Override
    public void sendMessage(String message){
        out.println(message);
    }

    private void sendInvalidFormatMessage() {
        sendMessage(messageFactory.invalidMessageFormat());
    }

    @Override
    public void itsYourTurn() {
        sendMessage(messageFactory.yourTurn());
    }

    @Override
    public void sendBoardUpdate(Move actualMove) {
        sendMessage(messageFactory.boardUpdate(actualMove));
    }

    @Override
    public void sendAfterMoveStillCanMove(List<BoardField> possibleMoves) {
        sendMessage(messageFactory.sendAfterMoveStillCanMove(possibleMoves));
    }

    @Override
    public void sendAfterMoveEndOfTurn() {
        sendMessage(messageFactory.sendAfterMoveEndOfTurn());
    }

    @Override
    public void sendEndOfGame(short place) {
        sendMessage(messageFactory.endOfGame(place));
    }

    @Override
    public void run() {
        log(String.format("Player service started, ID: %d", id));
        String currentLine;
        try {
            while ((currentLine = in.readLine()) != null){
                log(String.format("ID: %d client request: %s", id, currentLine));
                if (messagePayloadValidator.isRequestValid(currentLine))
                        takeAction(messagePayloadValidator.convertToJSONObject(currentLine));
                else
                    sendInvalidFormatMessage();
            }
            playerEscaped();
            log(String.format("Player service quit, ID: %d", id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void takeAction(JsonObject currentRequest) {
        log(String.format("ID: %d taking action", id));
        String requestAction = currentRequest.get("action").getAsString();
        switch (requestAction){
            case "HANDSHAKE": handshakeRequest();
                break;
            case "GET_ROOMS": getRoomsRequest();
                break;
            case "JOIN_ROOM": joinRoomRequest(currentRequest);
                break;
            case "CREATE_ROOM": createRoomRequest(currentRequest);
                break;
            case "MAKE_MOVE": makeMoveRequest(currentRequest);
                break;
            case "POSSIBLE_MOVES": possibleMovesRequest(currentRequest);
                break;
            case "QUIT_ROOM": quitRoomRequest();
                break;
            case "LEAVE_ROOM": leaveRoomRequest();
                break;
            case "PLAY_WITH_BOT": playWithBotRequest();
                break;
            default: sendInvalidFormatMessage();
        }
    }

    private void playWithBotRequest() {
        currentGameRoom.addBots();
    }

    private void quitRoomRequest() {
        myPlayer.leaveRoom();
        sendMessage(messageFactory.leftRoom());
    }

    private void handshakeRequest() {
        log(String.format("ID: %d handshake request", id));
        sendMessage(messageFactory.handshake(id));
    }

    private void getRoomsRequest() {
        log(String.format("ID: %d get_rooms request", id));
        sendMessage(messageFactory.getRooms(gameRoomService.getGameRooms()));
    }

    private void joinRoomRequest(JsonObject request){
        log(String.format("ID: %d join_room request", id));
        int roomIdToJoin = request.get("payload").getAsJsonObject().get("room_id").getAsInt();
        IGameRoom requestedRoom = gameRoomService.getGameRoom(roomIdToJoin);
        if (requestedRoom == null)
            sendMessage(messageFactory.error());
        else
            tryToJoinRoom(requestedRoom);
    }

    private void tryToJoinRoom(IGameRoom requestedRoom) {
        myPlayer = new Player(this);
        boolean isPlayerAdded = requestedRoom.addPlayerIfPossible(myPlayer);

        if (isPlayerAdded) {
            currentGameRoom = requestedRoom;
            int playersCorner = currentGameRoom.getCornerOfPlayer(myPlayer);
            sendMessage(messageFactory.joinedToRoom(playersCorner));
        }
        else
            sendMessage(messageFactory.fail());
    }

    private void createRoomRequest(JsonObject request){
        log(String.format("ID: %d create_room request", id));
        int roomType = request.get("payload").getAsJsonObject().get("room_type").getAsInt();
        if (availableRoomTypes.contains(roomType)) {
            int idOfNewGameRoom = gameRoomService.addGameRoomAngGetItsId(roomType);
            sendMessage(messageFactory.createdRoom(idOfNewGameRoom));
        }
        else
            sendMessage(messageFactory.error());
    }

    private void makeMoveRequest(JsonObject currentRequest) {
        log(String.format("ID: %d make_move request", id));
        if (currentGameRoom == null)
            sendMessage(messageFactory.fail());
        else {
            BoardField from = new BoardField(currentRequest.get("payload").getAsJsonObject().get("actual").getAsJsonObject().get("row").getAsByte(),
                    currentRequest.get("payload").getAsJsonObject().get("actual").getAsJsonObject().get("col").getAsByte());
            BoardField to = new BoardField(currentRequest.get("payload").getAsJsonObject().get("next").getAsJsonObject().get("row").getAsByte(),
                    currentRequest.get("payload").getAsJsonObject().get("next").getAsJsonObject().get("col").getAsByte());
            makeMove(from, to);
        }
    }

    private void makeMove(BoardField from, BoardField to) {
        myPlayer.setActualMove(new Move(from, to));
    }

    private void possibleMovesRequest(JsonObject currentRequest) {
        log(String.format("ID: %d possible_moves request", id));
        byte row = currentRequest.get("payload").getAsJsonObject().get("row").getAsByte();
        byte col = currentRequest.get("payload").getAsJsonObject().get("col").getAsByte();
        List<BoardField> possibleMoves =  currentGameRoom.possibleMoves(new BoardField(row, col));

        sendMessage(messageFactory.possibleMoves(possibleMoves));
    }

    private void leaveRoomRequest() {
        log(String.format("ID: %d leave_room request", id));
        if (myPlayer != null)
            myPlayer.setInactive();
        sendMessage(messageFactory.leftRoom());
    }

    private void playerEscaped() {
        if (myPlayer != null)
            myPlayer.setInactive();
    }


    private void log(String message) {
        System.out.println(myPrefix + message);
    }

    private List<Integer> createAvailableRoomTyped() {
        List<Integer> roomTypes = new ArrayList<>();
        roomTypes.add(2);
        roomTypes.add(3);
        roomTypes.add(4);
        roomTypes.add(6);
        return roomTypes;
    }
}
