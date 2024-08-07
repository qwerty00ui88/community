<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<a class="navbar-brand" href="/">👋 Hi️ Community</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarNav" aria-controls="navbarNav"
		aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarNav">
		<ul class="navbar-nav ml-auto">
			<c:choose>
				<c:when
					test="${not empty LOGIN_MEMBER_ID || not empty LOGIN_ADMIN_ID}">
					<!-- 로그인 상태일 때 -->
					<li class="nav-item"><a class="nav-link" href="/profile">프로필</a></li>
					<li class="nav-item">
						<form id="nav-logoutForm" action="/user/logout" method="post"
							class="form-inline my-2 my-lg-0 d-flex justify-content-center">
							<button id="logoutBtn" type="submit"
								class="btn btn-link nav-link">로그아웃</button>
						</form>
					</li>
				</c:when>
				<c:otherwise>
					<!-- 로그인 상태가 아닐 때 -->
					<li class="nav-item"><a class="nav-link" href="/login">로그인</a></li>
					<li class="nav-item"><a class="nav-link" href="/signup">회원가입</a></li>
				</c:otherwise>
			</c:choose>
		</ul>
	</div>
</nav>
<script>
	$(document).ready(function() {
		// 로그아웃
		$("#nav-logoutForm").on("submit", function(e) {
			e.preventDefault();
			$.post("/user/logout").always(function() {
				console.log("뭐");
				location.reload();
			})
		})
	})
</script>
