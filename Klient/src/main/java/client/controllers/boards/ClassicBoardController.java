package client.controllers.boards;

import client.boardsDiagrams.Color;
import client.boardsDiagrams.Corner;
import client.main.Game;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.VBox;

/**
 * Created by Jakub Kmita on 10.12.2017.
 */
public class ClassicBoardController {

    @FXML
    private VBox rotatingPanel;

    @FXML
    private Button
            x12y00,
            x11y01, x12y01,
            x10y02, x11y02, x12y02,
            x09y03, x10y03, x11y03, x12y03,
            x04y04, x05y04, x06y04, x07y04, x08y04, x09y04, x10y04, x11y04, x12y04, x13y04, x14y04, x15y04, x16y04,
            x04y05, x05y05, x06y05, x07y05, x08y05, x09y05, x10y05, x11y05, x12y05, x13y05, x14y05, x15y05,
            x04y06, x05y06, x06y06, x07y06, x08y06, x09y06, x10y06, x11y06, x12y06, x13y06, x14y06,
            x04y07, x05y07, x06y07, x07y07, x08y07, x09y07, x10y07, x11y07, x12y07, x13y07,
            x04y08, x05y08, x06y08, x07y08, x08y08, x09y08, x10y08, x11y08, x12y08,
            x03y09, x04y09, x05y09, x06y09, x07y09, x08y09, x09y09, x10y09, x11y09, x12y09,
            x02y10, x03y10, x04y10, x05y10, x06y10, x07y10, x08y10, x09y10, x10y10, x11y10, x12y10,
            x01y11, x02y11, x03y11, x04y11, x05y11, x06y11, x07y11, x08y11, x09y11, x10y11, x11y11, x12y11,
            x00y12, x01y12, x02y12, x03y12, x04y12, x05y12, x06y12, x07y12, x08y12, x09y12, x10y12, x11y12, x12y12,
            x04y13, x05y13, x06y13, x07y13,
            x04y14, x05y14, x06y14,
            x04y15, x05y15,
            x04y16;

    @FXML
    private Button colorButon;

    public void setColorButon(Color color) {
        colorButon.setStyle("-fx-background-radius: 20; -fx-background-color: " + color.toString());
        colorButon.setEffect(lighting);
    }

    @FXML
    private Label communicate;
    //Lambda, because there is a bug with Labels in javafx
    public void setCommunicate(String value) {
        Platform.runLater(() -> communicate.setText(value));
    }

    @FXML
    public void skipMove() {
        game.skipMove();
    }

    @FXML
    public void leaveRoom() {
        game.leaveRoom();
    }

    private Lighting lighting = new Lighting();

    private Game game;

    public void setGame(Game game) {
        this.game = game;
    }

    public void initialize() {
        communicate.setText("Wait...");
        //constructing button's array, needed to operate on x and y when animating a move
        board = new Button[][]{
                {null, null, null, null, null, null, null, null, null, null, null, null, x12y00},
                {null, null, null, null, null, null, null, null, null, null, null, x11y01, x12y01},
                {null, null, null, null, null, null, null, null, null, null, x10y02, x11y02, x12y02},
                {null, null, null, null, null, null, null, null, null, x09y03, x10y03, x11y03, x12y03},
                {null, null, null, null, x04y04, x05y04, x06y04, x07y04, x08y04, x09y04, x10y04, x11y04, x12y04, x13y04, x14y04, x15y04, x16y04},
                {null, null, null, null, x04y05, x05y05, x06y05, x07y05, x08y05, x09y05, x10y05, x11y05, x12y05, x13y05, x14y05, x15y05},
                {null, null, null, null, x04y06, x05y06, x06y06, x07y06, x08y06, x09y06, x10y06, x11y06, x12y06, x13y06, x14y06},
                {null, null, null, null, x04y07, x05y07, x06y07, x07y07, x08y07, x09y07, x10y07, x11y07, x12y07, x13y07},
                {null, null, null, null, x04y08, x05y08, x06y08, x07y08, x08y08, x09y08, x10y08, x11y08, x12y08},
                {null, null, null, x03y09, x04y09, x05y09, x06y09, x07y09, x08y09, x09y09, x10y09, x11y09, x12y09},
                {null, null, x02y10, x03y10, x04y10, x05y10, x06y10, x07y10, x08y10, x09y10, x10y10, x11y10, x12y10},
                {null, x01y11, x02y11, x03y11, x04y11, x05y11, x06y11, x07y11, x08y11, x09y11, x10y11, x11y11, x12y11},
                {x00y12, x01y12, x02y12, x03y12, x04y12, x05y12, x06y12, x07y12, x08y12, x09y12, x10y12, x11y12, x12y12},
                {null, null, null, null, x04y13, x05y13, x06y13, x07y13},
                {null, null, null, null, x04y14, x05y14, x06y14},
                {null, null, null, null, x04y15, x05y15},
                {null, null, null, null, x04y16}
        };
    }

    @FXML
    public void fieldOnAction(ActionEvent actionEvent) {
        int x, y;
        String name = actionEvent.getSource().toString();
        x = Integer.parseInt(name.substring(11, 13));
        y = Integer.parseInt(name.substring(14, 16));
        game.touchField(y, x);
    }

    private Button[][] board;

    public void showMove(int aX, int aY, int bX, int bY) {
        //TODO animation maybe :)
        String aStyle = board[aY][aX].getStyle();
        String bStyle = board[bY][bX].getStyle();
        Effect aEffect = board[aY][aX].getEffect();
        Effect bEffect = board[bY][bX].getEffect();
        board[aY][aX].setEffect(bEffect);
        board[bY][bX].setEffect(aEffect);
        board[aY][aX].setStyle(bStyle);
        board[bY][bX].setStyle(aStyle);
    }

    private void setColourOnButton(int y, int x, Color color) {
        board[y][x].setStyle("-fx-background-radius: 20; -fx-background-color: " + color.toString());
        board[y][x].setEffect(lighting);
    }

    public void rotate(double degree) {
        rotatingPanel.setRotate(degree);
    }

    public void showPossibleMove(int y, int x, boolean show) {
        if (show) board[y][x].setStyle("-fx-background-radius: 20; -fx-background-color: lime");
        else board[y][x].setStyle("-fx-background-radius: 20");
    }

    public void initializeCorner(Corner corner) {
        Color color = corner.getColor();
        byte[] coordinates = corner.getCoordinates();

        for (int i = 0; i < 20; ++i) {
            assert coordinates != null;
            setColourOnButton(coordinates[i], coordinates[++i], color);
        }
    }

    public void loadBots() {
        game.loadBots();
    }
}
