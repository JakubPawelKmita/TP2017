package server.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import server.board.BoardField;
import server.board.Move;
import server.gameRoom.GameRoom;
import server.gameRoom.GameRoomService;
import server.messages.MessageFactory;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.ComparisonChain.start;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class PlayerServiceTest {
    private int id = 1;
    private IPlayerService playerService;
    private Socket connection;
    private GameRoomService gameRoomService;
    private BufferedReader in;
    private PrintWriter out;
    private BufferedReader inThread;
    private PrintWriter outThread;
    private MessageFactory messageFactory;

    @Before
    public void SetUp(){
        connection = mock(Socket.class);
        gameRoomService = mock(GameRoomService.class);
        in = mock(BufferedReader.class);
        out = mock(PrintWriter.class);
        inThread = mock(BufferedReader.class);
        outThread = mock(PrintWriter.class);
        playerService = new PlayerService(id, gameRoomService, in, out);
        messageFactory = new MessageFactory();
    }

    @Test
    public void getID() throws Exception {
        Assert.assertEquals(id, playerService.getID());
    }

    @Test
    public void sendMessage() throws Exception {
        String message = "test";
        playerService.sendMessage(message);

        verify(out, Mockito.times(1)).println(message);
    }

    @Test
    public void itsYourTurn() throws Exception {
        playerService.itsYourTurn();

        verify(out, Mockito.times(1)).println(messageFactory.yourTurn());
    }

    @Test
    public void sendBoardUpdate() throws Exception {
        Move update = new Move(new BoardField((byte)5, (byte)11), new BoardField((byte)5, (byte)12));

        playerService.sendBoardUpdate(update);

        verify(out, Mockito.times(1)).println(messageFactory.boardUpdate(update));
    }

    @Test
    public void sendAfterMoveStillCanMove() throws Exception {
        List<BoardField> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardField((byte)4, (byte)12));
        possibleMoves.add(new BoardField((byte)5, (byte)12));

        playerService.sendAfterMoveStillCanMove(possibleMoves);

        verify(out, Mockito.times(1)).println(messageFactory.sendAfterMoveStillCanMove(possibleMoves));
    }

    @Test
    public void sendAfterMoveEndOfTurn() throws Exception {
        playerService.sendAfterMoveEndOfTurn();

        verify(out, Mockito.times(1)).println(messageFactory.sendAfterMoveEndOfTurn());
    }

    @Test
    public void sendEndOfGame() throws Exception {
        short place = 1;
        playerService.sendEndOfGame(place);

        verify(out, Mockito.times(1)).println(messageFactory.endOfGame(place));
    }

    @Test
    public void runHandshake() throws Exception {
        String clientMessage = "{\"action\": \"HANDSHAKE\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";
        when(inThread.readLine()).thenReturn(clientMessage)
                .thenReturn(null);

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.handshake(id));
    }

    @Test
    public void runGetRooms() throws Exception {
        String clientMessage = "{\"action\": \"GET_ROOMS\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";
        when(inThread.readLine()).thenReturn(clientMessage)
                .thenReturn(null);
        when(gameRoomService.getGameRooms()).thenReturn(new ArrayList<>());

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.getRooms(gameRoomService.getGameRooms()));
    }

    @Test
    public void runJoinRoom() throws Exception {
        String clientMessage = "{\"action\": \"JOIN_ROOM\",\"client_name\": \"some_name\",\"payload\": {\"room_id\" : \"0\"}}";
        GameRoom gameRoom = mock(GameRoom.class);
        when(inThread.readLine()).thenReturn(clientMessage)
                .thenReturn(null);
        when(gameRoomService.getGameRoom(0)).thenReturn(gameRoom);
        when(gameRoom.addPlayerIfPossible(any())).thenReturn(true);
        int corner = 0;
        when(gameRoom.getCornerOfPlayer(any())).thenReturn(corner);

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.joinedToRoom(corner));
    }

    @Test
    public void runCreateRoom() throws Exception {
        int roomId = 0;
        String clientMessage = "{\"action\": \"CREATE_ROOM\",\"client_name\": \"some_name\",\"payload\": {\"room_type\": \"2\"}}";
        when(inThread.readLine()).thenReturn(clientMessage)
                .thenReturn(null);
        when(gameRoomService.addGameRoomAngGetItsId(2)).thenReturn(roomId);

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.createdRoom(roomId));
    }

    @Test
    public void runMakeMove() throws Exception {
        int roomId = 0;
        String clientMessage = "{\"action\": \"MAKE_MOVE\", \"client_name\": \"some_name\", \"payload\": {\"actual\": {\"row\": \"3\", \"col\": \"10\"},\"next\": {\"row\": \"4\", \"col\": \"10\"}}}";
        when(inThread.readLine()).thenReturn(clientMessage)
                .thenReturn(null);
        when(gameRoomService.addGameRoomAngGetItsId(2)).thenReturn(roomId);

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.fail());
    }

    @Test
    public void runPossibleMoves() throws Exception {
        List<BoardField> possibleMoves = new ArrayList<>();
        BoardField possibleMove = new BoardField((byte)1, (byte)2);
        possibleMoves.add(possibleMove);

        String clientMessage1 = "{\"action\": \"JOIN_ROOM\",\"client_name\": \"some_name\",\"payload\": {\"room_id\" : \"0\"}}";
        String clientMessage2 = "{\"action\": \"POSSIBLE_MOVES\", \"client_name\": \"some_name\", \"payload\": {\"row\": \"13\", \"col\": \"5\"}}";
        GameRoom gameRoom = mock(GameRoom.class);
        when(inThread.readLine()).thenReturn(clientMessage1)
                .thenReturn(clientMessage2)
                .thenReturn(null);
        when(gameRoomService.getGameRoom(0)).thenReturn(gameRoom);
        when(gameRoom.addPlayerIfPossible(any())).thenReturn(true);
        int corner = 0;
        when(gameRoom.getCornerOfPlayer(any())).thenReturn(corner);
        when(gameRoom.possibleMoves(any())).thenReturn(possibleMoves);

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.joinedToRoom(corner));
        verify(outThread, times(1)).println(messageFactory.possibleMoves(possibleMoves));
    }

    @Test
    public void runLeaveRoom() throws Exception {
        List<BoardField> possibleMoves = new ArrayList<>();
        BoardField possibleMove = new BoardField((byte)1, (byte)2);
        possibleMoves.add(possibleMove);

        String clientMessage1 = "{\"action\": \"JOIN_ROOM\",\"client_name\": \"some_name\",\"payload\": {\"room_id\" : \"0\"}}";
        String clientMessage2 = "{\"action\": \"LEAVE_ROOM\",\"client_name\": \"some_name\",\"payload\": \"empty_payload\"}";
        GameRoom gameRoom = mock(GameRoom.class);
        when(inThread.readLine()).thenReturn(clientMessage1)
                .thenReturn(clientMessage2)
                .thenReturn(null);
        when(gameRoomService.getGameRoom(0)).thenReturn(gameRoom);
        when(gameRoom.addPlayerIfPossible(any())).thenReturn(true);
        int corner = 0;
        when(gameRoom.getCornerOfPlayer(any())).thenReturn(corner);
        when(gameRoom.possibleMoves(any())).thenReturn(possibleMoves);

        new PlayerService(id, gameRoomService, inThread, outThread).run();

        verify(outThread, times(1)).println(messageFactory.joinedToRoom(corner));
        verify(outThread, times(1)).println(messageFactory.leftRoom());
    }

}