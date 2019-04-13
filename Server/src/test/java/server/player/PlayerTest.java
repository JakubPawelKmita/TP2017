package server.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.board.BoardField;
import server.board.Move;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PlayerTest {
    private IPlayerService playerService;
    private IPlayer player;
    private int id = 1;

    @Before
    public void SetUp(){
        playerService = mock(PlayerService.class);
        when(playerService.getID()).thenReturn(id);
        player = new Player(playerService);
    }

    @Test
    public void sendMessage() throws Exception {
        String message = "test";

        player.sendMessage(message);

        verify(playerService, times(1)).sendMessage(message);
    }

    @Test
    public void setActualMove() throws Exception {
        Move actualMove = new Move(new BoardField((byte)4, (byte)10), new BoardField((byte)4, (byte)12));

        player.setActualMove(actualMove);

        Assert.assertEquals(actualMove, player.getActualMove());
    }

    @Test
    public void getActualMove() throws Exception {
        Move actualMove = new Move(new BoardField((byte)5, (byte)11), new BoardField((byte)5, (byte)12));

        player.setActualMove(actualMove);

        Assert.assertEquals(actualMove, player.getActualMove());
    }

    @Test
    public void getID() throws Exception {
        Assert.assertEquals(id, player.getID());
        verify(playerService, times(1)).getID();
    }

    @Test
    public void itsYourTurn() throws Exception {
        player.itsYourTurn();

        verify(playerService, times(1)).itsYourTurn();
    }

    @Test
    public void setInactive() throws Exception {
        player.setInactive();

        Assert.assertFalse(player.isActive());
    }

    @Test
    public void sendBoardUpdate() throws Exception {
        Move update = new Move(new BoardField((byte)5, (byte)11), new BoardField((byte)5, (byte)12));

        player.sendBoardUpdate(update);

        verify(playerService, times(1)).sendBoardUpdate(update);
    }

    @Test
    public void sendAfterMoveStillCanMove() throws Exception {
        List<BoardField> possibleMoves = new ArrayList<>();
        possibleMoves.add(new BoardField((byte)4, (byte)12));
        possibleMoves.add(new BoardField((byte)5, (byte)12));

        player.sendAfterMoveStillCanMove(possibleMoves);

        verify(playerService, times(1)).sendAfterMoveStillCanMove(possibleMoves);
    }

    @Test
    public void sendAfterMoveEndOfTurn() throws Exception {
        player.sendAfterMoveEndOfTurn();

        verify(playerService, times(1)).sendAfterMoveEndOfTurn();
    }

    @Test
    public void sendEndOfGame() throws Exception {
        short place = 1;
        player.sendEndOfGame(place);

        verify(playerService, times(1)).sendEndOfGame(place);
    }

    @Test
    public void leaveRoom() throws Exception {
        player.leaveRoom();

        Assert.assertTrue(player.hasLeaved());
        Assert.assertFalse(player.isActive());
    }

    @Test
    public void hasLeaved() throws Exception {
        Assert.assertFalse(player.hasLeaved());

        player.leaveRoom();

        Assert.assertTrue(player.hasLeaved());
    }

    @Test
    public void isActive() throws Exception {
        Assert.assertTrue(player.isActive());

        player.setInactive();

        Assert.assertFalse(player.isActive());
    }

    @Test
    public void equals() throws Exception {
        Assert.assertTrue(player.equals(new Player(playerService)));
    }

}