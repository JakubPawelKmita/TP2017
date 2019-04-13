package server;

import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest {
    @Test
    public void main() throws Exception {
        Main main = new Main();
        String[] args = new String[]{"127.0.0.2"};
        main.main(args);
    }

}