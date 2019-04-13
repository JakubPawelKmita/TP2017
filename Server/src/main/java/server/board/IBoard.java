package server.board;

import server.board.BoardField;

import java.util.List;

public interface IBoard {
    void movePawn(BoardField current, BoardField next);
    List<BoardField> possibleMoves(BoardField position);
    List<BoardField> possibleMovesAfterOneMove(BoardField previous, BoardField current);
    boolean isMoveValid(BoardField current, BoardField next);
    byte getPawnFromPosition(BoardField position);
    byte getCornerForPlayer(int playerNumber);
    boolean hasPlayerEnded(int playerId);
    boolean wasItHopMove(BoardField fromField, BoardField toField);
}
