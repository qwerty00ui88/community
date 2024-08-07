<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container">
	<h2 class="text-center">로그인</h2>
	<form id="loginForm" action="/user/login" method="post">
		<div class="form-group">
			<label for="username">아이디</label> <input type="text"
				class="form-control" id="nickname" name="username"
				placeholder="아이디 입력" autocomplete="current-password" required>
		</div>
		<div class="form-group">
			<label for="password">비밀번호</label> <input type="password"
				class="form-control" id="password" name="password"
				placeholder="비밀번호 입력" autocomplete="current-password" required>
		</div>
		<button type="submit" class="btn btn-primary-custom btn-block">로그인</button>
		<div class="text-center mt-3">
			<a href="#">비밀번호를 잊으셨나요?</a>
		</div>
		<div class="text-center mt-2">
			<a href="/signup">회원가입</a>
		</div>
	</form>
</div>
<script>
	$(document).ready(function() {
		$("#loginForm").on("submit", function(e) {
			e.preventDefault();

			const nickname = $("#nickname").val();
			const password = $("#password").val();

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
	})
</script>