package server.messages;

import server.board.Move;
import server.board.BoardField;
import server.gameRoom.IGameRoom;

import java.util.List;

public class MessageFactory {

    public String handshake(int clientID){
        String message = String.format("{\"status\": \"OK\",\"payload\": {\"client_id\" : \"%d\"}}",clientID);
        return message;
    }

    public String getRooms(List<IGameRoom> rooms){
        String messageWithoutRooms = "{\"status\": \"OK\",\"payload\": [%s]}";
        String singleRoomEntry = "{\"id\": \"%d\", \"type\": \"%d\", \"nb_of_players\": \"%d\"},";
        String allRooms = "";
        for (IGameRoom gr : rooms)
            allRooms = allRooms.concat(String.format(singleRoomEntry, gr.getId(), gr.getRoomType(), gr.getNumberOfPlayers()));
        if (allRooms.length() > 0)
            allRooms = allRooms.substring(0,allRooms.length()-1);

        return String.format(messageWithoutRooms, allRooms);
    }

    public String ok(){
        return "{\"status\": \"OK\",\"payload\": \"\"}";
    }

    public String fail(){
        return "{\"status\": \"FAIL\",\"payload\": \"\"}";
    }

    public String error(){
        return "{\"status\": \"ERROR\",\"payload\": \"\"}";
    }


    public String createdRoom(int idOfNewGameRoom) {
        return String.format("{\"status\": \"OK\",\"payload\": {\"room_id\": \"%d\"}}", idOfNewGameRoom);
    }

    public String yourTurn() {
        return "{\"status\": \"YOUR_TURN\",\"payload\": \"\"}";
    }

    public String possibleMoves(List<BoardField> possibleMoves) {
        return getMoves(possibleMoves);
    }

    public String invalidMessageFormat() {
        return "{\"status\": \"ERROR\",\"payload\": \"invalid message format\"}";
    }

    public String boardUpdate(Move actualMove) {
        String messageWithoutChange = "{\"status\": \"BOARD_UPDATE\",\"payload\": {%s, %s}}";
        String payloadActual = "\"actual\": {\"row\": \"%d\", \"col\": \"%d\"}";
        String payloadNext =  "\"next\": {\"row\": \"%d\", \"col\": \"%d\"}";

        payloadActual = String.format(payloadActual, actualMove.getFromField().getRow(), actualMove.getFromField().getCol());
        payloadNext = String.format(payloadNext, actualMove.getToField().getRow(), actualMove.getToField().getCol());

        return(String.format(messageWithoutChange, payloadActual, payloadNext));
    }

    public String joinedToRoom(int cornerNumber) {
        return String.format("{\"status\": \"OK\",\"payload\": {\"your_corner\": \"%d\"}}", cornerNumber);
    }

    public String sendAfterMoveStillCanMove(List<BoardField> possibleMoves) {
        return getMoves(possibleMoves);
    }

    public String sendAfterMoveEndOfTurn() {
        return "{\"status\": \"END_OF_TURN\",\"payload\": \"\"}";
    }

    public String endOfGame(short place) {
        String message = "{\"status\": \"GAME_OVER\",\"payload\": {\"your_place\": \"%d\"}}";
        return String.format(message, place);
    }

    public String leftRoom() {
        return "{\"status\": \"LEFT_ROOM\",\"payload\": \"\"}";
    }

    private String getMoves(List<BoardField> possibleMoves) {
        String messageWithoutMoves = "{\"status\": \"POSSIBLE_MOVES\",\"payload\": [%s]}";
        String oneMove = "{\"row\": \"%d\", \"col\": \"%d\"},";
        String allMoves = "";

        for (BoardField bf : possibleMoves){
            allMoves = allMoves.concat(String.format(oneMove, bf.getRow(), bf.getCol()));
        }

        if (allMoves.length() > 0)
            allMoves = allMoves.substring(0,allMoves.length()-1);

        return String.format(messageWithoutMoves, allMoves);
    }
}
