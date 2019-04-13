package client.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Created by Jakub Kmita on 10.12.2017.
 */
public class Main extends Application{

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/MainScreen.fxml"));
        StackPane stackPane = loader.load();
        Scene scene = new Scene(stackPane, 726, 624);
        primaryStage.setScene(scene);
        primaryStage.setTitle("The Trylma Game");
        primaryStage.show();
        primaryStage.setOnCloseRequest(we -> System.exit(0));
    }
}
