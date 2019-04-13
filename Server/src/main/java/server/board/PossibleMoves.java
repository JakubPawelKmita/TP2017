package server.board;

public class PossibleMoves {
    public final static byte[] noHop = {
            -1,0,
            -1,1,
             0,1,
             1,0,
            1,-1,
            0,-1
    }; //pairs (delta_row, delta_col)
    public final static byte[] withHop = {
            -2,0,
            -2,2,
             0,2,
             2,0,
            2,-2,
            0,-2
    }; //pairs (delta_row, delta_col)
}
