<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container view-post-container">
	<h2>${boardDTO.post.title}</h2>
	<div class="post-meta text-muted mb-4">
		<span>작성자: <strong>${boardDTO.writer.nickname}</strong></span> | <span>작성일:
			<strong>${boardDTO.post.createdAt}</strong>
		</span> | <span>조회수: <strong>${boardDTO.post.views}</strong></span>
	</div>
	<!-- 게시글 수정 및 삭제 버튼 -->
	<div class="text-right mb-4">
		<button id="updatePostBtn" class="btn btn-outline-primary mr-2">게시글
			수정</button>
		<button id="deletePostBtn" class="btn btn-outline-danger">게시글
			삭제</button>
	</div>
	<div class="post-content">
		<p>${boardDTO.post.contents}</p>
	</div>
	<hr>
	<div class="comments">
		<h5>댓글</h5>
		<div class="comment mb-3">
			<div class="comment-meta text-muted">
				<span>작성자: <strong>댓글 작성자명</strong></span> | <span>작성일: <strong>2024-01-02</strong></span>
			</div>
			<p>여기에 댓글 내용이 표시됩니다.</p>
			<button class="btn btn-secondary-custom mb-2">대댓글 작성</button>
			<div class="replies ml-4" style="display: none;">
				<form action="addReply" method="post">
					<div class="form-group">
						<label for="replyContent">대댓글 작성</label>
						<textarea class="form-control" id="replyContent"
							name="replyContent" rows="2" placeholder="대댓글 내용을 입력하세요" required></textarea>
					</div>
					<button type="submit" class="btn btn-secondary-custom">대댓글
						작성</button>
				</form>
				<div class="reply mb-3">
					<div class="reply-meta text-muted">
						<span>작성자: <strong>대댓글 작성자명</strong></span> | <span>작성일: <strong>2024-01-03</strong></span>
					</div>
					<p>여기에 대댓글 내용이 표시됩니다.</p>
				</div>
			</div>
		</div>
		<form action="addComment" method="post">
			<div class="form-group">
				<label for="commentContent">댓글 작성</label>
				<textarea class="form-control" id="commentContent"
					name="commentContent" rows="3" placeholder="댓글 내용을 입력하세요" required></textarea>
			</div>
			<button type="submit" class="btn btn-primary-custom">댓글 작성</button>
		</form>
	</div>
</div>

<script>
	$(document).ready(function() {
		const postId = ${boardDTO.post.id};

        // 게시글 수정 버튼 클릭 처리
        $("#updatePostBtn").on("click", function() {
            window.location.href = "/board/update/" + postId; // 수정 페이지로 이동
        });

        // 게시글 삭제 버튼 클릭 처리
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
	})
</script>
