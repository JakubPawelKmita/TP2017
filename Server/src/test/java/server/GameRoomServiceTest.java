package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.gameRoom.GameRoomService;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class GameRoomServiceTest {
    private GameRoomService gameRoomService;

    @Before
    public void SetUp(){
        gameRoomService = GameRoomService.getInstance();
    }

    @Test
    public void addGameRoomAngGetItsIdTestIfAdded() throws Exception {
        int idOfNewRoom = gameRoomService.addGameRoomAngGetItsId(2);

        Assert.assertNotNull(gameRoomService.getGameRoom(idOfNewRoom));
    }

    @Test
    public void addGameRoomAngGetItsIdTestRoomType() throws Exception {
        byte roomType = 2;
        int idOfNewRoom = gameRoomService.addGameRoomAngGetItsId(roomType);

        Assert.assertEquals(roomType, gameRoomService.getGameRoom(idOfNewRoom).getRoomType());
    }

    @Test
    public void getGameRoomsTestNumberOfAddedRooms() throws Exception {
        byte roomType = 2;
        short numberOfRooms = 20;
        gameRoomService.deleteAllRooms();
        for (short i=0; i<numberOfRooms; i++)
            gameRoomService.addGameRoomAngGetItsId(roomType);

        Assert.assertEquals(numberOfRooms, gameRoomService.getGameRooms().size());
    }

    @Test
    public void getGameRoomTestIfRoomExist() throws Exception {
        byte roomType = 3;
        int idOfNewRoom = gameRoomService.addGameRoomAngGetItsId(roomType);

        Assert.assertNotNull(gameRoomService.getGameRoom(idOfNewRoom));
    }

    @Test
    public void getGameRoomTestTypeOfAddedRoom() throws Exception {
        byte roomType = 3;
        int idOfNewRoom = gameRoomService.addGameRoomAngGetItsId(roomType);

        Assert.assertEquals(roomType, gameRoomService.getGameRoom(idOfNewRoom).getRoomType());
    }

}