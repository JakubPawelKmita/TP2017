package server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;

public class ConnectionBuilderTest {
    private Socket socket;
    private ConnectionBuilder connectionBuilder;

    @Before
    public void setUp(){
        socket = mock(Socket.class);
        connectionBuilder = new ConnectionBuilder();
    }

    @Test
    public void getIn() throws Exception {
        Assert.assertNull(connectionBuilder.getOut());
    }

    @Test
    public void getOut() throws Exception {
        Assert.assertNull(connectionBuilder.getIn());
    }

    @Test
    public void createInputAndOutputStreams() throws Exception {
        connectionBuilder.createInputAndOutputStreams(socket);

        verify(socket, times(1)).getInputStream();
        verify(socket, times(1)).close();
    }

}