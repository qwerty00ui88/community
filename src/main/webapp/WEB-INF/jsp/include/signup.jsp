<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container signup-container">
	<h2 class="text-center">회원가입</h2>
	<form id="signupForm" action="/user/signup" method="post">
		<div class="form-group">
			<label for="username">이름</label> <input type="text"
				class="form-control" id="name" name="name" placeholder="이름 입력"
				required>
		</div>
		<div class="form-group">
			<label for="username">닉네임</label> <input type="text"
				class="form-control" id="nickname" name="nickname"
				placeholder="닉네임 입력" autocomplete="nickname" required>
		</div>
		<button id="nicknameCheck" type="button"
			class="btn btn-primary-custom btn-block">닉네임 중복 확인</button>
		<div class="form-group">
			<label for="password">비밀번호</label> <input type="password"
				class="form-control" id="password" name="password"
				placeholder="비밀번호 입력" autocomplete="new-password" required>
		</div>
		<div class="form-group">
			<label for="confirmPassword">비밀번호 확인</label> <input type="password"
				class="form-control" id="confirmPassword" name="confirmPassword"
				placeholder="비밀번호 확인" autocomplete="new-password" required>
		</div>
		<button id="signupBtn" type="submit"
			class="btn btn-primary-custom btn-block">회원가입</button>
		<div class="text-center mt-3">
			<a href="/login">이미 계정이 있으신가요? 로그인</a>
		</div>
	</form>
</div>
<script>
	$(document).ready(function() {

		// 닉네임 중복 체크
		$("#nicknameCheck").on("click", function() {
			const nickname = $("#nickname").val();
			$.post("/user/isDuplicatedNickname", {
				"nickname" : nickname
			});
		})

		// 회원가입
		$("#signupForm").on("submit", function(e) {
			e.preventDefault();

			const name = $("#name").val();
			const nickname = $("#nickname").val();
			const password = $("#password").val();

			$.post("/user/signup", {
				"name" : name,
				"nickname" : nickname,
				"password" : password
			}).done(function(response) {
				// 회원가입 성공 시
				if (response.status === "SUCCESS") {
					alert("회원가입이 성공적으로 완료되었습니다.");
					window.location.href = "/login";
				} else {
					alert("회원가입 중 오류가 발생했습니다.");
				}
			}).fail(function() {
				// 회원가입 실패 시
				alert("회원가입 중 오류가 발생했습니다.");
			});
		})
	})
</script>