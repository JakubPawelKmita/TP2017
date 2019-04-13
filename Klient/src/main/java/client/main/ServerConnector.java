package client.main;

import client.controllers.LoginController;
import client.controllers.MainController;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Created by Jakub Kmita on 28.12.2017.
 */
public class ServerConnector implements Runnable {

    private boolean isAutoJoin = false;
    private final static int host = 8080;
    private String serverAddress;
    private String username;
    private String status = "not connected";
    private int myCorner;
    private Game game;
    private LoginController loginController;

    int getMyCorner() {
        return myCorner;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public String getUsername() {
        return username;
    }

    public ServerConnector(String address, String username, LoginController loginController) {
        this.serverAddress = address;
        this.username = username;
        this.loginController = loginController;
        game = null;
    }

    private BufferedReader input = null;
    private PrintWriter output = null;

    @Override
    public void run() {
        while (true) {
            JsonObject jsonMessage;
            try {
                jsonMessage = convertToJSONObject(input.readLine());
            } catch (IOException e) {
                loginController.loadMenuScreen();
                System.err.println("Connection broken");
                break;
            }
            if (inputTransmitter(jsonMessage)) break;
        }
    }

    private boolean inputTransmitter(JsonObject command) {
        switch (command.get("status").getAsString()) {
            case "YOUR_TURN":
                yourTurnSC();
                break;
            case "END_OF_TURN":
                endOfTurnSC();
                break;
            case "POSSIBLE_MOVES":
                possibleMovesSC(command);
                break;
            case "BOARD_UPDATE":
                boardUpdateSC(command);
                break;
            case "GAME_OVER":
                gameOverSC(command);
                break;
            case "LEFT_ROOM": {
                return true;
            }
            default:
                break;
        }
        return false;
    }

    private void yourTurnSC() {
        game.setMyTurn(true);
    }

    private void endOfTurnSC() {
        game.setMyTurn(false);
    }

    private void possibleMovesSC(JsonObject command) {
        game.setMyTurn(true);
        ArrayList<Pawn> possibleMoves = new ArrayList<>();
        JsonArray array = command.get("payload").getAsJsonArray();
        for (JsonElement e : array) {
            possibleMoves.add(new Pawn(e.getAsJsonObject().get("row").getAsInt(), e.getAsJsonObject().get("col").getAsInt()));
        }
        game.setPossibleMoves(possibleMoves);
    }

    private void boardUpdateSC(JsonObject command) {
        game.executeMove(
                command.get("payload").getAsJsonObject().get("actual").getAsJsonObject().get("row").getAsInt(),
                command.get("payload").getAsJsonObject().get("actual").getAsJsonObject().get("col").getAsInt(),
                command.get("payload").getAsJsonObject().get("next").getAsJsonObject().get("row").getAsInt(),
                command.get("payload").getAsJsonObject().get("next").getAsJsonObject().get("col").getAsInt());
    }

    private void gameOverSC(JsonObject command) {
        int place = command.get("payload").getAsJsonObject().get("your_place").getAsInt();
        game.setCommunicate("You're " + place);
    }

    //if connect will go ok, we will see menuScreen
    public boolean connect() {
        try {
            Socket socket = new Socket(serverAddress, host);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            status = "connected";
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getStatus() {
        return status;
    }

    public ObservableList<String> checkRooms() {
        output.println(RequestBuilder.genGetRoomsBody(username));
        ObservableList<String> list = FXCollections.observableArrayList();
        JsonObject jsonMessage = null;
        try {
            jsonMessage = convertToJSONObject(input.readLine());
        } catch (IOException e) {
            loginController.loadMenuScreen();
            System.err.println("Connection broken");
        }
        assert jsonMessage != null;
        JsonArray array = jsonMessage.get("payload").getAsJsonArray();
        for (JsonElement e : array)
            if (e.getAsJsonObject().get("nb_of_players").getAsInt() < e.getAsJsonObject().get("type").getAsInt()) {
                list.add("ID of a room: " + e.getAsJsonObject().get("id").getAsString() +
                        "  Players: " + e.getAsJsonObject().get("nb_of_players").getAsString() + " / " +
                        e.getAsJsonObject().get("type").getAsString());
                //Switch is not necessary, but it looks good
                switch (e.getAsJsonObject().get("type").getAsInt()) {
                    case 2:
                        list.add("Game type is DUEL");
                        break;
                    case 3:
                        list.add("Game type is FIGHT OF THREE");
                        break;
                    case 4:
                        list.add("Game type is FIGHT OF FOUR");
                        break;
                    case 6:
                        list.add("Game type is FULL");
                        break;
                    default:
                        list.add("Game type is unknown");
                        break;
                }
            }
        return list;
    }

    public Boolean createRoom(String roomType, MainController mainController) {
        output.println(RequestBuilder.genCreateRoomBody(username, roomType));
        Boolean value = false;
        JsonObject jsonMessage;
        try {
            jsonMessage = convertToJSONObject(input.readLine());
            if (jsonMessage.get("status").getAsString().equals("OK")) {
                value = true;
                if (isAutoJoin)
                    if (!joinRoom(jsonMessage.get("payload").getAsJsonObject().get("room_id").getAsString(), mainController))
                        value = false;
            }
        } catch (IOException e) {
            loginController.loadMenuScreen();
            System.err.println("Connection broken");
        }
        return value;
    }

    public boolean joinRoom(String ID, MainController mainController) {
        output.println(RequestBuilder.genJoinRoomBody(username, ID));
        JsonObject jsonMessage;
        try {
            jsonMessage = convertToJSONObject(input.readLine());
            if (jsonMessage.get("status").getAsString().equals("OK")) {
                myCorner = jsonMessage.get("payload").getAsJsonObject().get("your_corner").getAsInt();
                playGame(ID, mainController);
                return true;
            }
        } catch (IOException e) {
            loginController.loadMenuScreen();
            System.err.println("Connection broken");
        }
        return false;
    }

    int checkRoomType(String roomID) {
        output.println(RequestBuilder.genGetRoomsBody(username));
        //Simple Algorithm to check what kind of a room is with an ID
        try {
            JsonObject jsonMessage = convertToJSONObject(input.readLine());
            JsonArray array = jsonMessage.get("payload").getAsJsonArray();
            for (JsonElement e : array) {
                if (e.getAsJsonObject().get("id").getAsString().equals(roomID))
                    return e.getAsJsonObject().get("type").getAsInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    void checkPossibleMoves(Pawn touched) {
        output.println(RequestBuilder.genPossibleMovesBody(username, touched.getY(), touched.getX()));
    }

    void makeMove(Pawn actual, Pawn next) {
        output.println(RequestBuilder.genMakeMoveBody(username, actual.getY(), actual.getX(), next.getY(), next.getX()));
    }

    private JsonObject convertToJSONObject(String message) {
        Gson gson = new Gson();
        return gson.fromJson(message, JsonObject.class);
    }

    private void playGame(String ID, MainController mainController) {
        game = new Game(ID, this);
        game.setMainController(mainController);
        game.go();
    }

    public void changeAutoJoin() {
        isAutoJoin = !isAutoJoin;
    }

    void leaveRoom() {
        isAutoJoin = false;
        output.println(RequestBuilder.genLeaveRoomBody(username));
        loginController.loadMenuScreen();
    }

    void loadBots() {
        output.println(RequestBuilder.genLoadBots(username));
    }
}
