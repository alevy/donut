package edu.washington.cs.cse490h.donut;

import edu.washington.cs.cse490h.donut.service.ConnectionFailedException;

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
    
    public T get(E name) throws ConnectionFailedException {
        Exception exception = null;

        for (int i = 0; i < getRetries(); ++i) {
            try {
                return tryOne(name);
            } catch (Exception e) {
                exception = e;
            }
        }

        throw new ConnectionFailedException(exception);  
    }
    
    protected abstract T tryOne(E name) throws Exception;

}