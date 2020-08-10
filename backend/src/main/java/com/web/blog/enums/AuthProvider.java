package com.web.blog.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum AuthProvider {
  GOOGLE("Google", "구글"), GITHUB("GitHub", "깃허브"), NAVER("Naver","네이버"), KAKAO("Kakao", "카카오"), EMPTY("No Data", "없음");

  private final String key;
  private final String value;

  public static AuthProvider getAuthProviderValue(String provider){
    return Arrays.stream(AuthProvider.values()).filter(authProvider -> authProvider.hasAuthProvider(provider)).findAny().orElse(EMPTY);
  }

  public boolean hasAuthProvider(String provider){
    return key.equals(provider);
  }
}
