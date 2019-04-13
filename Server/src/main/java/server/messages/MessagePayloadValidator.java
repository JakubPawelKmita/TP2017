package server.messages;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MessagePayloadValidator {
    private List<String> validActionTypes;
    private List<String> requiredRequestKeys;

    public JsonObject convertToJSONObject(String message){
        Gson gson = new Gson();
        JsonObject body = gson.fromJson(message, JsonObject.class);
        return body;
    }

    public MessagePayloadValidator(){
        createValidActionTypesList();
        createRequiredRequestKeysList();
    }

    public boolean isRequestValid(String request){
        JsonObject body = convertToJSONObject(request);
        if (!doesRequestHaveAllRequiredKeys(body))
            return false;
        if (!isActionNameValid(body.get("action").getAsString()))
            return false;
        return true;
    }

    private boolean isActionNameValid(String actionName) {
        if (validActionTypes.contains(actionName.toUpperCase()))
            return true;
        else
            return false;
    }

    private boolean doesRequestHaveAllRequiredKeys(JsonObject body) {
        for (String key:requiredRequestKeys){
            if (body.get(key) == null)
                return false;
        }
        return true;
    }

    private void createValidActionTypesList() {
        validActionTypes = new ArrayList<>();
        validActionTypes.add("HANDSHAKE");
        validActionTypes.add("GET_ROOMS");
        validActionTypes.add("JOIN_ROOM");
        validActionTypes.add("CREATE_ROOM");
        validActionTypes.add("MAKE_MOVE");
        validActionTypes.add("POSSIBLE_MOVES");
        validActionTypes.add("LEAVE_ROOM");
        validActionTypes.add("QUIT_ROOM");
        validActionTypes.add("PLAY_WITH_BOT");
    }
    private void createRequiredRequestKeysList() {
        requiredRequestKeys = new ArrayList<>();
        requiredRequestKeys.add("action");
        requiredRequestKeys.add("client_name");
        requiredRequestKeys.add("payload");
    }
}
