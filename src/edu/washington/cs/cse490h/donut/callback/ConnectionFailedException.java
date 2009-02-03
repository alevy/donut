package edu.washington.cs.cse490h.donut.callback;

/**
 * @author alevy
 *
 */
public class ConnectionFailedException extends Exception {
    
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
