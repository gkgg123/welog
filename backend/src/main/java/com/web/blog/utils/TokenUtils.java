package com.web.blog.utils;


import com.web.blog.model.account.Account;
import com.web.blog.service.account.AccountService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Log4j2
public class TokenUtils {
  private static final String secretKey = "ThisIsA_SecretKeyForJwtExample";

  @Resource(name = "accountService")
  private AccountService accountService;

  public String generateJwtToken(Account account) {
    JwtBuilder builder = Jwts.builder()
            .setSubject(account.getUseremail())
            .setHeader(createHeader())
            .setClaims(createClaims(account))
            .setExpiration(createExpireDateForOneYear())
            .signWith(SignatureAlgorithm.HS256, createSigningKey());

    return builder.compact();
  }
  public boolean isValidToken(String token) {
    try {
      Claims claims = getClaimsFormToken(token);

      log.info("expireTime :" + claims.getExpiration());
      log.info("username :" + claims.get("username"));
      log.info("useremail :" + claims.get("useremail"));
      return true;

    } catch (ExpiredJwtException exception) {
      log.error("Token Expired");
      return false;
    } catch (JwtException exception) {
      log.error("Token Tampered");
      return false;
    } catch (NullPointerException exception) {
      log.error("Token is null");
      return false;
    }
  }

  public Authentication createAuthenticationFromToken(String token) {
    UserDetails userDetails = accountService.loadUserByUsername(getUserEmailFromToken(token));
    // it is rather safe to return Authentication with NULL credentials if you do not require to use user credentials after successful authentication.
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }

  public String getTokenFromHeader(String header) {
    return header.split(" ")[1];
  }

  private Date createExpireDateForOneYear() {
    // 토큰 만료시간은 30일으로 설정
    Calendar c= Calendar.getInstance();
    c.add(Calendar.DATE, 30);
    return c.getTime();
  }

  private Map<String, Object> createHeader() {
    Map<String, Object> header = new HashMap<>();

    header.put("typ", "JWT");
    header.put("alg", "HS256");
    header.put("regDate", System.currentTimeMillis());

    return header;
  }

  private Map<String, Object> createClaims(Account account) {
    // 비공개 클레임으로 사용자의 이름과 이메일을 설정, 세션 처럼 정보를 넣고 빼서 쓸 수 있다.
    Map<String, Object> claims = new HashMap<>();

    claims.put("username", account.getUsername());
    claims.put("useremail", account.getUseremail());

    return claims;
  }

  private Key createSigningKey() {
    byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(secretKey);
    return new SecretKeySpec(apiKeySecretBytes, SignatureAlgorithm.HS256.getJcaName());
  }

  private Claims getClaimsFormToken(String token) {
    return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(secretKey))
            .parseClaimsJws(token).getBody();
  }
  private String getTokenFromRequest(HttpServletRequest request){
    return request.getHeader("Authorization");
  }
  private String getUserEmailFromToken(String token) {
    Claims claims = getClaimsFormToken(token);
    return (String) claims.get("useremail");
  }
}
