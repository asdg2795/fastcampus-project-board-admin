package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.config.GlobalControllerConfig;
import com.fastcampus.projectboardadmin.config.TestSecurityConfig;
import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.service.ArticleManagementService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("컨트롤러 - 게시글 관리")
@Import({TestSecurityConfig.class, GlobalControllerConfig.class})
@WebMvcTest(ArticleManagementController.class)
class ArticleManagementControllerTest {
    private final MockMvc mvc;
    @MockBean private ArticleManagementService articleManagementService;
    public ArticleManagementControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @WithMockUser(username = "tester", roles = "USER")
	// 이 어노테이션은 현재 테스트에서 사용될 사용자를 지정
    @DisplayName("[view][GET] 게시글 관리 페이지 - 정상 호출")
    @Test
    void givenNothing_whenRequestingArticleManagementView_thenReturnsArticleManagementView() throws Exception {
        // Given
        given(articleManagementService.getArticles()).willReturn(List.of());
		// 'articleManagementService'의 'getArticles()' 메서드가 호출 될 때 반환할 값을 지정, 여기서는 빈 리스트를 반환하도록 지정

        // When & Then
        mvc.perform(get("/management/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("management/articles"))
                .andExpect(model().attribute("articles", List.of()));
				// 응답으로 받은 모델에 "articles"라는 속성이 있고, 그 값이 빈 리스트인지 확인합니다.
        then(articleManagementService).should().getArticles();
		// 메서드가 호출되었는지를 확인
    }

    @WithMockUser(username = "tester", roles = "USER")
    @DisplayName("[data][GET] 게시글 1개 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingArticle_thenReturnsArticle() throws Exception {
        // Given
        Long articleId = 1L;
		// 1번 게시글
        ArticleDto articleDto = createArticleDto("title", "content");
		// 테스트에 사용될 가짜 게시글 데이터를 생성
        given(articleManagementService.getArticle(articleId)).willReturn(articleDto);
		// 'articleManagementService'의 'getArticle()' 메서드가 호출될 때 어떤 값을 반환할지 지정

        // When & Then
        mvc.perform(get("/management/articles/" + articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(articleId))
				// JSON 응답에서 "title" 필드의 값이 'articleDto'의 제목과 일치하는지를 확인
                .andExpect(jsonPath("$.title").value(articleDto.title()))
                .andExpect(jsonPath("$.content").value(articleDto.content()))
                .andExpect(jsonPath("$.userAccount.nickname").value(articleDto.userAccount().nickname()));
				// JSON 응답에서 "userAccount.nickname" 필드의 값이 "articleDto"의 작성자 닉네임과 일치하는지를 확인
        then(articleManagementService).should().getArticle(articleId);
    }

    @WithMockUser(username = "tester", roles = "MANAGER")
    @DisplayName("[view][POST] 게시글 삭제 - 정상 호출")
    @Test
    void givenArticleId_whenRequestingDeletion_thenRedirectsToArticleManagementView() throws Exception {
        // Given
        Long articleId = 1L;
        willDoNothing().given(articleManagementService).deleteArticle(articleId);
		// 'WillDoNothing()' 메서드를 사용하여 'articleManagementservice'의 'deleteArticle()' 메서드가 호출되었을 때 아무런 동작을 하지 않도록 설정

        // When & Then
        mvc.perform(
                        post("/management/articles/" + articleId)
                                .with(csrf())//CSRF공격을 방지하기 위해 웹 애플리케이션에서 사용되는 일반적인 보안 메커니즘
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/management/articles"))
                .andExpect(redirectedUrl("/management/articles"));
        then(articleManagementService).should().deleteArticle(articleId);
    }
    private ArticleDto createArticleDto(String title, String content) {
        return ArticleDto.of(
                1L,
                createUserAccountDto(),
                title,
                content,
                null,
                LocalDateTime.now(),
                "Uno",
                LocalDateTime.now(),
                "Uno"
        );
    }
    private UserAccountDto createUserAccountDto() {
        return UserAccountDto.of(
                "unoTest",
                "uno-test@email.com",
                "uno-test",
                "test memo"
        );
    }
}