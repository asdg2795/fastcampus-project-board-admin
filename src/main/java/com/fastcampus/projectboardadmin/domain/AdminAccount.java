package com.fastcampus.projectboardadmin.domain;
import com.fastcampus.projectboardadmin.domain.constant.RoleType;
import com.fastcampus.projectboardadmin.domain.converter.RoleTypesConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
@Getter
@ToString(callSuper = true)
// 해당 클래스의 객체를 문자열로 출력할 때 부모 클래스의 정보도 함께 출력한다.
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt"),
        @Index(columnList = "createdBy")
})
// @Table은 엔티티 클래스가 매핑되는 테이블을 설정하는데, indexes속성을 사용하여 해당 테이블에 인덱스 추가
@Entity
// 데이터베이스 테이블과 매핑되는 엔티티
public class AdminAccount extends AuditingFields {
    @Id
    // 이 어노테이션을 필드 위에 붙이면 해당 필드가 엔티티의 식별자로 사용되는 것을 나타냄
    @Column(length = 50)
    private String userId;
    @Setter @Column(nullable = false) private String userPassword;
    @Convert(converter = RoleTypesConverter.class)
    // @Convert는 엔티티 클래스의 특정 필드를 데이터베이스에 저장하기 전에 변환을 수행할 때 사용
    // converter속성은 변환기 클래스를 지정하는데 엔티티 필드의 값을 데이터베이스에 저장할 때 변환하고, 데이터베이스에서 읽을 때는 다시 역변환하여 필드 값으로 변환됩니다.
    @Column(nullable = false)
    private Set<RoleType> roleTypes = new LinkedHashSet<>();
    // 'roleTypes'는 역할 유형을 담는데 사용되는 집합(Set)으로, 중복된 값이 없으며 입력된 순서가 유지
    // 이는 역할(Role)을 중복 없이 저장하고, 특정 순서로 접근할 수 있게 해줍니다.
    @Setter @Column(length = 100) private String email;
    @Setter @Column(length = 100) private String nickname;
    @Setter private String memo;


    protected AdminAccount() {}

    private AdminAccount(String userId, String userPassword, Set<RoleType> roleTypes, String email, String nickname, String memo, String createdBy) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.roleTypes = roleTypes;
        this.email = email;
        this.nickname = nickname;
        this.memo = memo;
        this.createdBy = createdBy;
        this.modifiedBy = createdBy;
    }

    public static AdminAccount of(String userId, String userPassword, Set<RoleType> roleTypes, String email, String nickname, String memo) {
        return AdminAccount.of(userId, userPassword, roleTypes, email, nickname, memo, null);
    }

    public static AdminAccount of(String userId, String userPassword, Set<RoleType> roleTypes, String email, String nickname, String memo, String createdBy) {
        return new AdminAccount(userId, userPassword, roleTypes, email, nickname, memo, createdBy);
    }

    public void addRoleType(RoleType roleType) {
        this.getRoleTypes().add(roleType);
    }
    // 단건 저장

    public void addRoleTypes(Collection<RoleType> roleTypes) {
        this.getRoleTypes().addAll(roleTypes);
    }
    // 다건 저장

    public void removeRoleType(RoleType roleType) {
        this.getRoleTypes().remove(roleType);
    }
    // 제거 기능

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdminAccount that)) return false;
        return this.getUserId() != null && this.getUserId().equals(that.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getUserId());
    }
}