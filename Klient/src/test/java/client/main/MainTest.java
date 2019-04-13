package client.main;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Jakub Kmita on 01.01.2018.
 */
public class MainTest {
    @Test
    public void start() throws Exception {
        assertNotEquals(null, new Main());
    }
}