package edu.school21.sockets;

import edu.school21.sockets.server.Server;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;

public class AppTest {

    private Server server;

    @Before
    public void setUp() {
        server = mock(Server.class);
    }

    @Test
    public void testServerStart() throws IOException {

        server.startServer(8081);

        verify(server).startServer(8081);
    }

    @Test
    public void testMultipleClients() throws InterruptedException {
        int numClients = 5;
        CountDownLatch latch = new CountDownLatch(numClients);

        for (int i = 0; i < numClients; i++) {
//            ServerTest serverSpy = spy(new ServerTest());
            new Thread(() -> {
                Process process1 = null;
                Process process2 = null;
                try {
                    process1 = Runtime.getRuntime().exec("java -jar SocketClient-1.0-SNAPSHOT.jar --port=8081\n");
                    process2 = Runtime.getRuntime().exec("java -jar SocketClient-1.0-SNAPSHOT2.jar --port=8081\n");

                    process1.waitFor();
                    process2.waitFor();


//                    verify(serverSpy).handleClientInteraction(any());


                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
                latch.countDown();
            }).start();
        }

        latch.await();
    }

    @After
    public void tearDown() throws IOException {
        server.stopServer();
    }
}