package edu.washington.cs.cse490h.donut.service;

/**
 * @author alevy
 *
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
