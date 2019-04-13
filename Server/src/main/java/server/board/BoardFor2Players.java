package server.board;

public class BoardFor2Players extends BoardTemplate implements IBoard {
    public BoardFor2Players(){
        super();
    }

    @Override
    protected void createPawnsOnBoard() {
        createFirstCornerPawns();
        createFourthCornerPawns();

        createEmptyCorners();
    }

    @Override
    protected void createAvailableCorners(){
        availableCorners = new byte[]{0, 3};
    }

    private void createEmptyCorners() {
        createEmptySecondCorner();
        createEmptyThirdCorner();
        createEmptyFifthCorner();
        createEmptySixthCorner();
    }
}
