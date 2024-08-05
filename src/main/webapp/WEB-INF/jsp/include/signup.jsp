<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container signup-container">
	<h2 class="text-center">회원가입</h2>
	<form action="signup" method="post">
		<div class="form-group">
			<label for="username">아이디</label> <input type="text"
				class="form-control" id="username" name="username"
				placeholder="아이디 입력" required>
		</div>
		<div class="form-group">
			<label for="password">비밀번호</label> <input type="password"
				class="form-control" id="password" name="password"
				placeholder="비밀번호 입력" required>
		</div>
		<div class="form-group">
			<label for="confirmPassword">비밀번호 확인</label> <input type="password"
				class="form-control" id="confirmPassword" name="confirmPassword"
				placeholder="비밀번호 확인" required>
		</div>
		<div class="form-group">
			<label for="email">이메일</label> <input type="email"
				class="form-control" id="email" name="email" placeholder="이메일 입력"
				required>
		</div>
		<button type="submit" class="btn btn-primary-custom btn-block">회원가입</button>
		<div class="text-center mt-3">
			<a href="/login">이미 계정이 있으신가요? 로그인</a>
		</div>
	</form>
</div>