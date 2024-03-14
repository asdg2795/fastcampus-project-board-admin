	package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.response.ArticleCommentResponse;
import com.fastcampus.projectboardadmin.service.ArticleCommentManagementService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/article-comments")
@Controller
public class ArticleCommentManagementController {

    private final ArticleCommentManagementService articleCommentManagementService;

    @GetMapping
    public String articleComments(Model model) {
        model.addAttribute(
                "comments",
                articleCommentManagementService.getArticleComments().stream().map(ArticleCommentResponse::of).toList()
        );
		// 이 부분은 Model 객체에 "comments"라는 속성을 추가하는 역할
		// 데이터는 articleCommentManagementService.getArticleComments()를 통해 가져옵니다
		// 가져온 댓글들은 'Stream'으로 반환, 그 후 'map(ArticleCommentResponse::of)를 사용하여 각 댓글을 'ArticleCommentResponse' 객체로 변환
		// 마지막으로 'toList()'를 사용하여 'Stream'을 리스트로 변환
        return "management/article-comments";
    }//다건 조회

    @ResponseBody
    @GetMapping("/{articleCommentId}")
    private ArticleCommentResponse articleComment(@PathVariable Long articleCommentId){
        return ArticleCommentResponse.of(articleCommentManagementService.getArticleComment(articleCommentId));
    }//단건 조회

    @PostMapping("/{articleCommentId}")
    public String deleteArticleComment(@PathVariable Long articleCommentId){
        articleCommentManagementService.deleteArticleComment(articleCommentId);

        return "redirect:/management/article-comments";
    }// 삭제

}