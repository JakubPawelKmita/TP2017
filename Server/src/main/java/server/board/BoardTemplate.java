package server.board;

import java.util.ArrayList;
import java.util.List;

public abstract class BoardTemplate implements IBoard {

    private final byte fieldWithoutPawn = -1;
    private final byte invalidField = -2;
    private List<List<Byte>> board;
    protected byte[] availableCorners;
    protected byte[] oppositeCorners;
    protected List<List<BoardField>> cornerFields;
    int moveCounter = 0;

    public BoardTemplate(){
        createBoard();
        createPawnsOnBoard();
        createAvailableCorners();
        createOppositeCorners();
        createCornerFields();
    }

    @Override
    public boolean hasPlayerEnded(int startCorner){
        int oppositeCorner = oppositeCorners[startCorner];

        for (short i = 0; i < 20-1; i+=2) {
            BoardField actualField = new BoardField(CornersCoordinates.allCorners[oppositeCorner][i],
                    CornersCoordinates.allCorners[oppositeCorner][i+1]);
            byte actualFieldValue = getPawnFromPosition(actualField);

            if (actualFieldValue != startCorner)
                return false;
        }

        return true;
    }

    @Override
    public boolean wasItHopMove(BoardField fromField, BoardField toField){
        for (int i=0; i<11; i+=2) {
            BoardField positionToCheck = new BoardField(
                    (byte) (fromField.getRow() + PossibleMoves.withHop[i]),
                    (byte) (fromField.getCol() + PossibleMoves.withHop[i + 1]));
            if (positionToCheck.equals(toField))
                return true;
        }
        return false;
    }

    @Override
    public byte getCornerForPlayer(int playerNumber){
        /*
        This method will be used when game room will ask for players corner
        If game room has 4 players, first player will have 0th corner, second 1th etc
         */
        if ((playerNumber >= 0) && playerNumber < availableCorners.length)
            return availableCorners[playerNumber];
        else
            return -1;
    }

    @Override
    public void movePawn(BoardField current, BoardField next) {
        if (isMoveValid(current, next)){
            setPawnInLocation(next.getRow(), next.getCol(), getPawnFromPosition(current));
            setPawnInLocation(current.getRow(), current.getCol(), fieldWithoutPawn);
        }
        System.out.println(String.format("move no %d", ++moveCounter));
        printBoard();
    }

    @Override
    public List<BoardField> possibleMoves(BoardField position) {
        if (isItAPawn(position)) {
            ArrayList<BoardField> possibleMoves = new ArrayList<>();
            possibleMoves.addAll(possibleMovesNoHop(position));
            possibleMoves.addAll(possibleMovesWithHop(position));
            return checkIfIsInOppositeCorner(position, possibleMoves);
        }
        else
            return new ArrayList<>();
    }

    @Override
    public List<BoardField> possibleMovesAfterOneMove(BoardField previous, BoardField current) {
        ArrayList<BoardField> possibleMoves = new ArrayList<>();
        if (!previous.equals(current)) {
            possibleMoves.addAll(possibleMovesWithHop(current));
            possibleMoves.remove(previous);
        }
        return checkIfIsInOppositeCorner(current, possibleMoves);
    }

    private boolean isItAPawn(BoardField position) {
        byte pawn = getPawnFromPosition(position);
        if ((pawn == invalidField)||(pawn == fieldWithoutPawn))
            return false;
        else
            return true;
    }

    private List<BoardField> checkIfIsInOppositeCorner(BoardField position, ArrayList<BoardField> possibleMoves) {
        byte actualPawn = getPawnFromPosition(position);
        if ((actualPawn == fieldWithoutPawn) ||(actualPawn == invalidField))
            return possibleMoves;
        byte oppositeCornerForGivenPawn = oppositeCorners[getPawnFromPosition(position)];
        if (isItOpposite(position, oppositeCornerForGivenPawn))
            possibleMoves.removeIf(field -> !isItOpposite(field, oppositeCornerForGivenPawn));
        return possibleMoves;
    }

    private boolean isItOpposite(BoardField field, byte oppositeCornerForGivenPawn) {
        return cornerFields.get(oppositeCornerForGivenPawn).contains(field);
    }

    @Override
    public boolean isMoveValid(BoardField current, BoardField next) {
        for (BoardField field : possibleMoves(current)){
            if ((field.getRow() == next.getRow()) && (field.getCol() == next.getCol()))
                return true;
        }
        return false;
    }

    public byte getPawnFromPosition(BoardField position){
        try {
            return board.get(position.getRow()).get(position.getCol());
        } catch (IndexOutOfBoundsException exception){
            return invalidField;
        }
    }


    protected abstract void createAvailableCorners();

    protected void createPawnsOnBoard(){
    }

    protected void createFirstCornerPawns(){
        createCornerPawns((byte)1);
    }

    protected void createSecondCornerPawns(){
        createCornerPawns((byte)2);
    }

    protected void createThirdCornerPawns(){
        createCornerPawns((byte)3);
    }

    protected void createFourthCornerPawns(){
        createCornerPawns((byte)4);
    }

    protected void createFifthCornerPawns(){
        createCornerPawns((byte)5);
    }

    protected void createSixthCornerPawns(){
        createCornerPawns((byte)6);
    }

    protected void createEmptyFirstCorner(){
        createEmptyCorner((byte)1);
    }

    protected void createEmptySecondCorner(){
        createEmptyCorner((byte)2);
    }

    protected void createEmptyThirdCorner(){
        createEmptyCorner((byte)3);
    }

    protected void createEmptyFourthCorner(){
        createEmptyCorner((byte)4);
    }

    protected void createEmptyFifthCorner(){
        createEmptyCorner((byte)5);
    }

    protected void createEmptySixthCorner(){
        createEmptyCorner((byte)6);
    }

    protected void createEmptyCorner(byte corner) {
        for (short i = 0; i < 20-1; i+=2)
            setPawnInLocation(CornersCoordinates.allCorners[corner-1][i],
                    CornersCoordinates.allCorners[corner-1][i+1],
                    fieldWithoutPawn);
    }

    protected void createCornerPawns(byte corner) {
        for (short i = 0; i < 20-1; i+=2)
            setPawnInLocation(CornersCoordinates.allCorners[corner-1][i],
                    CornersCoordinates.allCorners[corner-1][i+1],
                    (byte) (corner-1));
    }

    protected void setPawnInLocation(byte row, byte column, byte pawnNumber) {
        board.get(row).set(column, pawnNumber);
    }


    private List<BoardField> possibleMovesWithHop(BoardField position) {
        ArrayList<BoardField> possible = new ArrayList<>();
        for (int i=0; i<11; i+=2) {
            BoardField positionToCheck = new BoardField(
                    (byte) (position.getRow() + PossibleMoves.withHop[i]), (byte) (position.getCol() + PossibleMoves.withHop[i+1]));
            BoardField positionWithPawnToHopOver = new BoardField(
                    (byte) (position.getRow() + PossibleMoves.noHop[i]), (byte) (position.getCol() + PossibleMoves.noHop[i+1]));
            if ((getPawnFromPosition(positionToCheck) == fieldWithoutPawn) && (getPawnFromPosition(positionWithPawnToHopOver) > -1))
                possible.add(positionToCheck);
        }
        return possible;
    }

    private List<BoardField> possibleMovesNoHop(BoardField position) {
        ArrayList<BoardField> possible = new ArrayList<>();
        for (int i=0; i<11; i+=2) {
            BoardField positionToCheck = new BoardField(
                    (byte) (position.getRow() + PossibleMoves.noHop[i]), (byte) (position.getCol() + PossibleMoves.noHop[i+1]));
            if (getPawnFromPosition(positionToCheck) == fieldWithoutPawn)
                possible.add(positionToCheck);
        }
        return possible;
    }

    private void createBoard() {
        board = new ArrayList<>(17);

        for (int i=0; i<17; i++) {
            board.add(new ArrayList<>(17));
            for (int j = 0; j < 17; j++)
                board.get(i).add(invalidField);
        }

        initializeCenterFields();
    }

    private void initializeCenterFields() {
        /*
        During creation all fields on board were marked as invalid
        In this method fields in the center of board will be marked as empty valid fields
        Two
         */
        for (int row=4; row<13; row++){
            for (int column=4; column<13; column++)
                board.get(row).set(column, fieldWithoutPawn);
        }

    }

    private void createOppositeCorners() {
        oppositeCorners = new byte[]{3,4,5,0,1,2};
    }

    private void createCornerFields() {
        cornerFields = new ArrayList<>();
        for (short i = 0; i < 6; i++) {
            cornerFields.add(new ArrayList<>());
            for (short j = 0; j < 20-1; j+=2) {
                cornerFields.get(i).add(new BoardField(CornersCoordinates.allCorners[i][j],
                        CornersCoordinates.allCorners[i][j+1]));
            }
        }

    }

    private void printBoard() {
        for (int i=0; i<17; i++) {
            for (int j=0; j<17; j++) {
                if (board.get(i).get(j) == fieldWithoutPawn)
                    System.out.print("- ");
                else if (board.get(i).get(j) == invalidField)
                    System.out.print("  ");
                else
                    System.out.print("\u001B[33m" + board.get(i).get(j) + "\u001B[0m ");
            }
            System.out.println(" ");
        }
    }
}
