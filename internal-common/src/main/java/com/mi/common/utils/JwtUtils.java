package com.mi.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mi.common.dto.TokenResult;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: JWTUtils
 * Description:
 *
 * @author Jay
 * @version v1.0
 */
public class JwtUtils {

    //盐
    private static final String SECRET = "wojiaobjx";
    private static final String JWT_KEY_PHONE = "phone";
    //乘客是1 司机是2
    private static final String JWT_KEY_IDENTITY = "identity";

    private static final String JWT_TOKEN_TYPE = "tokenType";
    private static final String JWT_TOKEN_TIME = "tokenTime";

    //生成token
    public static String generateToken(String passengerPhone,String identity,String tokenType) {
        Map<String,String> map = new HashMap<>();
        map.put(JWT_KEY_PHONE,passengerPhone);
        map.put(JWT_KEY_IDENTITY,identity);
        map.put(JWT_TOKEN_TYPE,tokenType);
        //防止每次生成的token一样
        map.put(JWT_TOKEN_TIME,Calendar.getInstance().getTime().toString());

        JWTCreator.Builder builder = JWT.create();
        //整合map
        map.forEach((key,value)->{
            builder.withClaim(key,value);
        });

        //整合过期时间
        //builder.withExpiresAt(date);

        //生成token
        return builder.sign(Algorithm.HMAC256(SECRET));
    }

    //解析token
    public static TokenResult parseToken(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(SECRET)).build().verify(token);
        String phone = verify.getClaim(JWT_KEY_PHONE).asString();
        String identity = verify.getClaim(JWT_KEY_IDENTITY).asString();

        TokenResult tokenResult = new TokenResult();
        tokenResult.setPhone(phone);
        tokenResult.setIdentity(identity);

        return tokenResult;
    }

    /**
     * 校验token,主要是判断token是否有异常
     * @return
     */
    public static TokenResult checkToken(String token){
        TokenResult tokenResult = null;
        try {
            //解析token
            tokenResult = JwtUtils.parseToken(token);
        } catch (Exception e) {

        }
        return tokenResult;
    }

    public static void main(String[] args) {
        String token = generateToken("13910733521","1","accessToken");
        System.out.println("生成的token:"+ token);

        System.out.println("---------------解析---------------");

        TokenResult tokenResult = parseToken(token);
        System.out.println("手机号:" + tokenResult.getPhone());
        System.out.println("身份:" + tokenResult.getIdentity());

    }

}
