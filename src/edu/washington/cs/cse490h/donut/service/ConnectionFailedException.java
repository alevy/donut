package edu.washington.cs.cse490h.donut.service;

/**
 * @author alevy
 *
 */
public class ConnectionFailedException extends Exception {
    
    private static final long serialVersionUID = 1813350771785633251L;

    public ConnectionFailedException() {
        super();
    }
    
    public ConnectionFailedException(String message) {
        super(message);
    }
    
    public ConnectionFailedException(Throwable cause) {
        super(cause);
    }
    
    public ConnectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
