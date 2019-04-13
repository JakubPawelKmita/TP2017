package client.main;

import client.boardsDiagrams.Corner;
import client.boardsDiagrams.GameType;
import client.controllers.MainController;
import client.controllers.boards.ClassicBoardController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Jakub Kmita on 02.01.2018.
 */
public class Game {
    private GameType gameType;
    private Corner myCorner;
    private int myIntCorner;
    //private int gameRoomID; //is this necessary?
    private Pawn touched = null;
    private ArrayList<Pawn> myPawns = new ArrayList<>();
    private ArrayList<Pawn> possibleMoves = new ArrayList<>();
    private Boolean myTurn = false;
    private ClassicBoardController boardController;
    private MainController mainController;
    private ServerConnector serverConnector;
    private Boolean wasHop = false;

    void setPossibleMoves(ArrayList<Pawn> possibleMoves) {
        this.possibleMoves = possibleMoves;
        for (Pawn move : possibleMoves) {
            boardController.showPossibleMove(move.getY(), move.getX(), true);
        }
    }

    void executeMove(int aY, int aX, int bY, int bX) {
        for (Pawn pawn : myPawns) {
            if (pawn.getX() == aX && pawn.getY() == aY) {
                pawn.move(bY, bX);
                break;
            }
        }
        boardController.showMove(aX, aY, bX, bY);
    }

    void setMyTurn(Boolean myTurn) {
        this.myTurn = myTurn;
        if (myTurn) setCommunicate("YOUR TURN");
        else {
            setCommunicate("...");
            wasHop = false;
            touched = null;
            possibleMoves = null;
        }
    }

    private void setMyPawns(Corner corner) {
        byte coordinates[] = corner.getCoordinates();
        for (int i = 0; i < 20; ++i) {
            assert coordinates != null;
            myPawns.add(new Pawn(coordinates[i], coordinates[++i]));
        }
        boardController.setColorButon(myCorner.getColor());
    }

    void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    Game(String gameRoomID, ServerConnector serverConnector) {
        this.serverConnector = serverConnector;
        this.myIntCorner = serverConnector.getMyCorner();
        switch (myIntCorner) {
            case 0:
                this.myCorner = Corner.firstCorner;
                break;
            case 1:
                this.myCorner = Corner.secondCorner;
                break;
            case 2:
                this.myCorner = Corner.thirdCorner;
                break;
            case 3:
                this.myCorner = Corner.fourthCorner;
                break;
            case 4:
                this.myCorner = Corner.fifthCorner;
                break;
            case 5:
                this.myCorner = Corner.sixthCorner;
                break;
        }
        switch (serverConnector.checkRoomType(gameRoomID)) {
            case 2:
                this.gameType = GameType.DUEL;
                break;
            case 3:
                this.gameType = GameType.FIGHTOFTHREE;
                break;
            case 4:
                this.gameType = GameType.FIGHTOFFOUR;
                break;
            case 6:
                this.gameType = GameType.FULL;
                break;
        }
    }

    private void loadClassicBoardScreen() {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/boards/ClassicBoardScreen.fxml"));
        HBox hBox = null;
        try {
            hBox = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainController.setScreen(hBox);
        boardController = loader.getController();
        showGameType(gameType);
        rotate();
        boardController.setGame(this);
        setMyPawns(myCorner);

    }

    void go() { //start listening and load board screen
        loadClassicBoardScreen();
        new Thread(serverConnector).start();
    }

    private void rotate() {
        boardController.rotate(60 * (3 + (myIntCorner)));
    }

    private void showGameType(GameType type) {
        switch (type) {
            case DUEL: {
                boardController.initializeCorner(Corner.firstCorner);
                boardController.initializeCorner(Corner.fourthCorner);
            }
            break;
            case FIGHTOFTHREE: {
                boardController.initializeCorner(Corner.secondCorner);
                boardController.initializeCorner(Corner.fourthCorner);
                boardController.initializeCorner(Corner.sixthCorner);
            }
            break;
            case FIGHTOFFOUR: {
                boardController.initializeCorner(Corner.secondCorner);
                boardController.initializeCorner(Corner.thirdCorner);
                boardController.initializeCorner(Corner.fifthCorner);
                boardController.initializeCorner(Corner.sixthCorner);
            }
            break;
            case FULL: {
                boardController.initializeCorner(Corner.firstCorner);
                boardController.initializeCorner(Corner.secondCorner);
                boardController.initializeCorner(Corner.thirdCorner);
                boardController.initializeCorner(Corner.fourthCorner);
                boardController.initializeCorner(Corner.fifthCorner);
                boardController.initializeCorner(Corner.sixthCorner);
            }
        }
    }

    public void touchField(int y, int x) {
        if (myTurn) {
            myTurn = false; //Because we don't want to double click before server reply

            boolean itsMyPawn = false;
            if (!wasHop) for (Pawn pawn : myPawns) if (pawn.getX() == x && pawn.getY() == y) itsMyPawn = true;

            if (itsMyPawn) {
                touched = new Pawn(y, x);
                hideMoves();
                serverConnector.checkPossibleMoves(touched);
            } else if (possibleMoves != null) {
                for (Pawn pawn : possibleMoves)
                    if (pawn.getX() == x && pawn.getY() == y) {
                        hideMoves();
                        possibleMoves = null;
                        serverConnector.makeMove(touched, new Pawn(y, x));
                        touched.move(y, x);
                        wasHop = true;
                        break;
                    } else myTurn = true;
            } else myTurn = true;
        }
    }

    private void hideMoves() {
        if (possibleMoves != null)
            for (Pawn pawn : possibleMoves) {
                boardController.showPossibleMove(pawn.getY(), pawn.getX(), false);
            }
    }

    public void skipMove() {
        myTurn = false;
        hideMoves();
        serverConnector.makeMove(new Pawn(0, 0), new Pawn(0, 0));
    }

    public void leaveRoom() {
        serverConnector.leaveRoom();
    }

    void setCommunicate(String communicate) {
        boardController.setCommunicate(communicate);
    }

    public void loadBots() {
        serverConnector.loadBots();
    }
}
