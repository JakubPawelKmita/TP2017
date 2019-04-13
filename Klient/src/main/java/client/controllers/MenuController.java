package client.controllers;

import client.main.ServerConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

/**
 * Created by Jakub Kmita on 10.12.2017.
 */
public class MenuController {

    @FXML
    private ListView<String> listOfRooms;
    @FXML
    private TextField console; //input window
    @FXML
    private Label connectionStatus, currentServerAddress, welcome; //status

    private ServerConnector serverConnector;
    void setServerConnector(ServerConnector serverConnector) {
        this.serverConnector = serverConnector;
    }
    private MainController mainController;

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    public void refreshRoomList() {
        listOfRooms.setItems(serverConnector.checkRooms());
    }

    @FXML
    public void joinRoom() {
        String ID = console.getText();
        if (!serverConnector.joinRoom(ID, mainController)) {
            console.setText("error");
        }
    }


    @FXML
    public void createRoom() {
        String s = console.getText();
        if (s.equals("2") || s.equals("3") || s.equals("4") || s.equals("6"))
            if (serverConnector.createRoom(s, mainController))
                console.setText("");
            else console.setText("Error");
        else console.setText("2/3/4/6");
        refreshRoomList();
    }

    @FXML
    public void backToLoginScreen() {
        mainController.loadLoginScreen();
    }

    void customize() {
        welcome.setText("Hello " + serverConnector.getUsername() + "!");
        refreshRoomList();
        currentServerAddress.setText(serverConnector.getServerAddress());
        connectionStatus.setText(serverConnector.getStatus());
    }

    public void autoJoin() {
        serverConnector.changeAutoJoin();
    }

    /* maybe later this will be added
    //use when setting connection in server connector
    public Label getConnectionStatus() {
        return connectionStatus;
    }
    */
}
