package client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.*;

import java.io.IOException;

/**
 * Created by Jakub Kmita on 10.12.2017.
 */
public class MainController {

    @FXML
    private StackPane mainStackPane;

    @FXML
    public void initialize() {
        loadLoginScreen();

    }

    void loadLoginScreen() {
        //We wan't to see LoginScreen on the MainScreen, so we are loading it
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/LoginScreen.fxml"));
        VBox vBox = null;
        try {
            vBox = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        LoginController loginController = loader.getController();
        loginController.setMainController(this);
        setScreen(vBox); //Now on the mainStackPane we will see LoginScreen
    }

    public void setScreen(Pane pane){
        mainStackPane.getChildren().clear();
        mainStackPane.getChildren().add(pane);
    }
}
