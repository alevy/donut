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

public abstract class AbstractRetriable<T, E> {

    private int retries = 3;

    public AbstractRetriable() {
        super();
    }

    public void setRetries(int tries) {
        this.retries = tries;
    }

    public int getRetries() {
        return retries;
    }

    public T get(E name) throws RetryFailedException {
        Exception exception = null;

        for (int i = 0; i < getRetries(); ++i) {
            try {
                return tryOne(name);
            } catch (Exception e) {
                exception = e;
                e.printStackTrace();
            }
        }

        throw new RetryFailedException(exception);
    }

    protected abstract T tryOne(E name) throws Exception;

}