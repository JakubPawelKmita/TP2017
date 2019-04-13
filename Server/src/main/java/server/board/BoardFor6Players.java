package server.board;

public class BoardFor6Players extends BoardTemplate implements IBoard {

    public BoardFor6Players(){
        super();
    }

    @Override
    protected void createPawnsOnBoard() {
        createFirstCornerPawns();
        createSecondCornerPawns();
        createThirdCornerPawns();
        createFourthCornerPawns();
        createFifthCornerPawns();
        createSixthCornerPawns();
    }

    @Override
    protected void createAvailableCorners(){
        availableCorners = new byte[]{0, 1, 2, 3, 4, 5};
    }

}
