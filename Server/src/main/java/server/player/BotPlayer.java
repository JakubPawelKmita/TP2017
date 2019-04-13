package server.player;

import server.board.BoardField;
import server.board.CornersCoordinates;
import server.board.Move;
import server.gameRoom.IGameRoom;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BotPlayer implements IPlayer{

    private int id = 11711;
    private byte myCorner;
    private boolean isActive = true;
    private IGameRoom gameRoom;
    private List<BoardField> myPawnsPositions;
    private boolean isItMultiTurn = false;
    private BoardField lastPosition;
    private List<BoardField> multiTurnPossibleMoves;
    private String myPrefix = "\u001B[30m[BOT] \u001B[0m";
    private LinkedList<BoardField> lastMovesInMultiTurn;

    public BotPlayer(byte corner, IGameRoom gameRoom){
        myCorner = corner;
        this.gameRoom = gameRoom;
        createMyPawnsPositions();
        multiTurnPossibleMoves = new ArrayList<>();
        lastMovesInMultiTurn = new LinkedList<>();
        log("starting bot");
    }

    private void createMyPawnsPositions() {
        log("creating my pawns");
        myPawnsPositions = new ArrayList<>();
        for (short i = 0; i < 20-1; i+=2)
            myPawnsPositions.add(new BoardField(CornersCoordinates.allCorners[myCorner][i],
                    CornersCoordinates.allCorners[myCorner][i+1]));
    }


    @Override
    public Move getActualMove() {
        log("get actual move");
        try {
            Thread.sleep(400);
        } catch (InterruptedException e) {
        }

        if (!isItMultiTurn){
            List<BoardField> pawnsWithPossibleMoves = new ArrayList<>();
            pawnsWithPossibleMoves.addAll(myPawnsPositions);
            pawnsWithPossibleMoves.removeIf(pawn -> gameRoom.possibleMoves(pawn).size() == 0);

            BoardField chosenPawn = pawnsWithPossibleMoves.get(random(pawnsWithPossibleMoves.size()));
            List<BoardField> possibleMovesForChosenPawn = gameRoom.possibleMoves(chosenPawn);
            BoardField chosenMove = possibleMovesForChosenPawn.get(random(possibleMovesForChosenPawn.size()));

            updateMyPawns(chosenPawn, chosenMove);

            lastPosition = chosenMove;

            return new Move(chosenPawn, chosenMove);
        }else {
            BoardField chosenMoveField = multiTurnPossibleMoves.get(random(multiTurnPossibleMoves.size()));

            if (lastMovesInMultiTurn.contains(chosenMoveField)) {
                return new Move(new BoardField((byte) 0, (byte) 0), new BoardField((byte) 0, (byte) 0));
            }
            else{
                Move actualMove = new Move(lastPosition, chosenMoveField);
                updateMyPawns(lastPosition, chosenMoveField);
                lastPosition = chosenMoveField;
                lastMovesInMultiTurn.add(chosenMoveField);
                return actualMove;
            }
        }
    }

    private void updateMyPawns(BoardField chosenPawn, BoardField chosenMove) {
        myPawnsPositions.remove(chosenPawn);
        myPawnsPositions.add(chosenMove);
    }

    private int random(int size) {
        Random generator = new Random();
        return generator.nextInt(size);
    }

    @Override
    public void sendAfterMoveStillCanMove(List<BoardField> possibleMoves) {
        isItMultiTurn = true;
        multiTurnPossibleMoves = possibleMoves;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public void setInactive() {
        isActive = false;
    }

    @Override
    public void sendAfterMoveEndOfTurn() {
        isItMultiTurn = false;
        lastMovesInMultiTurn.clear();
    }

    @Override
    public void sendBoardUpdate(Move actualMove) {
    }

    @Override
    public void sendEndOfGame(short i) {
    }

    @Override
    public void leaveRoom() {
    }

    @Override
    public boolean hasLeaved() {
        return false;
    }

    @Override
    public boolean isActive() {
        return isActive;
    }

    @Override
    public void sendMessage(String message) {
    }

    @Override
    public void itsYourTurn() {
    }

    @Override
    public synchronized void setActualMove(Move actualMove){
    }

    private void log(String message) {
        System.out.println(myPrefix + message);
    }
}
