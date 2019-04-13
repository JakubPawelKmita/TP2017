package client.main;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jakub Kmita on 01.01.2018.
 */
public class RequestBuilderTest {
    private String username = "BOB";
    private int aY=1;
    private int aX=2;


    @Test
    public void genLeaveRoomBody() throws Exception {
        String s = RequestBuilder.genLeaveRoomBody(this.username);
        assertEquals("{\"action\": \"LEAVE_ROOM\",\"client_name\": \"BOB\",\"payload\": \"\" }",
               s );
    }

    @Test
    public void genMakeMoveBody() throws Exception {
        String a = "{\"action\": \"MAKE_MOVE\"," +
                "\"client_name\": \"BOB\",\"payload\": {\"actual\": {\"row\": \"1\", \"col\": \"2\"},\"next\": {\"row\": \"3\", \"col\": \"4\"} }}";
        int bY = 3;
        int bX = 4;
        String b = RequestBuilder.genMakeMoveBody(username, aY, aX, bY, bX);
        assertEquals(a,b);
    }

    @Test
    public void genPossibleMovesBody() throws Exception {
        String a ="{\"action\": \"POSSIBLE_MOVES\"," +
                "\"client_name\": \"BOB\",\"payload\": {\"row\": \"1\",\"col\": \"2\" } }";
                String b =RequestBuilder.genPossibleMovesBody(username, aY, aX);
                assertEquals(a,b);
    }

    @Test
    public void genGetRoomsBody() throws Exception {
        String a ="{\"action\": \"GET_ROOMS\",\"client_name\": \"BOB\",\"payload\": \"empty_payload\"}";
        String b = RequestBuilder.genGetRoomsBody(username);
        assertEquals(a,b);
    }

    @Test
    public void genJoinRoomBody() throws Exception {
        String a = "{\"action\": \"JOIN_ROOM\",\"client_name\": \"BOB\",\"payload\": {\"room_id\" : \"0\"}}";
        String b = RequestBuilder.genJoinRoomBody(username, "0");
        assertEquals(a,b);
    }

    @Test
    public void genCreateRoomBody() throws Exception {
        String a = "{\"action\": \"CREATE_ROOM\",\"client_name\": \"BOB\",\"payload\": {\"room_type\": \"0\"}}";
        String b = RequestBuilder.genCreateRoomBody(username, "0");
        assertEquals(a,b);
    }

}