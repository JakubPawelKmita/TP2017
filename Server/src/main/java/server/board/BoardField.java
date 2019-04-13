package server.board;

public class BoardField {
    private byte row;
    private byte col;

    public BoardField(byte row, byte col){
        this.row = row;
        this.col = col;
    }

    public byte getRow(){
        return row;
    }

    public byte getCol() {
        return col;
    }

    public String toString(){
        return "row: " + row + " col: " + col;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if (!(other instanceof BoardField))return false;
        BoardField otherObj = (BoardField) other;
        if ((otherObj.getRow() == this.getRow()) && (otherObj.getCol() == this.getCol()))
            return true;
        return false;
    }
}
