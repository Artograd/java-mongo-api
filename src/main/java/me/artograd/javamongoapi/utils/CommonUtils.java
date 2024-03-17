package me.artograd.javamongoapi.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtils {

	public static ResponseEntity<?> addCacheHeader(Object result, int seconds ) {
    	HttpHeaders headers = new HttpHeaders();
        headers.setCacheControl("max-age="+seconds);
        
        return ResponseEntity.ok().headers(headers).body(result);
    }
	
	public static String parseToken(HttpServletRequest request) {
		String authorizationHeader = request.getHeader("Authorization");
        String token = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7); // Extract token without "Bearer "
        }
        
        return token;
	}
}
