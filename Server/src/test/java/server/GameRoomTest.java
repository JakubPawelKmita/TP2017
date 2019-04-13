package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.board.BoardFactory;
import server.board.BoardField;
import server.gameRoom.GameRoom;
import server.gameRoom.GameRoomService;
import server.gameRoom.IGameRoom;
import server.player.IPlayer;
import server.player.IPlayerService;
import server.player.Player;
import server.player.PlayerService;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameRoomTest {

    private IGameRoom gameRoom;
    private int roomId = 1;
    private int roomType = 4;
    private BoardFactory boardFactory;
    private BufferedReader in;
    private PrintWriter out;

    @Before
    public void setUp(){
        in = mock(BufferedReader.class);
        out = mock(PrintWriter.class);
        boardFactory = new BoardFactory();
        gameRoom = new GameRoom(roomId, roomType, boardFactory);
    }

    @Test
    public void getNumberOfPlayers() throws Exception {
        PlayerService playerService1 = mock(PlayerService.class);
        PlayerService playerService2 = mock(PlayerService.class);
        PlayerService playerService3 = mock(PlayerService.class);
        gameRoom.addPlayerIfPossible(new Player(playerService1));
        gameRoom.addPlayerIfPossible(new Player(playerService2));
        gameRoom.addPlayerIfPossible(new Player(playerService3));

        Assert.assertEquals(3, gameRoom.getNumberOfPlayers());
    }

    @Test
    public void addPlayer() throws Exception {
        PlayerService playerService1 = mock(PlayerService.class);

        Assert.assertEquals(0, gameRoom.getNumberOfPlayers());
        gameRoom.addPlayerIfPossible(new Player(playerService1));
        Assert.assertEquals(1, gameRoom.getNumberOfPlayers());
    }

    @Test
    public void getRoomType() throws Exception {
        Assert.assertEquals(roomType, gameRoom.getRoomType());
    }

    @Test
    public void isGameActiveWhenInstantiated() throws Exception {
        Assert.assertTrue(gameRoom.isGameActive());

    }

    @Test
    public void isGameActiveWhenDeactivated() throws Exception {
        gameRoom.setInactiveGame();
        Assert.assertFalse(gameRoom.isGameActive());

    }

    @Test
    public void deletePlayer() throws Exception{
        Socket socket = mock(Socket.class);
        GameRoomService gameRoomService = mock(GameRoomService.class);
        IPlayerService playerService = new PlayerService(1, gameRoomService, in, out);
        gameRoom.addPlayerIfPossible(new Player(playerService));

        IPlayerService playerService2 = new PlayerService(2, gameRoomService, in, out);
        gameRoom.addPlayerIfPossible(new Player(playerService2));

        Assert.assertEquals(2, gameRoom.getNumberOfPlayers());

        gameRoom.deletePlayer(1);
        Assert.assertEquals(1, gameRoom.getNumberOfPlayers());

        gameRoom.deletePlayer(99);
        Assert.assertEquals(1, gameRoom.getNumberOfPlayers());
    }

    @Test
    public void canIJoinWhenEmpty() throws Exception {
        Assert.assertTrue(gameRoom.canIJoin());
    }

    @Test
    public void canIJoinWhenFullByAsking() throws Exception {
        PlayerService playerService1 = mock(PlayerService.class);
        PlayerService playerService2 = mock(PlayerService.class);
        PlayerService playerService3 = mock(PlayerService.class);
        PlayerService playerService4 = mock(PlayerService.class);
        gameRoom.addPlayerIfPossible(new Player(playerService1));
        gameRoom.addPlayerIfPossible(new Player(playerService2));
        gameRoom.addPlayerIfPossible(new Player(playerService3));
        gameRoom.addPlayerIfPossible(new Player(playerService4));

        Assert.assertFalse(gameRoom.canIJoin());
    }

    @Test
    public void canIJoinWhenFullByAdding() throws Exception {
        PlayerService playerService1 = mock(PlayerService.class);
        PlayerService playerService2 = mock(PlayerService.class);
        PlayerService playerService3 = mock(PlayerService.class);
        PlayerService playerService4 = mock(PlayerService.class);
        gameRoom.addPlayerIfPossible(new Player(playerService1));
        gameRoom.addPlayerIfPossible(new Player(playerService2));
        gameRoom.addPlayerIfPossible(new Player(playerService3));
        gameRoom.addPlayerIfPossible(new Player(playerService4));

        PlayerService playerService5 = mock(PlayerService.class);
        Assert.assertFalse(gameRoom.addPlayerIfPossible(new Player(playerService5)));
    }

    @Test
    public void getCornerOfPlayer4PlayerBoard() {
        PlayerService playerService1 = mock(PlayerService.class);
        when(playerService1.getID()).thenReturn(0);
        IPlayer player = new Player(playerService1);
        gameRoom.addPlayerIfPossible(player);

        PlayerService playerService2 = mock(PlayerService.class);
        when(playerService2.getID()).thenReturn(1);
        IPlayer player2 = new Player(playerService2);
        gameRoom.addPlayerIfPossible(player2);

        PlayerService playerService3 = mock(PlayerService.class);
        when(playerService3.getID()).thenReturn(2);
        IPlayer player3 = new Player(playerService3);
        gameRoom.addPlayerIfPossible(player3);

        PlayerService playerService4 = mock(PlayerService.class);
        when(playerService4.getID()).thenReturn(3);
        IPlayer player4 = new Player(playerService4);
        gameRoom.addPlayerIfPossible(player4);

        Assert.assertEquals(1, gameRoom.getCornerOfPlayer(player));
        Assert.assertEquals(2, gameRoom.getCornerOfPlayer(player2));
        Assert.assertEquals(4, gameRoom.getCornerOfPlayer(player3));
        Assert.assertEquals(5, gameRoom.getCornerOfPlayer(player4));


    }

    @Test
    public void getCornerOfPlayer3PlayerBoard() {

        IGameRoom myGameRoom = new GameRoom(2, 3, boardFactory);

        PlayerService playerService1 = mock(PlayerService.class);
        when(playerService1.getID()).thenReturn(0);
        IPlayer player = new Player(playerService1);
        myGameRoom.addPlayerIfPossible(player);

        PlayerService playerService2 = mock(PlayerService.class);
        when(playerService2.getID()).thenReturn(1);
        IPlayer player2 = new Player(playerService2);
        myGameRoom.addPlayerIfPossible(player2);

        PlayerService playerService3 = mock(PlayerService.class);
        when(playerService3.getID()).thenReturn(2);
        IPlayer player3 = new Player(playerService3);
        myGameRoom.addPlayerIfPossible(player3);

        Assert.assertEquals(1, myGameRoom.getCornerOfPlayer(player));
        Assert.assertEquals(3, myGameRoom.getCornerOfPlayer(player2));
        Assert.assertEquals(5, myGameRoom.getCornerOfPlayer(player3));


    }

    @Test
    public void possibleMoves() throws Exception {
        List<BoardField> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardField((byte)4, (byte)12));
        possibleMoves.add(new BoardField((byte)5, (byte)12));

        List<BoardField> possibleMovesReturnedByMethod = gameRoom.possibleMoves(new BoardField((byte)4, (byte)13));

        Assert.assertTrue(possibleMovesReturnedByMethod.containsAll(possibleMoves));
    }

    @Test
    public void runTest() throws Exception {
        Assert.assertTrue(true);
    }

}