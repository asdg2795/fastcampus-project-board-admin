package com.fastcampus.projectboardadmin.config;

import com.fastcampus.projectboardadmin.dto.security.BoardAdminPrincipal;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
// JPA Auditing을 활성화하여 엔티티 객체의 생성 및 수정일자 자동으로 관리할 수 있게 됩니다.
@Configuration
// 스프링 애플리케이션의 설정 정보를 포함하고 있으며, 스프링 애플리케이션 컨텍스트에 빈(bean)을 등록하고 구성하는데 사용
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> Optional.ofNullable(SecurityContextHolder.getContext())
				// 현재 스레드의 SecurityContext를 Optioanl로 감싸고, null인 경우에 대비합니다.
                .map(SecurityContext::getAuthentication)
				// SecurityContext에서 인증(Authentication) 객체를 가져옵니다.
                .filter(Authentication::isAuthenticated)
				// 인증 객체가 인증되어 있는지 확인합니다.
                .map(Authentication::getPrincipal)
				// 안증 객체에서 Principal(주체)을 가져옵니다.
                .map(BoardAdminPrincipal.class::cast)
				// Principal을 BoardAdminPrincipal로 캐스팅합니다.
                .map(BoardAdminPrincipal::getUsername);
				// BoardAdminPrincipal에서 사용자 이름을 가져옵니다.
    }
 // AuditorAware 빈은 현재 사용자의 이름을 추적하여 엔티티의 생성자와 수정자로 설정하는 데 사용됩니다.

}