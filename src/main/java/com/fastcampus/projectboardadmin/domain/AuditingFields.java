package com.fastcampus.projectboardadmin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
// 엔티티의 생성, 수정, 삭제 등의 이벤트를 감지하여 특정 동작을 수행
@MappedSuperclass
// 엔티티 별로 공통 필드가 존재하는 경우 불 필요한 중복 코드를 제거하기 위해 사용된다.
public abstract class AuditingFields {

    /** 생성일시 */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CreatedDate
    @Column(nullable = false, updatable = false)
 	// 데이터베이스에서 NULL 값을 허용하지 않는다.
    protected LocalDateTime createdAt;

    /** 생성자 */
    @CreatedBy
    @Column(nullable = false, updatable = false, length = 100)
    protected String createdBy;

    /** 수정일시 */
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @LastModifiedDate
    @Column(nullable = false)
    protected LocalDateTime modifiedAt;

    /** 수정자 */
    @LastModifiedBy
    @Column(nullable = false, length = 100)
    protected String modifiedBy;

}