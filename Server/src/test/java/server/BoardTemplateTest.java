package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.board.BoardFactory;
import server.board.BoardField;
import server.board.CornersCoordinates;
import server.board.IBoard;

import java.util.List;

public class BoardTemplateTest {

    private IBoard board;
    private BoardFactory boardFactory;
    private final byte fieldWithoutPawn = -1;
    private final byte invalidField = -2;

    @Before
    public void setUp(){
        boardFactory = new BoardFactory();
        board = boardFactory.getGameRoom(6);
    }

    @Test
    public void testThatBoardWasCreatedAfterInstantination(){
        Assert.assertNotNull(board);
    }

    @Test
    public void testThatPawnsInFirstCornerAreLocatedAsDefined(){
        for(int i=0; i<20-1; i+=2){
            BoardField currentField = new BoardField(CornersCoordinates.firstCorner[i], CornersCoordinates.firstCorner[i+1]);
            Assert.assertEquals(0, board.getPawnFromPosition(currentField));
        }

    }

    @Test
    public void testThatPawnsInSecondCornerAreLocatedAsDefined(){
        for(int i=0; i<20-1; i+=2){
            BoardField currentField = new BoardField(CornersCoordinates.secondCorner[i], CornersCoordinates.secondCorner[i+1]);
            Assert.assertEquals(1, board.getPawnFromPosition(currentField));
        }

    }

    @Test
    public void testThatPawnsInThirdCornerAreLocatedAsDefined(){
        for(int i=0; i<20-1; i+=2){
            BoardField currentField = new BoardField(CornersCoordinates.thirdCorner[i], CornersCoordinates.thirdCorner[i+1]);
            Assert.assertEquals(2, board.getPawnFromPosition(currentField));
        }

    }

    @Test
    public void testThatPawnsInFourthCornerAreLocatedAsDefined(){
        for(int i=0; i<20-1; i+=2){
            BoardField currentField = new BoardField(CornersCoordinates.fourthCorner[i], CornersCoordinates.fourthCorner[i+1]);
            Assert.assertEquals(3, board.getPawnFromPosition(currentField));
        }

    }

    @Test
    public void testThatPawnsInFifthCornerAreLocatedAsDefined(){
        for(int i=0; i<20-1; i+=2){
            BoardField currentField = new BoardField(CornersCoordinates.fifthCorner[i], CornersCoordinates.fifthCorner[i+1]);
            Assert.assertEquals(4, board.getPawnFromPosition(currentField));
        }

    }

    @Test
    public void testThatPawnsInSixthCornerAreLocatedAsDefined(){
        for(int i=0; i<20-1; i+=2){
            BoardField currentField = new BoardField(CornersCoordinates.sixthCorner[i], CornersCoordinates.sixthCorner[i+1]);
            Assert.assertEquals(5, board.getPawnFromPosition(currentField));
        }

    }

    @Test
    public void testPossibleMovesInCorners(){
        List<BoardField> possibleMoves = board.possibleMoves(new BoardField((byte)0,(byte)12));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)4,(byte)16)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)12,(byte)12)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)16,(byte)4)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)12,(byte)0)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)4,(byte)4)));

        Assert.assertEquals(0, possibleMoves.size());
    }

    @Test
    public void testPossibleMovesInsideCornersRowsWithOneAndTwoPawns(){
        List<BoardField> possibleMoves = board.possibleMoves(new BoardField((byte)12,(byte)11));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)5,(byte)15)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)12,(byte)11)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)15,(byte)5)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)11,(byte)1)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)4,(byte)5)));

        Assert.assertEquals(0, possibleMoves.size());
    }

    @Test
    public void testPossibleMovesFrontPawns(){
        List<BoardField> possibleMoves = board.possibleMoves(new BoardField((byte)3,(byte)10));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)5,(byte)13)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)10,(byte)11)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)13,(byte)5)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)10,(byte)3)));
        possibleMoves.addAll(board.possibleMoves(new BoardField((byte)5,(byte)6)));

        Assert.assertEquals(6*2, possibleMoves.size());
    }

    @Test
    public void testMovePawnToValidPosition(){
        BoardField current = new BoardField((byte)3,(byte)11);
        BoardField next = new BoardField((byte)4,(byte)11);
        byte movedPawn = board.getPawnFromPosition(current);

        board.movePawn(current, next);

        Assert.assertEquals(movedPawn, board.getPawnFromPosition(next));
        Assert.assertEquals(fieldWithoutPawn, board.getPawnFromPosition(current));
    }

    @Test
    public void testMovePawnToInvalidPositionShouldResultInNoMove(){
        BoardField current = new BoardField((byte)3,(byte)11);
        BoardField next = new BoardField((byte)5,(byte)6);
        byte movedPawn = board.getPawnFromPosition(current);
        byte destinationPawn = board.getPawnFromPosition(next);

        board.movePawn(current, next);

        Assert.assertEquals(destinationPawn, board.getPawnFromPosition(next));
        Assert.assertEquals(movedPawn, board.getPawnFromPosition(current));
    }

    @Test
    public void testPossibleMovesWithHop(){
        BoardField current = new BoardField((byte)13,(byte)5);
        BoardField next = new BoardField((byte)12,(byte)5);

        board.movePawn(current, next);

        Assert.assertTrue(board.possibleMoves(new BoardField((byte)13,(byte)4)).contains(new BoardField((byte)11,(byte)6)));
    }

    @Test
    public void testMoveWithHop(){
        BoardField current = new BoardField((byte)13,(byte)5);
        BoardField next = new BoardField((byte)12,(byte)5);

        board.movePawn(current, next);

        BoardField currentHop = new BoardField((byte)13,(byte)4);
        BoardField nextHop = new BoardField((byte)11,(byte)6);
        byte pawnThatWillHopAnotherPawn = board.getPawnFromPosition(currentHop);

        board.movePawn(currentHop, nextHop);

        Assert.assertEquals(pawnThatWillHopAnotherPawn, board.getPawnFromPosition(nextHop));
    }

}