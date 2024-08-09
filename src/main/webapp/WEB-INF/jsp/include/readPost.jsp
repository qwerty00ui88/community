<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<div class="container view-post-container">
    <h2>${boardDTO.post.title}</h2>
    <div class="post-meta text-muted mb-4">
        <span>작성자: <strong>${boardDTO.writer.nickname}</strong></span> | <span>작성일:
            <strong>${fn:substring(boardDTO.post.createdAt.toString(), 0, 10)}</strong>
        </span> | <span>조회수: <strong>${boardDTO.post.views}</strong></span>
    </div>
    <!-- 게시글 수정 및 삭제 버튼 -->
    <c:if test="${boardDTO.post.userId == LOGIN_USER_ID || boardDTO.post.userId == LOGIN_ADMIN_ID}">
	    <div class="text-right mb-4">
	        <button id="updatePostBtn" class="btn btn-outline-primary mr-2">게시글 수정</button>
	        <button id="deletePostBtn" class="btn btn-outline-danger">게시글 삭제</button>
	    </div>
    </c:if>
    <div class="post-content">
        <p>${boardDTO.post.contents}</p>
    </div>
    <hr>
    <div class="comments">
        <h5>댓글</h5>
        <c:forEach items="${boardDTO.commentList}" var="comment">
            <div class="comment mb-3" data-comment-id="${comment.id}">
                <div class="comment-meta text-muted">
                    <span><strong>${comment.writer.nickname}</strong></span> | <span><strong><c:out value="${fn:substring(comment.createdAt.toString(), 0, 10)}"/></strong></span>
                    <c:if test="${comment.writer.id == LOGIN_USER_ID || comment.writer.id == LOGIN_ADMIN_ID}">
	                    <div class="float-right">
	                        <button class="btn btn-sm text-secondary edit-comment-btn" data-comment-id="${comment.id}">수정</button>
	                        <button class="btn btn-sm text-danger delete-comment-btn" data-comment-id="${comment.id}">삭제</button>
	                    </div>
                    </c:if>
                </div>
                <p class="comment-content">${comment.contents}</p>
                <div class="edit-comment-form" style="display: none;">
                    <div class="form-group">
                        <textarea class="form-control edit-comment-content" rows="2">${comment.contents}</textarea>
                    </div>
                    <button class="btn btn-primary-custom update-comment-btn" data-comment-id="${comment.id}">수정하기</button>
                    <button class="btn btn-secondary cancel-edit-btn">취소</button>
                </div>
                <button class="reply-btn text-primary btn-link mb-2" data-comment-id="${comment.id}" style="background: none; border: none; padding: 0; cursor: pointer;">대댓글 작성</button>
                <div class="replies ml-4">
                    <c:forEach items="${comment.replyList}" var="reply">
                        <div class="reply mb-3" data-reply-id="${reply.id}">
                            <div class="reply-meta text-muted">
                                <span><strong>${reply.writer.nickname}</strong></span> | <span><strong>${fn:substring(reply.createdAt.toString(), 0, 10)}</strong></span>
                                <c:if test="${reply.writer.id == LOGIN_USER_ID || reply.writer.id == LOGIN_ADMIN_ID}">
	                                <div class="float-right">
	                                    <button class="btn btn-sm text-secondary edit-reply-btn" data-reply-id="${reply.id}">수정</button>
	                                    <button class="btn btn-sm text-danger delete-reply-btn" data-reply-id="${reply.id}">삭제</button>
	                                </div>
                                </c:if>
                            </div>
                            <p class="reply-content">${reply.contents}</p>
                            <div class="edit-reply-form" style="display: none;">
                                <div class="form-group">
                                    <textarea class="form-control edit-reply-content" rows="2">${reply.contents}</textarea>
                                </div>
                                <button class="btn btn-primary-custom update-reply-btn" data-reply-id="${reply.id}">수정하기</button>
                                <button class="btn btn-secondary cancel-edit-reply-btn">취소</button>
                            </div>
                        </div>
                    </c:forEach>
                    <!-- 대댓글 작성 폼 -->
					<form class="reply-form" style="display: none;" data-parent-id="${comment.id}">
					    <div class="form-group">
					        <textarea class="form-control reply-content" rows="2" placeholder="대댓글 내용을 입력하세요" required></textarea>
					    </div>
					    <button type="submit" class="btn btn-primary-custom">대댓글 작성</button>
					    <button type="button" class="btn btn-secondary cancel-reply-btn">취소</button>
					</form>
                </div>
            </div>
        </c:forEach>
        <form class="comment-form" action="createComment" method="post">
            <div class="form-group">
                <label for="commentContent">댓글 작성</label>
                <textarea class="form-control" id="commentContent" name="commentContent" rows="3" placeholder="댓글 내용을 입력하세요" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary-custom">댓글 작성</button>
        </form>
    </div>
</div>


<script>
$(document).ready(function() {
    const postId = ${boardDTO.post.id};

    // 게시글 수정
    $("#updatePostBtn").on("click", function() {
        window.location.href = "/board/update/" + postId;
    });

    // 게시글 삭제
    $("#deletePostBtn").on("click", function() {
        if (confirm("정말로 게시글을 삭제하시겠습니까?")) {
            $.ajax({
                url: "/api/post/" + postId,
                type: "DELETE",
                success: function(response) {
                    alert("게시글이 성공적으로 삭제되었습니다.");
                    window.location.href = "/";
                },
                error: function(xhr, status, error) {
                    alert("게시글 삭제 중 오류가 발생했습니다: " + error);
                }
            });
        }
    });

    // 댓글 및 대댓글 작성
    $(".comment-form, .reply-form").on("submit", function(e) {
        e.preventDefault();
        const parentId = $(this).hasClass('reply-form') ? $(this).data("parent-id") : null;
        const content = parentId ? $(this).find(".reply-content").val() : $("#commentContent").val();

        if (!content.trim()) {
            alert("내용을 입력하세요.");
            return;
        }

        $.post("/api/comment", {
            "parentId": parentId,
            "contents": content,
            "postId": postId
        }).done(function(response) {
            alert("댓글이 성공적으로 작성되었습니다.");
            location.reload();
        }).fail(function(xhr, status, error) {
            alert("댓글 작성 중 오류가 발생했습니다: " + error);
        });
    });

    // 대댓글 작성 버튼 클릭 시 대댓글 작성 폼 토글 표시
    $(".reply-btn").on("click", function() {
        const commentId = $(this).data("comment-id");
        const replyForm = $("form.reply-form[data-parent-id='" + commentId + "']");
        
        // 토글 기능: 폼이 보여진 상태면 숨기고, 숨겨진 상태면 보여줌
        replyForm.slideToggle();
        if (replyForm.is(":visible")) {
            $('html, body').animate({
                scrollTop: replyForm.offset().top
            }, 500);
        }
    });

    // 대댓글 작성 취소 버튼 클릭 시 폼 숨기기
    $(".cancel-reply-btn").on("click", function() {
        const replyForm = $(this).closest("form.reply-form");
        replyForm.slideUp();
    });

    // 댓글 삭제
    $(".delete-comment-btn").on("click", function() {
        const commentId = $(this).data("comment-id");
        if (confirm("정말로 댓글을 삭제하시겠습니까?")) {
            $.ajax({
                url: "/api/comment/" + commentId,
                type: "DELETE",
                success: function(response) {
                    alert("댓글이 성공적으로 삭제되었습니다.");
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("댓글 삭제 중 오류가 발생했습니다: " + error);
                }
            });
        }
    });

    // 대댓글 삭제
    $(".delete-reply-btn").on("click", function() {
        const replyId = $(this).data("reply-id");
        if (confirm("정말로 대댓글을 삭제하시겠습니까?")) {
            $.ajax({
                url: "/api/comment/" + replyId,
                type: "DELETE",
                success: function(response) {
                    alert("대댓글이 성공적으로 삭제되었습니다.");
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("대댓글 삭제 중 오류가 발생했습니다: " + error);
                }
            });
        }
    });

    // 댓글 및 대댓글 수정 UI 표시
    $(".edit-comment-btn, .edit-reply-btn").on("click", function() {
        const isReply = $(this).hasClass('edit-reply-btn');
        const id = isReply ? $(this).data("reply-id") : $(this).data("comment-id");
        const targetDiv = isReply ? $('.reply[data-reply-id="' + id + '"]') : $('.comment[data-comment-id="' + id + '"]');
        const contentElement = targetDiv.find(isReply ? ".reply-content" : ".comment-content");
        const editForm = isReply ? targetDiv.find(".edit-reply-form") : targetDiv.find(".edit-comment-form");
        
        contentElement.hide();
        editForm.show();
    });
    
    // 댓글 및 대댓글 수정 취소
    $(".cancel-edit-btn, .cancel-edit-reply-btn").on("click", function() {
        const targetDiv = $(this).closest(".comment, .reply");
        const contentElement = targetDiv.hasClass('reply') ? targetDiv.find(".reply-content") : targetDiv.find(".comment-content");
        const editFormElement = targetDiv.hasClass('reply') ? targetDiv.find(".edit-reply-form") : targetDiv.find(".edit-comment-form");

        contentElement.show();
        editFormElement.hide();
    });

    // 댓글 수정
    $(".update-comment-btn").on("click", function() {
        const commentId = $(this).data("comment-id");
        const newContent = $(this).closest(".edit-comment-form").find(".edit-comment-content").val();
        
        if (!newContent.trim()) {
            alert("내용을 입력하세요.");
            return;
        }

        $.ajax({
            url: "/api/comment/" + commentId,
            type: "PUT",
            data: {
                "contents": newContent
            },
            success: function(response) {
                alert("댓글이 성공적으로 수정되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("댓글 수정 중 오류가 발생했습니다: " + error);
            }
        });
    });

    // 대댓글 수정
    $(".update-reply-btn").on("click", function() {
        const replyId = $(this).data("reply-id");
        const newReplyContent = $(this).closest(".edit-reply-form").find(".edit-reply-content").val();

        if (!newReplyContent.trim()) {
            alert("내용을 입력하세요.");
            return;
        }

        $.ajax({
            url: "/api/comment/" + replyId,
            type: "PUT",
            data: {
                "contents": newReplyContent
            },
            success: function(response) {
                alert("대댓글이 성공적으로 수정되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("대댓글 수정 중 오류가 발생했습니다: " + error);
            }
        });
    });
});
</script>
