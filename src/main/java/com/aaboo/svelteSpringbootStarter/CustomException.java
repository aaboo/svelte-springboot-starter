package com.aaboo.svelteSpringbootStarter;

public class CustomException extends RuntimeException {
	
	CustomException(){};
	CustomException(String msg){
		super(msg);
	};
	
	public void exception(String msg) {
		System.out.println(msg);
	}
	
	public static void main (String[] args) {
		throw new CustomException("야 이놈아");
	}
}
