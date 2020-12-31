package com.griffin.springboot.config.auth.dto;

import com.griffin.springboot.domain.user.Role;
import com.griffin.springboot.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    static Logger logger = LoggerFactory.getLogger(OAuthAttributes.class);

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map이기때문에 값 하나하나를 전환
    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
           logger.debug("::: registrationId ::: " + registrationId);
        if ("naver".equals(registrationId)) {   // 네이버인지 판단
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String,Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {  //네이버 생성자 추가
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        logger.info(response.toString());
        /*
        {
            "resultCode": "00",
            "message": "success",
            "response": {
                "email": "openapi@naver.com",
                "nickname": "OpenAPI",
                "profile_image": "https://....",
                "age": "40-49",
                "gender": "F",
                "id": "32459886",
                "name": "오픈API",
                "birthday": "10-01"
            }
        }
        * */
        return OAuthAttributes.builder()
                .name((String) response.get("name"))
                .email((String) response.get("email"))
                .picture((String) response.get("profile_image"))
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();

    }

    // User 엔티티 생성
    public User toEntity() {
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST) // 가입할때 기본권한 GUEST
                .build();
    }
}
