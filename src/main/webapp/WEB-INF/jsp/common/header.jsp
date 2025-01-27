<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
	<a class="navbar-brand" href="/">👋 Hi️ Community</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse"
		data-target="#navbarNav" aria-controls="navbarNav"
		aria-expanded="false" aria-label="Toggle navigation">
		<span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarNav">
		<ul class="navbar-nav ml-auto">
			<!-- 로그인 상태일 때 -->
			<sec:authorize access="isAuthenticated()">
				<li class="nav-item"><a class="nav-link" href="/profile/<sec:authentication property="principal.id" />">프로필</a></li>
				<li class="nav-item">
					<form id="nav-logoutForm" action="/api/user/logout" method="post"
						class="form-inline my-2 my-lg-0 d-flex justify-content-center">
						<button id="logoutBtn" type="submit"
							class="btn btn-link nav-link">로그아웃</button>
					</form>
				</li>
			</sec:authorize>
			<!-- 로그인 상태가 아닐 때 -->
			<sec:authorize access="isAnonymous()">
				<li class="nav-item"><a class="nav-link" href="/login">로그인</a></li>
				<li class="nav-item"><a class="nav-link" href="/signup">회원가입</a></li>
			</sec:authorize>
		</ul>
	</div>
</nav>
<script>
	$(document).ready(function() {
		// 로그아웃
		$("#nav-logoutForm").on("submit", function(e) {
			e.preventDefault();
			$.ajax({
		        type: "POST",
		        url: "/api/user/auth/logout",
		        contentType: "application/json",
		        success: function (response) {
		            if (response.code === "SUCCESS") {
		                location.href = "/";
		            } else {
		                alert("로그아웃에 실패했습니다.");
		            }
		        },
		        error: function (jqXHR, textStatus, errorThrown) {
		            console.error("Error:", textStatus, errorThrown);
		            alert("로그아웃 중 오류가 발생했습니다. 다시 시도하세요.");
		        },
		        complete: function () {
		        	location.href = "/";
		        },
		    });
		})
	})
</script>
