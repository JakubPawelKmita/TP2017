package server.player;

import server.board.Move;
import server.board.BoardField;

import java.util.List;

public interface IPlayerService {
    void sendMessage(String message);
    int getID();
    void itsYourTurn();
    void sendBoardUpdate(Move actualMove);
    void sendAfterMoveStillCanMove(List<BoardField> possibleMoves);
    void sendAfterMoveEndOfTurn();
    void sendEndOfGame(short place);
}
