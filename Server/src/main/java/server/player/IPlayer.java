package server.player;

import server.board.Move;
import server.board.BoardField;

import java.util.List;

public interface IPlayer {
    int getID();
    boolean isActive();
    void sendMessage(String message);
    void itsYourTurn();
    void setActualMove(Move move);
    Move getActualMove();
    void setInactive();
    void sendBoardUpdate(Move actualMove);
    void sendAfterMoveStillCanMove(List<BoardField> possibleMoves);
    void sendAfterMoveEndOfTurn();
    void sendEndOfGame(short i);
    void leaveRoom();
    boolean hasLeaved();
}
