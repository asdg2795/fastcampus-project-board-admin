package com.fastcampus.projectboardadmin.repository;

import com.fastcampus.projectboardadmin.domain.AdminAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminAccountRepository extends JpaRepository<AdminAccount, String> {
// JpaRepository를 상속받아 UserAccount 엔티티에 대한 데이터베이스 작업을 수행하는 인터페이스를 정의
// 이 인터페이스는 UserAccount 엔티티와 해당 엔티티의 식별자 타입(String)을 사용하여 기본적인 CRUD 작업을 제공
// UserAccountRepository 인터페이스는 UserAccount 엔티티에 대한 다양한 데이터베이스 작업을 쉽게 수행할 수 있드록 해줍니다.
// ex) findById(), save(), deleteById() 등의 메서드를 사용하여 해당 엔티티의 조회, 저장, 삭제 등의 작업을 할 수 있습니다.
}