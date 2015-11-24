package jdk2010.current.future;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FpkjFutureTask extends FutureTask<String>{
	
	private String encodeStr;
	
	
	public String getEncodeStr() {
		return encodeStr;
	}


	public void setEncodeStr(String encodeStr) {
		this.encodeStr = encodeStr;
	}


	public FpkjFutureTask(Callable<String> callable) {
		super(callable);
 	}

	
 

}
