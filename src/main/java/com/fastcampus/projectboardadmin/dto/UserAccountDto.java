ㅇpackage com.fastcampus.projectboardadmin.dto;

import java.time.LocalDateTime;

public record UserAccountDto(
        String userId,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy
) {

    public static UserAccountDto of(String userId, String email, String nickname, String memo) {
        return UserAccountDto.of(userId, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto of(String userId, String email, String nickname, String memo, LocalDateTime createdAt, String createdBy, LocalDateTime modifiedAt, String modifiedBy) {
        return new UserAccountDto(userId, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

	// 사용자는 필요에 따라 간편하게 UserAccountDto 객체를 생성할 수 있습니다.
	// 필요한 정보만을 인자로 전달하여 객체를 생성할 수 있고, 나머지는 필요에 따라 넘겨줄 수 있습니다.

}