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

package edu.washington.cs.cse490h.donut.service;

/**
 * @author alevy
 */
public class RetryFailedException extends Exception {

    private static final long serialVersionUID = 1813350771785633251L;

    public RetryFailedException() {
        super();
    }

    public RetryFailedException(String message) {
        super(message);
    }

    public RetryFailedException(Throwable cause) {
        super(cause);
    }

    public RetryFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
