package me.artograd.javamongoapi.controllers;

import me.artograd.javamongoapi.model.Tender;
import me.artograd.javamongoapi.model.User;
import me.artograd.javamongoapi.model.UserAttribute;
import me.artograd.javamongoapi.model.system.UserTokenClaims;
import me.artograd.javamongoapi.services.CognitoService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
    private CognitoService cognitoService;
    
    @GetMapping("/{username}")
    public ResponseEntity<?> getUserAttributesByUsername(@PathVariable String username) {
        User user = cognitoService.getUserByUsername(username);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("/{username}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deleteUserById(@PathVariable String username, HttpServletRequest request) {
    	
    	if ( isDenied(username, request) ) { return new ResponseEntity<>(HttpStatus.FORBIDDEN);}
    	
        boolean success = cognitoService.deleteUserByUsername(username);
        if (success) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>("Error deleting user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{username}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> updateUserAttributesById(@PathVariable String username, @RequestBody List<UserAttribute> attributes, HttpServletRequest request) {
    	
    	if ( isDenied(username, request) ) { return new ResponseEntity<>(HttpStatus.FORBIDDEN);}
    	
        boolean success = cognitoService.updateUserAttributes(username, attributes);
        if (success) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Error updating user attributes", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * Check that operation is executed by profile owner 
     * @param tenderId
     * @param request
     * @return
     */
    private boolean isDenied(String username, HttpServletRequest request) {
    	UserTokenClaims claims = cognitoService.getUserTokenClaims(request);
    	if ( claims.getUsername() == null || //username is not provided in token
    		!claims.getUsername().equals( username )) {//request is made on behalf of different user
    		return true;
    	}
    	return false;
    }
}
