package com.fastcampus.projectboardadmin.service;

import com.fastcampus.projectboardadmin.dto.ArticleDto;
import com.fastcampus.projectboardadmin.dto.properties.ProjectProperties;
import com.fastcampus.projectboardadmin.dto.response.ArticleClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ArticleManagementService {

    private final RestTemplate restTemplate;
    private final ProjectProperties projectProperties;


    public List<ArticleDto> getArticles() {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/articles")
		// 'UriComponentsBuilder'르ㄹ 사용하여 요청할 URI를 생성
		// 'projectProperties.board().url()' 은 게시판 서비스의 URL을 가져오는 메서드를 호출하여 게시판 서비스의 기본 URL을 가져옵니다.
		// 그리고 "/api/articles"를 추가하여 게시물 정보를 요청할 API의 경로를 지정
				.queryParam("size", 10000) // TODO: 전체 게시글을 가져오기 위해 충분히 큰 사이즈를 전달하는 방식.       
				// "size"를 키로 하고 값으로 10000을 전달하여 한 번에 최대 10000개의 게시물을 가져오도록 요청(불완전하다).
				.build()
				.toUri();
				// 사용하여 최종적인 URI를 생성
        ArticleClientResponse response = restTemplate.getForObject(uri, ArticleClientResponse.class);
		// 첫 번쨰 파라미터인 'uri'는 요청할 서버의 URI를 나타냅니다.
		// 두 번쨰 파라미터인 'ArticleClientReponse.class'는 요청의 응답을 어떤 클래스로 변환 할지를 나타냅니다.
		// 'getForObject' 메서드는 요청을 보내고, 서버로부터 받은 응답을 주어진 클래스 형식으로 변환하여 반환됩니다.

        return Optional.ofNullable(response).orElseGet(ArticleClientResponse::empty).articles();
		// 응답이 null이 아니면 해당 응답을, null 이면 빈'ArticleClientResponse' 객체를 반환
		// 그 후 에는 'ArticleClientReponse' 객체의 'articles()' 메서드를 호출하여 게시물 리스트를 반환합니다.
		
    }

    public ArticleDto getArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/articles/" + articleId)
                .queryParam("projection", "withUserAccount")
                .build()
                .toUri();
        ArticleDto response = restTemplate.getForObject(uri, ArticleDto.class);

        return Optional.ofNullable(response)
                .orElseThrow(() -> new NoSuchElementException("게시글이 없습니다 - articleId: " + articleId));
    }

    public void deleteArticle(Long articleId) {
        URI uri = UriComponentsBuilder.fromHttpUrl(projectProperties.board().url() + "/api/articles/" + articleId)
                .build()
                .toUri();
        restTemplate.delete(uri);
    }

}