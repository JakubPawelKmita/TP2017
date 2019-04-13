package server.gameRoom;

import server.board.IBoard;
import server.player.BotPlayer;
import server.player.IPlayer;
import server.board.Move;
import server.board.BoardFactory;
import server.board.BoardField;

import java.util.ArrayList;
import java.util.List;

public class GameRoom implements IGameRoom, Runnable {
    private int id;
    private List<IPlayer> players;
    private int roomType; //can be for now 2,3,4 or 6 players type, an integer is passed
    private boolean gameActive; //after joining selected number of players game should activate
    private BoardFactory boardFactory;
    private IBoard board;
    private boolean roomIsFull;
    private short winnerCounter = 1;

    public GameRoom(int id, int roomType, BoardFactory boardFactory){
        players = new ArrayList<>();
        this.id = id;
        this.roomType = roomType;
        this.boardFactory = boardFactory;
        board = boardFactory.getGameRoom(roomType);
        roomIsFull = false;
        setGameActive();
    }
    @Override
    public void run() {
        while(isGameActive()){
            if (isRoomFull())
                startGame();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }

    private void startGame() {
        while(!gameHasEnded()){
            for (IPlayer player : players){
                if (player.isActive()) {
                    player.itsYourTurn();
                    Move actualMove = player.getActualMove();

                    if (actualMove!=null && (! actualMove.getFromField().equals(actualMove.getToField())) ) {

                        board.movePawn(actualMove.getFromField(), actualMove.getToField());
                        updateBoardForAllPlayers(actualMove);

                        if (board.wasItHopMove(actualMove.getFromField(), actualMove.getToField())) {
                            while (board.possibleMovesAfterOneMove(actualMove.getFromField(), actualMove.getToField()).size() > 0) {
                                player.sendAfterMoveStillCanMove(board.possibleMovesAfterOneMove(actualMove.getFromField(), actualMove.getToField()));
                                actualMove = player.getActualMove();
                                if (actualMove==null || actualMove.getFromField().equals(actualMove.getToField()) )
                                    break;
                                board.movePawn(actualMove.getFromField(), actualMove.getToField());
                                updateBoardForAllPlayers(actualMove);
                            }
                        }

                    }
                    player.sendAfterMoveEndOfTurn();

                    byte actualPlayerStartCorner = board.getCornerForPlayer(players.indexOf(player));
                    if (board.hasPlayerEnded(actualPlayerStartCorner)){
                        player.sendEndOfGame(winnerCounter++);
                        player.setInactive();
                    }

                }
            }
        }
    }

    private void updateBoardForAllPlayers(Move actualMove) {
        for (IPlayer player : players) {
            if (!player.hasLeaved())
                player.sendBoardUpdate(actualMove);
        }
    }

    private boolean gameHasEnded() {
        return false;
    }

    @Override
    public int getCornerOfPlayer(IPlayer player) {
        return (int) board.getCornerForPlayer(players.indexOf(player));
    }

    @Override
    public synchronized void addBots() {
        for (int i=players.size(); i<roomType; i++)
            players.add(new BotPlayer(board.getCornerForPlayer(i), this));
        roomIsFull = true;
    }

    @Override
    public List<BoardField> possibleMoves(BoardField position){
        return board.possibleMoves(position);
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean canIJoin(){
        return !roomIsFull;
    }

    public int getNumberOfPlayers(){
        return players.size();
    }

    @Override
    public synchronized boolean addPlayerIfPossible(IPlayer player) {

        boolean playerAdded = true;
        boolean roomIsFullPlayerNotAdded = false;

        if(canIJoin()) {
            players.add(player);
            if (getNumberOfPlayers() == getRoomType())
                roomIsFull = true;
            return playerAdded;
        }

        return roomIsFullPlayerNotAdded;
    }


    @Override
    public synchronized void deletePlayer(final int playerID){
        players.removeIf(player->player.getID() == playerID);
    }

    @Override
    public int getRoomType() {
        return roomType;
    }


    public boolean isGameActive() {

        return gameActive;
    }

    public void setInactiveGame(){

        gameActive = false;
    }

    private void setGameActive() {

        gameActive = true;
    }

    public synchronized boolean isRoomFull() {
        return roomIsFull;
    }
}
