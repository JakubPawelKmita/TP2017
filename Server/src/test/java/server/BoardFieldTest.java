package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import server.board.BoardField;

public class BoardFieldTest {
    private byte row;
    private byte col;
    private BoardField boardField;

    @Before
    public void SetUp(){
        row = (byte) 3;
        col = (byte) 7;
        boardField = new BoardField(row, col);
    }

    @Test
    public void getRow() throws Exception {
        Assert.assertEquals(row, boardField.getRow());
    }

    @Test
    public void getCol() throws Exception {
        Assert.assertEquals(col, boardField.getCol());
    }

    @Test
    public void toStringTest() throws Exception {
        Assert.assertEquals(String.format("row: %d col: %d", row, col), boardField.toString());
    }

    @Test
    public void equals() throws Exception {
        BoardField boardField2 = new BoardField(row, col);

        Assert.assertTrue(boardField.equals(boardField2));
    }

}