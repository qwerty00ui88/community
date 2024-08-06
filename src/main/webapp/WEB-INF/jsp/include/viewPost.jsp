<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container view-post-container">
	<h2 class="text-center">게시글 제목</h2>
	<div class="post-meta text-muted mb-4">
		<span>작성자: <strong>작성자명</strong></span> | <span>작성일: <strong>2024-01-01</strong></span>
		| <span>조회수: <strong>123</strong></span>
	</div>
	<div class="post-content">
		<p>여기에 게시글 내용이 표시됩니다.</p>
		<p>이미지를 포함할 수 있습니다.</p>
		<img src="path/to/image.jpg" alt="Example Image"
			class="img-fluid mb-3">
		<p>더 많은 내용이 여기에 표시됩니다.</p>
	</div>
	<hr>
	<div class="comments">
		<h3>댓글</h3>
		<div class="comment mb-3">
			<div class="comment-meta text-muted">
				<span>작성자: <strong>댓글 작성자명</strong></span> | <span>작성일: <strong>2024-01-02</strong></span>
			</div>
			<p>여기에 댓글 내용이 표시됩니다.</p>
			<button class="btn btn-secondary-custom mb-2"
				onclick="toggleReplyForm(this)">대댓글 작성</button>
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
	<script>
		function toggleReplyForm(button) {
			var replyForm = button.nextElementSibling;
			if (replyForm.style.display === "none") {
				replyForm.style.display = "block";
			} else {
				replyForm.style.display = "none";
			}
		}
	</script>
</div>
