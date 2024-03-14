package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.response.AdminAccountResponse;
import com.fastcampus.projectboardadmin.service.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class AdminAccountController {

    private final AdminAccountService adminAccountService;

    @GetMapping("/admin/members")
    public String members() {
        return "admin/members";
    }

    @ResponseBody
	// 메서드가 반환하는 객체를 HTTP 응답의 본문(body)으로 사용함을 나타낸다.
    @GetMapping("/api/admin/members")
    public List<AdminAccountResponse> getMembers() {
        return adminAccountService.users().stream()
		// adminAccountService를 사용하여 관리자 계정 정보를 가져온다.
                .map(AdminAccountResponse::from)
				// 가져온 각 관리자 계정 정보를 AdminAccountResponse 객체로 변환
                .toList();
				// 변환된 객체들을 리스트로 수집하여 반환
    }

    @ResponseStatus(HttpStatus.NO_CONTENT) // httpstatus를 특정한 곳으로 지정하는 것
    @ResponseBody
    @DeleteMapping("/api/admin/members/{userId}")
    public void delete(@PathVariable String userId) {
        adminAccountService.deleteUser(userId);
    }

}