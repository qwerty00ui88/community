<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="row">
	<aside class="col-md-4">
		<div class="sidebar">
			<c:choose>
				<c:when
					test="${not empty LOGIN_MEMBER_ID || not empty LOGIN_ADMIN_ID}">
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
				<li class="list-group-item"><a href="post1.jsp">게시글 1</a></li>
				<li class="list-group-item"><a href="post2.jsp">게시글 2</a></li>
				<li class="list-group-item"><a href="post3.jsp">게시글 3</a></li>
			</ul>
		</div>
	</aside>
	<main class="col-md-8">
		<div class="main">
			<nav class="nav nav-pills nav-justified nav-custom">
				<a class="nav-link active" href="#">전체</a> <a class="nav-link"
					href="#">카테고리 1</a> <a class="nav-link" href="#">카테고리 2</a>
			</nav>
			<div class="form-group">
				<div class="input-group">
					<input type="text" class="form-control" id="search"
						placeholder="검색 단어">
					<div class="input-group-append">
						<button class="btn" type="button">검색</button>
					</div>
				</div>
			</div>
			<h2>글 목록</h2>
			<ul class="list-group">
				<li class="list-group-item"><a href="post1.jsp">게시글 1</a></li>
				<li class="list-group-item"><a href="post2.jsp">게시글 2</a></li>
				<li class="list-group-item"><a href="post3.jsp">게시글 3</a></li>
				<!-- 더 많은 게시글을 추가할 수 있습니다. -->
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
		})

		// 로그아웃
		$("#sidebar-logoutForm").on("submit", function(e) {
			e.preventDefault();
			$.post("/user/logout").always(function() {
				location.reload();
			})
		})
	})
</script>