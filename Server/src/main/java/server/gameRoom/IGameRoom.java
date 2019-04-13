package server.gameRoom;

import server.player.IPlayer;
import server.board.BoardField;

import java.util.List;

public interface IGameRoom extends Runnable {
    int getId();
    boolean addPlayerIfPossible(IPlayer player);
    void deletePlayer(int playerID);
    int getRoomType();
    int getNumberOfPlayers();
    boolean isGameActive();
    void setInactiveGame();
    boolean canIJoin();
    List<BoardField> possibleMoves(BoardField boardField);
    int getCornerOfPlayer(IPlayer id);
    void addBots();
}
