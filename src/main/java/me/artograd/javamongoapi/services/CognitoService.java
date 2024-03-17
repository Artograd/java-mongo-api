package me.artograd.javamongoapi.services;

import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminDeleteUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminUpdateUserAttributesRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import me.artograd.javamongoapi.model.User;
import me.artograd.javamongoapi.model.UserAttribute;

@Service
public class CognitoService {
	
	@Value("${aws.cognito.userPoolId}")
    private String userPoolId;
   
	public boolean deleteUserByUsername(String userName) {
        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().build()) {
            AdminDeleteUserRequest deleteRequest = AdminDeleteUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(userName) 
                    .build();
            
            cognitoClient.adminDeleteUser(deleteRequest);
            return true; 
        } catch (Exception e) {
            System.err.println("Error deleting user by sub: " + e.getMessage());
            return false;
        }
    }

    public boolean updateUserAttributes(String userName, List<UserAttribute> attributes) {
        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().build()) {
            List<AttributeType> attributeTypes = new ArrayList<>();
            for (UserAttribute userAttribute : attributes) {
                attributeTypes.add(AttributeType.builder()
                        .name(userAttribute.getName())
                        .value(userAttribute.getValue())
                        .build());
            }
            
            AdminUpdateUserAttributesRequest updateRequest = AdminUpdateUserAttributesRequest.builder()
                    .userPoolId(userPoolId)
                    .username(userName) 
                    .userAttributes(attributeTypes)
                    .build();
            
            cognitoClient.adminUpdateUserAttributes(updateRequest);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating user attributes by username: " + e.getMessage());
            return false;
        }
    }
    
    public User getUserByUsername(String username) {
        try (CognitoIdentityProviderClient cognitoClient = CognitoIdentityProviderClient.builder().build()) {
            AdminGetUserRequest getUserRequest = AdminGetUserRequest.builder()
                    .userPoolId(userPoolId)
                    .username(username)
                    .build();
            
            AdminGetUserResponse getUserResponse = cognitoClient.adminGetUser(getUserRequest);
            List<UserAttribute> userAttrsResult = new ArrayList<>();
            for (AttributeType attr : getUserResponse.userAttributes()) {
                userAttrsResult.add(new UserAttribute(attr.name(), attr.value()));
            }
            return new User(userAttrsResult);
        } catch (Exception e) {
            System.err.println("Error fetching user by username: " + e.getMessage());
            return null;
        }
    }

}
