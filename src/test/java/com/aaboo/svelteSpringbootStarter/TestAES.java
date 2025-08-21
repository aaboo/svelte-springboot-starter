package com.aaboo.svelteSpringbootStarter;

import com.aaboo.svelteSpringbootStarter.common.AES256;

public class TestAES {
	public static void main(String[] args){
		test();
	}
	
	//Property 암복호화
	public static void test() {
		//위키참조: VAN WEB서비스 DB정보 암호화
		 System.out.println("#local");
	    testAES256_1("axboot.dataSource.username", "osca_new");
	    testAES256_1("axboot.dataSource.password", "osca_new_dev");
	    testAES256_1("mysql.bosms.username", "bosms");
	    testAES256_1("mysql.bosms.password", "!@wpdlxl34");
	    testAES256_1("bxm.sftp.id", "osca");
	    testAES256_1("bxm.sftp.pw", "jtnet!11osca");
	    System.out.println();
	    System.out.println("#dev(alpha)");
	    testAES256_1("axboot.dataSource.username", "osca_new");
	    testAES256_1("axboot.dataSource.password", "osca_new_dev");
	    testAES256_1("mysql.bosms.username", "bosms");
	    testAES256_1("mysql.bosms.password", "!@wpdlxl34");
	    testAES256_1("bxm.sftp.id", "osca");
	    testAES256_1("bxm.sftp.pw", "jtnet!11osca");
	    System.out.println();
	    System.out.println("#test(beta)");
	    testAES256_1("axboot.dataSource.username", "osca_new");
	    testAES256_1("axboot.dataSource.password", "osca_new_test");
	    testAES256_1("mysql.bosms.username", "bosms");
	    testAES256_1("mysql.bosms.password", "!@wpdlxl34");
	    testAES256_1("bxm.sftp.id", "osca");
	    testAES256_1("bxm.sftp.pw", "jtnet!11osca");
	    System.out.println();
	    System.out.println("#production");
	    testAES256_2("dataSource.oracle.username", "osca_web");
	    testAES256_2("dataSource.oracle.password", "oscaweb!11dhnf");
	    testAES256_2("dataSource.mysql.username", "bosms");
	    testAES256_2("dataSource.mysql.password", "!@wpdlxl34");
	    testAES256_2("bxm.sftp.id", "osca");
	    testAES256_2("bxm.sftp.pw", "jtnet!11osca");
	}
	
	//AES256 Test1
	public static void testAES256_1(String key, String str) {
		AES256 aes256 = new AES256("202208~@NiCePaY*"); 
		System.out.println(key+"="+ aes256.encode(str) +" ("+ str +")");
	}

	//AES256 Test2
	public static void testAES256_2(String key, String str) {
		AES256 aes256 = new AES256("!@2208#$NiCePaY*"); 
		System.out.println(key+"="+ aes256.encode(str) +" ("+ str +")");
	}
}
