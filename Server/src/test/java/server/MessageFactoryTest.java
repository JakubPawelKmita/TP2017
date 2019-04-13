package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.board.BoardFactory;
import server.board.BoardField;
import server.board.Move;
import server.gameRoom.GameRoom;
import server.gameRoom.IGameRoom;
import server.messages.MessageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

public class MessageFactoryTest {
    private MessageFactory messageFactory;

    @Before
    public void setUp(){
        messageFactory = new MessageFactory();
    }

    @Test
    public void handshake(){
        int clientId = 1;
        String correctMessage = String.format("{\"status\": \"OK\",\"payload\": {\"client_id\" : \"%d\"}}", clientId);
        Assert.assertEquals(correctMessage, messageFactory.handshake(clientId));
    }

    @Test
    public void getRooms(){
        List<IGameRoom> rooms = new ArrayList<IGameRoom>();
        BoardFactory boardFactory = new BoardFactory();
        rooms.add(new GameRoom(1,4, boardFactory));
        rooms.add(new GameRoom(2,6, boardFactory));

        String correctMessage = "{\"status\": \"OK\",\"payload\": [{\"id\": \"1\", \"type\": \"4\", \"nb_of_players\": \"0\"},{\"id\": \"2\", \"type\": \"6\", \"nb_of_players\": \"0\"}]}";

        Assert.assertEquals(correctMessage, messageFactory.getRooms(rooms));
    }

    @Test
    public void boardUpdate(){
        int clientId = 1;
        String correctMessage = "{\"status\": \"BOARD_UPDATE\",\"payload\": {\"actual\": {\"row\": \"5\", \"col\": \"5\"}, \"next\": {\"row\": \"6\", \"col\": \"5\"}}}";

        Move move = new Move(new BoardField((byte)5, (byte)5), new BoardField((byte)6, (byte)5));

        Assert.assertEquals(correctMessage, messageFactory.boardUpdate(move));

    }

    @Test
    public void okMessage(){
        String correctMessage = "{\"status\": \"OK\",\"payload\": \"\"}";

        Assert.assertEquals(correctMessage, messageFactory.ok());

    }

    @Test
    public void failMessage(){
        String correctMessage = "{\"status\": \"FAIL\",\"payload\": \"\"}";

        Assert.assertEquals(correctMessage, messageFactory.fail());

    }

    @Test
    public void errorMessage() {
        String correctMessage = "{\"status\": \"ERROR\",\"payload\": \"\"}";

        Assert.assertEquals(correctMessage, messageFactory.error());
    }

    @Test
    public void createdRoom(){
        int roomId = 1;
        String correctMessageWithoutRoom = "{\"status\": \"OK\",\"payload\": {\"room_id\": \"%d\"}}";
        String correctMessage = String.format(correctMessageWithoutRoom, roomId);

        Assert.assertEquals(correctMessage, messageFactory.createdRoom(roomId));
    }

    @Test
    public void yourTurn(){
        String correctMessage = "{\"status\": \"YOUR_TURN\",\"payload\": \"\"}";

        Assert.assertEquals(correctMessage, messageFactory.yourTurn());
    }

    @Test
    public void joinedRoom(){
        int corner = 1;
        String correctMessageWithoutCorner = "{\"status\": \"OK\",\"payload\": {\"your_corner\": \"%d\"}}";
        String correctMessage = String.format(correctMessageWithoutCorner, corner);

        Assert.assertEquals(correctMessage, messageFactory.joinedToRoom(corner));
    }

    @Test
    public void sendAfterMoveEndOfTurn(){
        String correctMessage = "{\"status\": \"END_OF_TURN\",\"payload\": \"\"}";

        Assert.assertEquals(correctMessage, messageFactory.sendAfterMoveEndOfTurn());
    }

    @Test
    public void endOfGame(){
        short place = 1;
        String correctMessageWithoutPlace = "{\"status\": \"GAME_OVER\",\"payload\": {\"your_place\": \"%d\"}}";
        String correctMessage = String.format(correctMessageWithoutPlace, place);

        Assert.assertEquals(correctMessage, messageFactory.endOfGame(place));
    }

    @Test
    public void leavedRoom(){
        String correctMessage = "{\"status\": \"LEFT_ROOM\",\"payload\": \"\"}";

        Assert.assertEquals(correctMessage, messageFactory.leftRoom());
    }

    @Test
    public void invalidMessageFormat(){
        String correctMessage = "{\"status\": \"ERROR\",\"payload\": \"invalid message format\"}";

        Assert.assertEquals(correctMessage, messageFactory.invalidMessageFormat());
    }

    @Test
    public void sendAfterMoveStillCanMove(){
        List<BoardField> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardField((byte)4, (byte)12));
        possibleMoves.add(new BoardField((byte)4, (byte)10));

        String correctMessage = "{\"status\": \"POSSIBLE_MOVES\",\"payload\": [{\"row\": \"4\", \"col\": \"12\"},{\"row\": \"4\", \"col\": \"10\"}]}";

        Assert.assertEquals(correctMessage, messageFactory.sendAfterMoveStillCanMove(possibleMoves));
    }

}