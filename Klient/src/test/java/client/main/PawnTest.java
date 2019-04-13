package client.main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jakub Kmita on 01.01.2018.
 */
public class PawnTest {
    private Pawn testPawn;
    @Before
    public void before(){
        testPawn = new Pawn(2, 1);
    }

    @Test
    public void move() throws Exception {
        testPawn.move(-1, -1);
        int d = testPawn.getX()*testPawn.getY();
        assertEquals(1, d);
    }

    @Test
    public void getX() throws Exception {
        assertEquals(1, testPawn.getX());
    }

    @Test
    public void getY() throws Exception {
        assertEquals(2, testPawn.getY());
    }

}