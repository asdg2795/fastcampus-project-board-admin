package com.fastcampus.projectboardadmin.repository;

import com.fastcampus.projectboardadmin.domain.AdminAccount;
import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import static org.assertj.core.api.Assertions.assertThat;
@DisplayName("JPA 연결 테스트")
@Import(JpaRepositoryTest.TestJpaConfig.class)
@DataJpaTest
class JpaRepositoryTest {

    private final AdminAccountRepository adminAccountRepository;

    public JpaRepositoryTest(@Autowired AdminAccountRepository adminAccountRepository) {
        this.adminAccountRepository = adminAccountRepository;
    }

    @DisplayName("회원 정보 select 테스트")
    @Test
    void givenAdminAccounts_whenSelecting_thenWorksFine() {
        // Given

        // When
        List<AdminAccount> adminAccounts = adminAccountRepository.findAll();
		// findAll()을 호출하여 모든 사용자 계정을 가져옵니다.

        // Then
        assertThat(adminAccounts)
                .isNotNull()
                .hasSize(4);
		// adminAccounts가 null이 아니고 크기가 4인지를 검사합니다.
		// 만약 사용자 계정이 정확히 4개라면 테스트가 성공
    }
    @DisplayName("회원 정보 insert 테스트")
    @Test
    void givenAdminAccount_whenInserting_thenWorksFine() {
        // Given
        long previousCount = adminAccountRepository.count();
		// 이전 회원 수를 가져옵니다.
        AdminAccount adminAccount = AdminAccount.of("test", "pw", Set.of(RoleType.DEVELOPER), null, null, null);
		// 새로운 계정을 생성합니다.		

        // When
        adminAccountRepository.save(adminAccount)
		// 사용자 계정을 저장합니다.
		// 테스트 대상이 샐행되는 부분, 생성한 사용자 계정을 데이터베이스에 저장

        // Then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount + 1);
		// 회원 수가 1 증가했는지를 검증합니다.
		// 예상 결과를 검증하는 부분, 이전 회원 수에 1을 더한 값과 현재의 회원 수를 비교하여 회원 수가 1 증가했는지를 검증
    }

    @DisplayName("회원 정보 update 테스트")
    @Test
    void givenAdminAccountAndRoleType_whenUpdating_thenWorksFine() {
        // Given
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("uno");
		// ID가 "uno"인 사용자 계쩡을 가져옵니다.
        adminAccount.addRoleType(RoleType.DEVELOPER);
		// DEVELOPER 역할을 추가
        adminAccount.addRoleTypes(List.of(RoleType.USER, RoleType.USER));
		// USER를 두 개 입력하여 들어갈 때 중복 체크
		// 두 개 입력되어도 하나만 들어가야된다.
        adminAccount.removeRoleType(RoleType.ADMIN);
		// ADMIN 역할을 제거

        // When
        AdminAccount updatedAccount = adminAccountRepository.saveAndFlush(adminAccount);
    	// 사용자 계정을 업데이트하고 데이터베이스에 반영

        // Then
        assertThat(updatedAccount)
                .hasFieldOrPropertyWithValue("userId", "uno")
				 // 사용자 ID가 "uno"인지 검증
                .hasFieldOrPropertyWithValue("roleTypes", Set.of(RoleType.DEVELOPER, RoleType.USER));
				 // 업데이트된 사용자 계쩡의 역할이 개발자와 사용자로 올바르게 업데이트 되었는지를 확인
    }

    @DisplayName("회원 정보 delete 테스트")
    @Test
    void givenAdminAccount_whenDeleting_thenWorksFine() {
        // Given
        long previousCount = adminAccountRepository.count();
		// 이전 회원 수를 가져옵니다.
        AdminAccount adminAccount = adminAccountRepository.getReferenceById("uno");
		// ID가 "uno"인지 사용자 계정을 가져옵니다.
        
		// When
        adminAccountRepository.delete(adminAccount);
		// 사용자 계정을 삭제

        // Then
        assertThat(adminAccountRepository.count()).isEqualTo(previousCount - 1);
		// 회원 수가 1 감소했는지를 검증합니다.
    }


    @EnableJpaAuditing
    @TestConfiguration
    static class TestJpaConfig {
        @Bean
        AuditorAware<String> auditorAware() {
            return () -> Optional.of("uno");
        }
		// 항상 "uno"를 포함한 Optional 객체를 반환
		// 이는 테스트에서 사용되는 가짜 구현체로, 사용자 이름이 항상 "uno"로 설정
    }
}