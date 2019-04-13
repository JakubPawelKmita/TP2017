package server.board;

import server.board.BoardField;

public class Move {
    private BoardField from;
    private BoardField to;

    public Move(BoardField from, BoardField to){
        this.from = from;
        this.to = to;
    }

    public BoardField getFromField(){
        return from;
    }

    public BoardField getToField(){
        return to;
    }
}
