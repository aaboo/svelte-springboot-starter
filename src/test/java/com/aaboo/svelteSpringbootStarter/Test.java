package com.aaboo.svelteSpringbootStarter;

public class Test {
	public static void main(String[] args) {
		String url = "http:/localhost:8082/a/b/c/d/e/f/g";
		System.out.println(url.matches("$\\/.+"));
		System.out.println(url.substring(url.lastIndexOf("/")+1));
	}
}
