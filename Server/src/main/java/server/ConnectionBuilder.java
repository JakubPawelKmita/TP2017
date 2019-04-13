package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionBuilder {

    private BufferedReader in = null;
    private PrintWriter out = null;

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }

    public void createInputAndOutputStreams(Socket connection) {
        try {
            in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));
            out = new PrintWriter(
                    connection.getOutputStream(), true);
        } catch (Exception exc) {
            exc.printStackTrace();
            try { connection.close(); } catch(Exception e) {}
        }
    }
}
