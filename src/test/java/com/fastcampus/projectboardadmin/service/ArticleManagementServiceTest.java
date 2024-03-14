package com.fastcampus.projectboardadmin.service;

import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.UserAccountDto;
import com.fastcampus.projectboardadmin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardadmin.dto.response.ArticleClientResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@ActiveProfiles("test")
// 스프링 애플리케이션을 테스틑 할 때 사용되는 어노테이션: 특정 프로파일이 활성화되도록 지정
@DisplayName("비즈니스 로직 - 게시글 관리")
class ArticleManagementServiceTest {

    @Disabled("실제 API 호출 결과 관찰용이므로 평상시엔 비활성화")
    @DisplayName("실제 API 호출 테스트")
    @SpringBootTest
    @Nested
    class RealApiTest {
        private final ArticleManagementService sut;
        @Autowired
        public RealApiTest(ArticleManagementService sut) {
            this.sut = sut;
        }
        @DisplayName("게시글 API를 호출하면, 게시글을 가져온다.")
        @Test
        void givenNothing_whenCallingArticleApi_thenReturnsArticleList() {
            // Given
            // When
            List<ArticleDto> result = sut.getArticles();
			// SUT(System Under Test)인 'sut' 객체의 'getArticles()' 메서드를 호출하여 게시글 목록을 가져옵니다.

            // Then
            System.out.println(result.stream().findFirst());
			// 테스트 중간 결과를 출력
			// 이는 테스트 결과를 확인하거나 디버깅할 때 유용
            assertThat(result).isNotNull();
			// 가져온 게시글 목록이 null이 아닌지를 확인하는 검증 구문
			// 만약 nulldㅣ라면 테스트가 실패
        }
    }
    @DisplayName("API mocking 테스트")
    @EnableConfigurationProperties(ProjectProperties.class)
	// 'ProjectProperties' 클래스를 구성 속성으로 활성화합니다. 프로젝트의 구속 속성을 정의
    @AutoConfigureWebClient(registerRestTemplate = true)
	// WebClient를 자동으로 구성하도록 지시
	// 'registerRestTemplate'을 true로 설정하여 RestTemplate을 자동으로 등록하도록 합니다.
    @RestClientTest(ArticleManagementService.class)
    @Nested
	// 중첩된 테스트 클래스를 나타냅니다.
	// 이 클래스는 다른 테스트 클래스 안에 중첩되어 있으며, 해당 클래스의 테스트 메서드들은 부모 테스트 클래스의 설정을 공유
    class RestTemplateTest {
        private final ArticleManagementService sut;
        private final ProjectProperties projectProperties;
        private final MockRestServiceServer server;
        private final ObjectMapper mapper;
        @Autowired
        public RestTemplateTest(
                ArticleManagementService sut,
                ProjectProperties projectProperties,
                MockRestServiceServer server,
                ObjectMapper mapper
        ) {
            this.sut = sut;
            this.projectProperties = projectProperties;
            this.server = server;
            this.mapper = mapper;
        }
        @DisplayName("게시글 목록 API를 호출하면, 게시글들을 가져온다.")
        @Test
        void givenNothing_whenCallingArticlesApi_thenReturnsArticleList() throws Exception {
            // Given
            ArticleDto expectedArticle = createArticleDto("제목", "글");
			// 예상되는 게시글을 생성합니다. 여기서는 제목이 "제목"이고 내용이 "글"인 게시글을 생성
            ArticleClientResponse expectedResponse = ArticleClientResponse.of(List.of(expectedArticle));
			// 예상되는 응답을 생성 이 응답은 예상되는 게시글을 포함
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/articles?size=10000"))
				 	// 예상되는 요청을 기대하도록 지시
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedResponse),
                            MediaType.APPLICATION_JSON
				 	// 예상되는 요청에 대한 응답을 지정
                    ));

            // When
            List<ArticleDto> result = sut.getArticles();
			// 실제로 테스트 대상인 'sut' 객체의 'getArticles()' 메서드를 호출하여 게시글 목록을 가져옵니다.

            // Then
            assertThat(result).first()
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());
			// 가져온 게시글 목록의 첫 번째 게시글이 예상되는 게시글과 일치하는지를 확인하는 검증 구문입니다.
			// 만약 목록의 첫 번째 게시글이 예상되는 게시글과 일치하지 않으면 테스트가 실패합니다.
            server.verify();
			// 기대된 요청이 수행되었는지를 검증
        }
	
        @DisplayName("게시글 ID와 함께 게시글 API을 호출하면, 게시글을 가져온다.")
        @Test
        void givenArticleId_whenCallingArticleApi_thenReturnsArticle() throws Exception {
            // Given
            Long articleId = 1L;
            ArticleDto expectedArticle = createArticleDto("게시판", "글");
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/articles/" + articleId + "?projection=withUserAccount"))
                    .andRespond(withSuccess(
                            mapper.writeValueAsString(expectedArticle),
                            MediaType.APPLICATION_JSON
                    ));
            // When
            ArticleDto result = sut.getArticle(articleId);

            // Then
            assertThat(result)
                    .hasFieldOrPropertyWithValue("id", expectedArticle.id())
                    .hasFieldOrPropertyWithValue("title", expectedArticle.title())
                    .hasFieldOrPropertyWithValue("content", expectedArticle.content())
                    .hasFieldOrPropertyWithValue("userAccount.nickname", expectedArticle.userAccount().nickname());
            server.verify();
        }

        @DisplayName("게시글 ID와 함께 게시글 삭제 API을 호출하면, 게시글을 삭제한다.")
        @Test
        void givenArticleId_whenCallingDeleteArticleApi_thenDeletesArticle() throws Exception {
            // Given
            Long articleId = 1L;
            server
                    .expect(requestTo(projectProperties.board().url() + "/api/articles/" + articleId))
                    .andExpect(method(HttpMethod.DELETE))
                    .andRespond(withSuccess());
            // When
            sut.deleteArticle(articleId);
            // Then
            server.verify();
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
}