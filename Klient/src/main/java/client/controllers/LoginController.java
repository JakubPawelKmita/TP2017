package client.controllers;

import client.main.ServerConnector;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * Created by Jakub Kmita on 10.12.2017.
 */
public class LoginController {

    private ServerConnector serverConnector;
    private MainController mainController;

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private TextField serverAddress;

    @FXML
    private TextField username;

    @FXML
    public void loadMenuScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MenuScreen.fxml"));
        BorderPane menuBorderPane = null;
        try {
            menuBorderPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.setScreen(menuBorderPane);
        MenuController menuController = loader.getController();
        menuController.setMainController(mainController);
        menuController.setServerConnector(serverConnector);
        menuController.customize();
    }

    @FXML
    public void connectWithServer() {
        serverConnector = new ServerConnector(serverAddress.getText(), username.getText(), this);
        if (serverConnector.connect()) loadMenuScreen();
        else serverAddress.setText("Please, try with another address");
    }


}
