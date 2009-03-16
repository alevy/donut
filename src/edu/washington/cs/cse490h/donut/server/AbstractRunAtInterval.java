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

public abstract class AbstractRunAtInterval extends Thread {
    private int interval;

    /**
     * Creates a new RunAtInterval thread. Place the code to be repeated every interval milliseconds
     * in the runClosure() body. Call kill when you want to kill the thread
     * 
     * @param interval
     *            The interval between each time runClosure is called
     */
    public AbstractRunAtInterval(int interval) {
        if (interval <= 0)
            throw new IllegalArgumentException("interval must be > 0");
        this.interval = interval;
    }

    public abstract void runClosure();

    public void kill() {
        interval = 0;
    }

    @Override
    public void run() {
        super.run();
        while (interval != 0) {
            runClosure();
            try {
                sleep(interval);
            } catch (InterruptedException e) {
            }
        }
    }
}
