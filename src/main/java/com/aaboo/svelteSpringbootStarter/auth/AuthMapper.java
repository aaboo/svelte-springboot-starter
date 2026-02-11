package com.aaboo.svelteSpringbootStarter.auth;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.aaboo.svelteSpringbootStarter.common.JtMap;

public interface AuthMapper {
	
	JtMap selectUserCheck(@Param("param") Map<String, Object> param);//로그인
	int updateUserLoginInfo(@Param("param") Map<String, Object> param);//로그인 성공 업데이트
	int updateUserLoginInfoLogout(@Param("param") Map<String, Object> param);//로그인 성공 업데이트
	String selectLoginAuthKey(@Param("param") Map<String, Object> param);//LOGINAUTHKEY 조회
	
	
	
	
	//패스워드 확인
	String pwChangeConfirmSelectUserpw(@Param("userid") String userid);
	
	//패스워드 변경
	int pwChangeConfirmUpdate(			
		@Param("userid") String userid
		, @Param("userpw") String userpw
	);
	
	//사용자 OS, 브라우저, TLS 정보 저장
	int updateTls(			
		@Param("userid") String userid
		, @Param("osVer") String osVer
		, @Param("browserVer") String browserVer
		, @Param("tlsVer") String tlsVer
	);
	//로그인 성공 실패 이력 저장
	int insertUserLoginHistory(
		@Param("userid") String userid
		, @Param("logFlag") String logFlag
		, @Param("userip") String userip
		, @Param("inType") String inType
		, @Param("logNotice") String logNotice
	);
	//본인인증번호 저장
	int updateCertNum(
		@Param("userid") String userid
		, @Param("certNum") String certNum
		, @Param("certTimeLimit") String certTimeLimit
	);	
	//본인 휴대폰본호 확인
	int selectUserPhoneCheck(
		@Param("userid") String userid
		, @Param("certUserPhone") String certUserPhone
	);
	//본인인증본호 확인
	int selectUserCertCheck(
		@Param("userid") String userid
		, @Param("certNum") String certNum
		, @Param("certUserPhone") String certUserPhone
	);
	//본인인증본호 확인
	int updateUserCertCheck(@Param("userid") String userid);
	
	//패스워드 관련
	int updateLoginFailedCount(@Param("userid") String userid);//패스워드 틀린 숫자 입력	
	int updateLoginSuccessCount(@Param("userid") String userid);//패스워드 성공 숫자 업데이트(passwd>0)	
	String selectLoginFailedCount(@Param("userid") String userid);//패스워드 틀린 숫자 조회
}
