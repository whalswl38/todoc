package com.todoc.web.security.oauth.info;

import java.util.Map;

public class KakaoOAuth2UserInfo extends OAuth2UserInfo
{
	public KakaoOAuth2UserInfo(Map<String, Object> attributes)
	{
		super(attributes);
	}
	
	@Override
	public String getName()
	{
		Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
		
		if(response == null) return null;
		return response.get("name").toString();
	}
	
	@Override
	public String getEmail()
	{		
		Map<String, Object> response = (Map<String, Object>) attributes.get("kakao_account");
	
		if(response == null) return null;
		return response.get("email").toString();
	}
}
