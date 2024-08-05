<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container">
	<h2 class="text-center">로그인</h2>
	<form action="login" method="post">
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
		<button type="submit" class="btn btn-primary-custom btn-block">로그인</button>
		<div class="text-center mt-3">
			<a href="forgot_password.jsp">비밀번호를 잊으셨나요?</a>
		</div>
		<div class="text-center mt-2">
			<a href="register.jsp">회원가입</a>
		</div>
	</form>
</div>