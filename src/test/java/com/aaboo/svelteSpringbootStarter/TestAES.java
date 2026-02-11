package com.aaboo.svelteSpringbootStarter;

import com.aaboo.svelteSpringbootStarter.common.AES256;

public class TestAES {
	public static void main(String[] args){
		test();
	}
	
	//Property 암복호화
	public static void test() {}
	
	//AES256 Test1
	public static void testAES256_1(String key, String str) {
		AES256 aes256 = new AES256("1234567890123456"); 
		System.out.println(key+"="+ aes256.encode(str) +" ("+ str +")");
	}

	//AES256 Test2
	public static void testAES256_2(String key, String str) {
		AES256 aes256 = new AES256("1234567890123456"); 
		System.out.println(key+"="+ aes256.encode(str) +" ("+ str +")");
	}
}
