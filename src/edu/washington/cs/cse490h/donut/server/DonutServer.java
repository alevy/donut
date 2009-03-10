package edu.washington.cs.cse490h.donut.server;

import java.util.logging.Logger;

import org.apache.thrift.server.TServer;

import com.google.inject.Inject;

/**
 * @author alevy
 */
public class DonutServer extends Thread {

    private static final Logger LOGGER;

    private final TServer       server;

    static {
        LOGGER = Logger.getLogger(DonutPeer.class.getName());
    }

    @Inject
    public DonutServer(TServer server) {
        this.server = server;
    }

    @Override
    public void run() {
        LOGGER.info("Starting Donut server...");
        super.run();
        server.serve();
    }

    public void kill() {
        server.stop();
    }
}
