package client.boardsDiagrams;

import org.junit.Test;

import static client.boardsDiagrams.Corner.*;
import static org.junit.Assert.*;

/**
 * Created by Jakub Kmita on 01.01.2018.
 */
public class CornerTest {
    @Test
    public void getColor() throws Exception {
        assertEquals(Color.blue, firstCorner.getColor());
        assertEquals(Color.yellow, secondCorner.getColor());
        assertEquals(Color.red, thirdCorner.getColor());
        assertEquals(Color.green, fourthCorner.getColor());
        assertEquals(Color.orange, fifthCorner.getColor());
        assertEquals(Color.purple, sixthCorner.getColor());
    }

    @Test
    public void getCoordinates() throws Exception {
        assertEquals(CornersCoordinates.firstCorner, firstCorner.getCoordinates());
        assertEquals(CornersCoordinates.secondCorner, secondCorner.getCoordinates());
        assertEquals(CornersCoordinates.thirdCorner, thirdCorner.getCoordinates());
        assertEquals(CornersCoordinates.fourthCorner, fourthCorner.getCoordinates());
        assertEquals(CornersCoordinates.fifthCorner, fifthCorner.getCoordinates());
        assertEquals(CornersCoordinates.sixthCorner, sixthCorner.getCoordinates());

    }

}