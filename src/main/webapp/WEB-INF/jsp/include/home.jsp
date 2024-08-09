<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<aside class="col-md-4">
		<div class="sidebar">
			<c:choose>
				<c:when
					test="${not empty LOGIN_USER_ID || not empty LOGIN_ADMIN_ID}">
					<div class="form-group">
						<label>Welcome!</label>
					</div>
					<form id="sidebar-logoutForm" action="/user/logout" method="post">
						<button type="submit" class="btn btn-primary-custom btn-block">로그아웃</button>
					</form>
				</c:when>
				<c:otherwise>
					<form id="loginForm" action="/user/login" method="post">
						<div class="form-group">
							<label for="nickname">Nickname</label> <input type="text"
								class="form-control" id="nickname" name="nickname"
								placeholder="Enter nickname" autocomplete="current-password">
						</div>
						<div class="form-group">
							<label for="password">Password</label> <input type="password"
								class="form-control" id="password" name="password"
								placeholder="Enter password" autocomplete="current-password">
						</div>
						<button type="submit" class="btn btn-primary-custom btn-block">로그인</button>
						<button type="button" class="btn btn-secondary btn-block">ID/PW
							찾기</button>
					</form>
				</c:otherwise>
			</c:choose>
			<hr>
			<h3>추천수가 높은순 리스트</h3>
			<ul class="list-group">
				<c:forEach items="${homeDTO.mostViewedPostList}" var="post">
					<li class="list-group-item"><a href="/board/${post.id}">${post.title}</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</aside>
	<main class="col-md-8">
		<div class="main">
			<ul class="nav nav-pills nav-justified nav-custom">
				<li class="category nav-link active" data-category="all">전체</li>
				<li class="category nav-link" data-category="category1">카테고리1</li>
				<li class="category nav-link" data-category="category2">카테고리2</li>
			</ul>
			<div class="form-group mt-3">
				<div class="input-group">
					<input type="text" class="form-control" id="search"
						placeholder="검색 단어">
					<div class="input-group-append">
						<button class="btn" type="button">검색</button>
					</div>
				</div>
			</div>
			<div class="d-flex justify-content-between align-items-center mt-3">
				<h2>글 목록</h2>
				<a href="/board/create" class="btn btn-primary">글쓰기</a>
			</div>
			<ul class="list-group mt-3" id="post-list-all">
				<c:forEach items="${homeDTO.allPostList}" var="post">
					<li class="list-group-item"><a href="/board/${post.id}">${post.title}</a>
					</li>
				</c:forEach>
			</ul>
			<ul class="list-group mt-3" id="post-list-category1"
				style="display: none;">
				<c:forEach items="${homeDTO.postListByCategory1}" var="post">
					<li class="list-group-item"><a href="/board/${post.id}">${post.title}</a>
					</li>
				</c:forEach>
			</ul>
			<ul class="list-group mt-3" id="post-list-category2"
				style="display: none;">
				<c:forEach items="${homeDTO.postListByCategory2}" var="post">
					<li class="list-group-item"><a href="/board/${post.id}">${post.title}</a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</main>
</div>

<script>
	$(document).ready(function() {
		// 로그인
		$("#loginForm").on("submit", function(e) {
			e.preventDefault();
			let nickname = $("#nickname").val();
			let password = $("#password").val();
			$.post("/user/login", {
				"nickname" : nickname,
				"password" : password
			}).done(function(response) {
				console.log(response);
				if (response.status === "SUCCESS") {
					window.location.href = "/";
				} else {
					alert("로그인에 실패했습니다. 닉네임과 비밀번호를 확인하세요.");
				}
			}).fail(function(_, _, error) {
				console.log(error);
				alert("로그인 중 오류가 발생했습니다.");
			});
		});
	
		// 로그아웃
		$("#sidebar-logoutForm").on("submit", function(e) {
			e.preventDefault();
			$.post("/user/logout").always(function() {
				location.reload();
			});
		});
	
		// 카테고리 탭 클릭 시 해당 게시글만 표시
		$(".category").on("click", function(e) {
			e.preventDefault();
			$(".category").removeClass("active");
			$(this).addClass("active");
			let category = $(this).data("category");
			$("ul[id^='post-list']").hide();
			$("#post-list-" + category).show();
		});
	});
</script>
