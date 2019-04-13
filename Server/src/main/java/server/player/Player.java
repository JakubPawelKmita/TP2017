package server.player;

import server.board.Move;
import server.board.BoardField;

import java.util.List;

public class Player implements IPlayer { //aka adapter for PlayerService

    private IPlayerService myPlayerService;
    private boolean amIActive;
    private volatile boolean leavedRoom;
    private volatile Move actualMove;
    private volatile boolean iHaveNewMove;

    public Player(IPlayerService myPlayerService){
        this.myPlayerService = myPlayerService;
        amIActive = true;
        leavedRoom = false;
        noNewMove();
    }

    @Override
    public void sendMessage(String message){
        myPlayerService.sendMessage(message);
    }

    @Override
    public synchronized void setActualMove(Move actualMove){
        this.actualMove = actualMove;
        newMove();
    }

    @Override
    public Move getActualMove() {

        while (!doIHaveNewMove()){
            if (!amIActive)
                return null;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
            }
        }
        noNewMove();
        return actualMove;
    }

    @Override
    public int getID() {
        return myPlayerService.getID();
    }

    @Override
    public void itsYourTurn() {
        myPlayerService.itsYourTurn();
    }

    public synchronized void setInactive(){
        amIActive = false; // player decided to left this game room or has ended game("win")
    }

    @Override
    public void sendBoardUpdate(Move actualMove) {
        myPlayerService.sendBoardUpdate(actualMove);
    }

    @Override
    public void sendAfterMoveStillCanMove(List<BoardField> possibleMoves) {
        myPlayerService.sendAfterMoveStillCanMove(possibleMoves);
    }

    @Override
    public void sendAfterMoveEndOfTurn() {
        myPlayerService.sendAfterMoveEndOfTurn();
    }

    @Override
    public void sendEndOfGame(short place) {
        myPlayerService.sendEndOfGame(place);
    }

    @Override
    public synchronized void leaveRoom(){
        leavedRoom = true;
        amIActive = false;
    }

    @Override
    public synchronized boolean hasLeaved(){
        return leavedRoom;
    }

    public synchronized boolean isActive(){
        return amIActive;
    }

    private synchronized void newMove() {
        iHaveNewMove = true;
    }

    private synchronized void noNewMove() {
        iHaveNewMove = false;
    }

    private synchronized boolean doIHaveNewMove() {
        return iHaveNewMove;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof IPlayer))return false;
        IPlayer otherObj = (IPlayer) other;
        if (otherObj.getID() == this.getID())
            return true;
        return false;
    }
}
