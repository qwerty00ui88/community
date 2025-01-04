<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container view-post-container">
	<!-- 게시글 -->
    <h2 class="post-title">${boardDTO.post.title}</h2>
    <div class="post-meta text-muted mb-4">
        <span>작성자: 
	        <c:choose>
		        <c:when test="${boardDTO.writer.status == 'DELETED'}">
		            <strong>탈퇴회원</strong>
		        </c:when>
		        <c:otherwise>
		            <strong>${boardDTO.writer.nickname}</strong>
		        </c:otherwise>
		    </c:choose>
        </span> | 
        <span>작성일: <strong>${fn:substring(boardDTO.post.createdAt.toString(), 0, 10)}</strong></span>
        <c:if test="${boardDTO.post.status == 'EDITED'}">
            <span class="text-muted">(edited)</span>
        </c:if>
        | <span>조회수: <strong>${boardDTO.post.views}</strong></span>
    </div>
    
    <!-- 게시글 수정 및 삭제 버튼 -->
    <div class="text-right mb-4">
	    <c:if test="${boardDTO.post.userId == LOGIN_USER_ID || (LOGIN_ADMIN_ID != null && boardDTO.post.userId == LOGIN_ADMIN_ID)}">
	        <button id="updatePostBtn" class="btn btn-outline-primary mr-2">게시글 수정</button>
	    </c:if>
	    <c:if test="${boardDTO.post.userId == LOGIN_USER_ID || LOGIN_ADMIN_ID != null}">
	        <button id="deletePostBtn" class="btn btn-outline-danger">게시글 삭제</button>
	    </c:if>
	</div>
    <div class="post-content">
		<c:forEach var="item" items="${boardDTO.fileList}">
		    <c:choose>
		    	<c:when test="${fn:contains(item.metadata, 'image/')}">
		            <img src="${item.url}" alt="Image" style="max-width: 100%; height: auto;">
		        </c:when>
		        <c:otherwise>
		        	<a href="${item.url}" target="_blank" download="${item.metadataObject.originalFilename}">${item.metadataObject.originalFilename}</a>
		        </c:otherwise>
			</c:choose>
		</c:forEach>
        <p>${boardDTO.post.contents}</p>
    </div>
    <hr>
    
    <!-- 댓글 및 대댓글 -->
    <div class="comments">
        <h5>댓글</h5>
        <c:forEach items="${boardDTO.commentList}" var="comment">
            <div class="comment mb-3" data-comment-id="${comment.id}">
                <div class="comment-meta text-muted">
                    <span>
                    <c:choose>
				        <c:when test="${comment.writer.status == 'DELETED'}">
				            <strong>탈퇴회원</strong>
				        </c:when>
				        <c:otherwise>
				            <strong>${comment.writer.nickname}</strong>
				        </c:otherwise>
				    </c:choose>
                    </span> | 
                    <span><strong><c:out value="${fn:substring(comment.createdAt.toString(), 0, 10)}"/></strong></span>
                    <c:if test="${comment.status == 'EDITED'}">
                        <span class="text-muted">(edited)</span>
                    </c:if>
                    <!-- 댓글 수정 및 삭제 버튼 -->
                    <div class="float-right">
					    <c:if test="${(comment.writer.id == LOGIN_USER_ID || comment.writer.id == LOGIN_ADMIN_ID) && comment.status != 'DELETED'}">
					        <button class="btn btn-sm text-secondary edit-comment-btn" data-comment-id="${comment.id}">수정</button>
					    </c:if>
					    <c:if test="${(comment.writer.id == LOGIN_USER_ID || LOGIN_ADMIN_ID != null) && comment.status != 'DELETED'}">
					        <button class="btn btn-sm text-danger delete-comment-btn" data-comment-id="${comment.id}">삭제</button>
					    </c:if>
					</div>
                </div>
                <p class="comment-content">
				    <c:choose>
				        <c:when test="${comment.status == 'DELETED'}">
				            ⚠ 삭제된 댓글입니다
				        </c:when>
				        <c:otherwise>
				            ${comment.contents}
				        </c:otherwise>
				    </c:choose>
				</p>
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
                                <span>
                                <c:choose>
							        <c:when test="${reply.writer.status == 'DELETED'}">
							            <strong>탈퇴회원</strong>
							        </c:when>
							        <c:otherwise>
							            <strong>${reply.writer.nickname}</strong>
							        </c:otherwise>
							    </c:choose>
                                </span> | 
                                <span><strong>${fn:substring(reply.createdAt.toString(), 0, 10)}</strong></span>
                                <c:if test="${reply.status == 'EDITED'}">
                                    <span class="text-muted">(edited)</span>
                                </c:if>
                                <!-- 대댓글 수정 및 삭제 버튼 -->
                                <div class="float-right">
								    <c:if test="${(reply.writer.id == LOGIN_USER_ID || reply.writer.id == LOGIN_ADMIN_ID) && reply.status != 'DELETED'}">
								        <button class="btn btn-sm text-secondary edit-reply-btn" data-reply-id="${reply.id}">수정</button>
								    </c:if>
								    <c:if test="${(reply.writer.id == LOGIN_USER_ID || LOGIN_ADMIN_ID != null) && reply.status != 'DELETED'}">
								        <button class="btn btn-sm text-danger delete-reply-btn" data-reply-id="${reply.id}">삭제</button>
								    </c:if>
								</div>
                            </div>
                            <p class="reply-content">
							    <c:choose>
							        <c:when test="${reply.status == 'DELETED'}">
							            ⚠ 삭제된 대댓글입니다
							        </c:when>
							        <c:otherwise>
							            ${reply.contents}
							        </c:otherwise>
							    </c:choose>
							</p>
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
        
        <!-- 댓글 작성 폼 -->
        <form class="comment-form">
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
    const isAdmin = ${LOGIN_ADMIN_ID != null};    
    
    // 게시글 수정
    $("#updatePostBtn").on("click", function() {
        location.href = "/board/update/" + postId;
    });

    // 게시글 삭제
    $("#deletePostBtn").on("click", function() {
        if (confirm("정말로 게시글을 삭제하시겠습니까?")) {
            $.ajax({
                url: isAdmin ? `/api/post/admin/\${postId}` : `/api/post/\${postId}`,
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
                url: isAdmin ? `/api/comment/admin/\${commentId}` : `/api/comment/\${commentId}`,
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
                url: isAdmin ? `/api/comment/admin/\${replyId}` : `/api/comment/\${replyId}`,
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
<style>
	.post-content,
	.post-title {
		word-wrap: break-word;
	    white-space: pre-wrap;
	}
	
	.post-item-title {
		flex-grow: 1;
	    margin-left: 10px;
	    white-space: nowrap;
	    overflow: hidden;
	    text-overflow: ellipsis;
	}
</style>
