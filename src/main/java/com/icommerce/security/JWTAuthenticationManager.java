package com.icommerce.security;

import com.icommerce.utils.MockObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class JWTAuthenticationManager implements AuthenticationManager {
  private static final String TOKEN_DETAILS_PROFILE_ID = "profileId";
  private static final String TOKEN_DETAILS_USERNAME = "username";
  private static final String TOKEN_DETAILS_RAW_TOKEN = "rawToken";


  @Override
  public Authentication authenticate(Authentication authentication) {
    String rawAuthToken = authentication.getCredentials().toString();

    // Mock value:
    String username = MockObject.TEST_USER;
    User principal = new User(username, StringUtils.EMPTY, mockGetPermissions(rawAuthToken));

    Map<String, String> tokenDetails = mockTokenDetails(rawAuthToken);
    // Put token details for username context
    UsernamePasswordAuthenticationToken userAuthToken =
            new UsernamePasswordAuthenticationToken(principal, rawAuthToken, mockGetPermissions(rawAuthToken));
    userAuthToken.setDetails(tokenDetails);

    return userAuthToken;
  }

  private List<GrantedAuthority> mockGetPermissions(String rawAuthToken) {
    return new ArrayList<>();
  }

  private Map<String, String> mockTokenDetails(String rawAuthToken) {
    return Map.of(
            TOKEN_DETAILS_PROFILE_ID,
            "profileId",
            TOKEN_DETAILS_USERNAME,
            "test-user",
            TOKEN_DETAILS_RAW_TOKEN,
            rawAuthToken);
  }

}
