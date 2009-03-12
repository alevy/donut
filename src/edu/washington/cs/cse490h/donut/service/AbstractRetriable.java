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