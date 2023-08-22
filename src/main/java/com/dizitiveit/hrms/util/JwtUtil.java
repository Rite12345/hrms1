package com.dizitiveit.hrms.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;



@Service
public class JwtUtil {

	 private String SECRET_KEY = "1096076897863288";
	 
	 
	  public String extractUsername(String token) {
		  return extractClaim(token, Claims::getSubject); 
		  }
	  
	  public Date extractExpiration(String token) { 
		  return extractClaim(token,Claims::getExpiration);
		  }
	  
	  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
	  {
	  final Claims claims = extractAllClaims(token); 
	  System.out.println("claims"+token);
	  return claimsResolver.apply(claims); 
	  } 
	  private Claims extractAllClaims(String token)
	  { 
		  return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); 
		  }
	  
	  private Boolean isTokenExpired(String token) { 
		  return extractExpiration(token).before(new Date()); 
		  }
	  
	  public String generateToken(UserDetails userDetails) {
		 
		  Map<String, Object> claims = new HashMap<>();
		  System.out.println("get claims"+claims.toString());
		  return createToken(claims,
	  userDetails.getUsername());
		  }
	  
	  private String createToken(Map<String, Object> claims, String subject) {
		  System.out.println("subject is"+subject);
		  System.out.println("claims"+claims.toString());
	        String obj=
		  
				  Jwts.builder().setClaims(claims).setSubject(subject)
	.setIssuedAt(new Date(System.currentTimeMillis())) 
	.setExpiration(new Date(System.currentTimeMillis() + 100800000 * 60 * 60 * 1))
	 .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
	        System.out.println("claims"+obj.toString());
	        return  obj;
	  }
	  
	  
	  public String createRefreshToken() {
		  String refreshToken = UUID.randomUUID().toString();
		  return refreshToken;
	  }
	  
	  
	  public Boolean validateToken(String token, UserDetails userDetails) { 
		  System.out.println(userDetails.toString());
		  final String username = extractUsername(token); 
		  return (username.equals(userDetails.getUsername()) && !isTokenExpired(token)); 
		  }
	  
	  

	  public static SecretKey getAESKey() throws NoSuchAlgorithmException {
	      KeyGenerator keyGen = KeyGenerator.getInstance("AES");
	      keyGen.init(256, SecureRandom.getInstanceStrong());
	      return keyGen.generateKey();
	  }
	  
	  
	  public String generateUniqueId() {
		  String uniqueId = UUID.randomUUID().toString();
		  System.out.println(uniqueId);
		  return uniqueId;
	  }
	
}
