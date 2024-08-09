<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container create-post-container">
	<h2 class="text-center">게시글 작성</h2>
	<form id="categoryForm">
		<div class="form-group">
			<label for="title">제목</label> <input type="text" class="form-control"
				id="title" name="title" placeholder="제목 입력" required>
		</div>
		<div class="form-group">
			<label for="category">카테고리</label> <select class="form-control"
				id="category" name="category" required>
				<option value="1">카테고리 1</option>
				<option value="2">카테고리 2</option>
				<option value="3">카테고리 3</option>
			</select>
		</div>
		<div class="form-group">
			<label for="contents">내용</label>
			<textarea class="form-control" id="contents" name="contents"
				rows="10" placeholder="내용을 입력하세요" required></textarea>
		</div>
		<button type="submit" class="btn btn-primary-custom btn-block">게시글
			작성</button>
	</form>
</div>
<script>
	$(document).ready(function() {
		$("#categoryForm").on("submit", function(e) {
			e.preventDefault();

			const title = $("#title").val();
			const categoryId = $("#category").val();
			const contents = $("#contents").val();

			console.log(title, categoryId, contents);

			$.post("/api/post/create", {
			    "title": title,
			    "categoryId": categoryId,
			    "contents": contents
			}).done(function(response) {
			    if (response.status === "OK" && response.code === "SUCCESS") {
			        alert("게시글이 성공적으로 작성되었습니다.");
			        window.location.href = "/board/" + response.data.id;
			    } else {
			        alert("게시글 작성에 성공했으나, 예상치 못한 응답이 발생했습니다.");
			    }
			}).fail(function(xhr, status, error) {
			    alert("게시글 작성 중 오류가 발생했습니다: " + error);
			});

		});
	});
</script>
