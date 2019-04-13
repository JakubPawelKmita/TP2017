package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.board.*;

public class BoardFactoryTest {
    private BoardFactory boardFactory;

    @Before
     public void SetUp(){
        boardFactory = new BoardFactory();
    }


    @Test
    public void getGameRoomofType2() throws Exception {
        Assert.assertTrue(boardFactory.getGameRoom(2) instanceof BoardFor2Players);
    }

    @Test
    public void getGameRoomofType3() throws Exception {
        Assert.assertTrue(boardFactory.getGameRoom(3) instanceof BoardFor3Players);
    }

    @Test
    public void getGameRoomofType4() throws Exception {
        Assert.assertTrue(boardFactory.getGameRoom(4) instanceof BoardFor4Players);
    }

    @Test
    public void getGameRoomofType6() throws Exception {
        Assert.assertTrue(boardFactory.getGameRoom(6) instanceof BoardFor6Players);
    }

}