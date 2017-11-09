package service;

/**
 * This exception class has been created to separate all existing exceptions from such that the JVS is using.
 */
public class JVSException extends Exception {
    JVSException() {super();}

    JVSException(String message) {super(message);}
}