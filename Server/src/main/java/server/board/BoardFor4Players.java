package server.board;

public class BoardFor4Players extends BoardTemplate implements IBoard {
    public BoardFor4Players(){
        super();
    }

    @Override
    protected void createPawnsOnBoard() {
        createSecondCornerPawns();
        createThirdCornerPawns();
        createFifthCornerPawns();
        createSixthCornerPawns();

        createEmptyCorners();
    }

    @Override
    protected void createAvailableCorners(){
        availableCorners = new byte[]{1, 2, 4, 5};
    }

    private void createEmptyCorners() {
        createEmptyFirstCorner();
        createEmptyFourthCorner();
    }
}
