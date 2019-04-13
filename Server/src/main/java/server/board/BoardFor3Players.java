package server.board;

public class BoardFor3Players extends BoardTemplate implements IBoard {
    public BoardFor3Players(){
        super();
    }

    @Override
    protected void createPawnsOnBoard() {
        createSecondCornerPawns();
        createFourthCornerPawns();
        createSixthCornerPawns();

        createEmptyCorners();
    }

    @Override
    protected void createAvailableCorners(){
        availableCorners = new byte[]{1, 3, 5};
    }

    private void createEmptyCorners() {
        createEmptyFirstCorner();
        createEmptyThirdCorner();
        createEmptyFifthCorner();
    }
}
