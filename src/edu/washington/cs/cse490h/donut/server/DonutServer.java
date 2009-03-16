/*
 * Copyright 2009 Amit Levy, Jeff Prouty, Rylan Hawkins
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *      
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
