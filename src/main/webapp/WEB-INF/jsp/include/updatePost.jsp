<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<div class="container edit-post-container">
	<h2 class="text-center">게시글 수정</h2>
	<form id="updatePostForm">
		<div class="form-group">
			<label for="title">제목</label> <input type="text" class="form-control"
				id="title" name="title" placeholder="제목 입력" value="${post.title}"
				required>
		</div>
		<div class="form-group">
			<label for="category">카테고리</label> <select class="form-control"
				id="category" name="category" required>
				<option value="1"
					<c:if test="${post.categoryId == 1}">selected</c:if>>카테고리
					1</option>
				<option value="2"
					<c:if test="${post.categoryId == 2}">selected</c:if>>카테고리
					2</option>
				<option value="3"
					<c:if test="${post.categoryId == 3}">selected</c:if>>카테고리
					3</option>
			</select>
		</div>
		<div class="form-group">
			<label for="contents">내용</label>
			<textarea class="form-control" id="contents" name="contents"
				rows="10" placeholder="내용을 입력하세요" required>${post.contents}</textarea>
		</div>
		<button type="submit" class="btn btn-primary-custom btn-block">게시글
			수정</button>
	</form>
</div>
<script>
	$(document).ready(function() {
		const postId = ${post.id};

		// 게시글 수정
		$("#updatePostForm").on("submit", function(e) {
			e.preventDefault();

			const title = $("#title").val();
			const categoryId = $("#category").val();
			const contents = $("#contents").val();

			$.ajax({
			    url: "/api/post/" + postId,
			    type: "PUT",
			    data: {
			        "title": title,
			        "categoryId": categoryId,
			        "contents": contents
			    },
			    success: function(response) {
			        if (response.status === "OK" && response.code === "SUCCESS") {
			            alert(response.message);
			            window.location.href = "/board/" + postId;
			        } else {
			            alert("게시글 수정에 성공했으나, 예상치 못한 응답이 발생했습니다.");
			        }
			    },
			    error: function(xhr, status, error) {
			        alert("게시글 수정 중 오류가 발생했습니다: " + error);
			    }
			});

		});
	});
</script>
