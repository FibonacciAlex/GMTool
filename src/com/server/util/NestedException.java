package com.server.util;

public class NestedException extends RuntimeException{

	private static final long serialVersionUID = -8717303685092476120L;
	
	private Throwable throwable;  
	  
    private NestedException(Throwable t) {  
        this.throwable = t;  
    }  
  
    /** Wraps another exeception in a RuntimeException. */  
    public static RuntimeException wrap(Throwable t) {  
        if (t instanceof RuntimeException)  
            return (RuntimeException) t;  
        return new NestedException(t);  
    }  
  
    @Override
	public Throwable getCause() {  
        return this.throwable;  
    }  
  
    @Override
	public void printStackTrace() {  
        this.throwable.printStackTrace();  
    }  
}
