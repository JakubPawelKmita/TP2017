package server.board;

public class BoardFactory {
    public IBoard getGameRoom(int type){
        switch(type){
            case(2): return new BoardFor2Players();
            case(3): return new BoardFor3Players();
            case(4): return new BoardFor4Players();
            case(6): return new BoardFor6Players();
        }
        return new BoardFor6Players(); //as default
    }

}
