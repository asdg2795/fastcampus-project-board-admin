package com.fastcampus.projectboardadmin.dto.security;

import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.dto.AdminAccountDto;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
public record BoardAdminPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        Map<String, Object> oAuth2Attributes
) implements UserDetails, OAuth2User {
    public static BoardAdminPrincipal of(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return BoardAdminPrincipal.of(username, password, roleTypes, email, nickname, memo, Map.of());
    }
    // OAuth2 인증 정보는 빈 Map으로 초기화됩니다.

    public static BoardAdminPrincipal of(String username, String password, Set<RoleType> roleTypes, String email, String nickname, String memo, Map<String, Object> oAuth2Attributes) {
        return new BoardAdminPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getRoleName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet())
                ,
                email,
                nickname,
                memo,
                oAuth2Attributes
        );
    }
    // 이전과 같은 인자를 받으면서 OAuth2 인증 정보를 추가로 받습니다.

    public static BoardAdminPrincipal from(AdminAccountDto dto) {
        return BoardAdminPrincipal.of(
                dto.userId(),
                dto.userPassword(),
                dto.roleTypes(),
                dto.email(),
                dto.nickname(),
                dto.memo()
        );
    }
    // UserAccountDto를 BoardAdminPrincipal로 변환하는 역할을 합니다.
    // UserAccountDto 객체를 받아서 해당 객체의 필드들을 이용하여 BoardAdminPrincipal 객체를 생성
  

    public AdminAccountDto toDto() {
        return AdminAccountDto.of(
                username,
                password,
                authorities.stream(
		// authorities 필드를 Stream으로 변환한 후,
                        .map(GrantedAuthority::getAuthority)
			 // 각 GrantedAuthority에서 권한이름으로 매핑한 다음
                        .map(RoleType::valueOf)
			 // RoleType으로 다시 변환
			 // 이렇게 하면 BoardAdminPrincipal의 authorities를 RoleType집합으로 변환할 수 있다.
                        .collect(Collectors.toUnmodifiableSet())
			 // collect(Collectors.toUnmodifiableSet())를 사용하여 roleTypes를 수정할 수 없는 불변한 Set으로 만든다.
                ,
                email,
                nickname,
                memo
        );
    }
    // 결과적으로, 'toDto' 메서드를 호출하면 'BoardAdminPrincipal' 객체가 UserAccountDto 객체로 변환되어 반환됩니다.
    // 이를 통해 두 객체 간의 변환을 간편하게 수행 

    @Override public String getUsername() { return username; }
    @Override public String getPassword() { return password; }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
    @Override public Map<String, Object> getAttributes() { return oAuth2Attributes; }
    @Override public String getName() { return username; }
}