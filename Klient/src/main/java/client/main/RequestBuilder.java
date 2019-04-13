package client.main;

/**
 * Created by Jakub Kmita on 06.01.2018.
 */
class RequestBuilder {
    private RequestBuilder() { }

    static String genLeaveRoomBody(String user){
        return "{\"action\": \"LEAVE_ROOM\",\"client_name\": \""+user+"\",\"payload\": \"\" }";
    }

    static String genMakeMoveBody(String user, int aY, int aX, int bY, int bX){
        return "{\"action\": \"MAKE_MOVE\"," +
                "\"client_name\": \"" + user +
                "\",\"payload\": {\"actual\": {\"row\": \"" + aY + "\", \"col\": \"" + aX +
                "\"},\"next\": {\"row\": \""+bY+"\", \"col\": \"" + bX + "\"} }}";
    }

    static String genPossibleMovesBody(String user, int tY, int tX){
        return "{\"action\": \"POSSIBLE_MOVES\"," +
                "\"client_name\": \"" + user +
                "\",\"payload\": {\"row\": \"" + tY + "\",\"col\": \"" + tX + "\" } }";
    }

    static String genGetRoomsBody(String user){
        return "{\"action\": \"GET_ROOMS\",\"client_name\": \""
                + user + "\",\"payload\": \"empty_payload\"}";
    }

    static String genJoinRoomBody(String user, String ID){
        return "{\"action\": \"JOIN_ROOM\",\"client_name\": \""
                + user + "\",\"payload\": {\"room_id\" : \"" + ID + "\"}}";
    }

    static String genCreateRoomBody(String user, String type){
        return "{\"action\": \"CREATE_ROOM\",\"client_name\": \""
                + user + "\",\"payload\": {\"room_type\": \"" + type + "\"}}";
    }

    static String genLoadBots(String user) {
        return  "{\"action\": \"PLAY_WITH_BOT\",\"client_name\": \""+user+"\",\"payload\": \"empty_payload\"}";
    }
}
