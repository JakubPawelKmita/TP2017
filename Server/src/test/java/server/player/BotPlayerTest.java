package server.player;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.gameRoom.GameRoom;
import server.gameRoom.IGameRoom;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class BotPlayerTest {
    private IPlayer bot;
    IGameRoom gameRoom;

    @Before
    public void setUp(){
        gameRoom = mock(GameRoom.class);
        bot = new BotPlayer((byte)1, gameRoom);
    }

    @Test
    public void getID() throws Exception {
        Assert.assertEquals(11711, bot.getID());
    }

    @Test
    public void setInactive() throws Exception {
        bot.setInactive();
        Assert.assertFalse(bot.isActive());
    }

    @Test
    public void leaveRoom() throws Exception {
        Assert.assertTrue(true);
    }

    @Test
    public void hasLeaved() throws Exception {
        Assert.assertFalse(bot.hasLeaved());
    }

    @Test
    public void isActive() throws Exception {
        Assert.assertTrue(bot.isActive());
    }

    @Test
    public void sendMessage() throws Exception {
        Assert.assertTrue(true);
    }

}