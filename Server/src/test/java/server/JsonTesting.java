package server;

import org.junit.Test;
import server.board.BoardField;
import server.messages.MessageFactory;
import server.messages.MessagePayloadValidator;

import java.util.ArrayList;
import java.util.List;

public class JsonTesting {

    @Test
    public void testing(){
        MessagePayloadValidator mpv = new MessagePayloadValidator();
        String m = "{\"action\": \"MAKE_MOVE\",\"client_name\": \"some_name\",\"payload\": {\"actual\": {\"row\": \"row_value\", \"col\": \"col_value\"},\"next\": {\"row\": \"row_value\", \"col\": \"col_value\"}}}";

        System.out.println(mpv.convertToJSONObject(m).get("payload").getAsJsonObject().get("actual").getAsJsonObject().get("row"));
    }


    @Test
    public void testing2(){
        MessagePayloadValidator mpv = new MessagePayloadValidator();
        String m = "{\"action\": \"JOIN_ROOM\",\"client_name\": \"some_name\",\"payload\": {\"room_id\" : \"some_id\"}}";

        System.out.println(mpv.convertToJSONObject(m).get("payload").getAsJsonObject().get("room_id"));
    }

    @Test
    public void testing3(){
        MessageFactory mf = new MessageFactory();

        List<BoardField> list = new ArrayList<>();

        list.add(new BoardField((byte)1,(byte)2));
        list.add(new BoardField((byte)2,(byte)3));
        list.add(new BoardField((byte)3,(byte)4));
        list.add(new BoardField((byte)4,(byte)5));

        System.out.println(mf.possibleMoves(list));
    }
}
