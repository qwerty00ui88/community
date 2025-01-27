<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container">
	<h2 class="text-center">로그인</h2>
	<form id="loginForm" action="/api/user/login" method="post">
		<div class="form-group">
			<label for="username">닉네임</label> <input type="text"
				class="form-control" id="nickname" name="username"
				placeholder="아이디 입력" autocomplete="current-password" required>
		</div>
		<div class="form-group">
			<label for="password">비밀번호</label> <input type="password"
				class="form-control" id="password" name="password"
				placeholder="비밀번호 입력" autocomplete="current-password" required>
		</div>
		<button type="submit" class="btn btn-primary-custom btn-block">로그인</button>
		<div class="text-center mt-2">
			<a href="/signup">회원가입</a>
		</div>
	</form>
</div>
<script>
	$(document).ready(function() {
		// 로그인
		$("#loginForm").on("submit", function(e) {
			e.preventDefault();

			let nickname = $("#nickname").val().trim();
		    let password = $("#password").val();
		
		    if (!nickname || !password) {
		        alert("닉네임과 비밀번호를 입력하세요.");
		        return;
		    }
		
		    $.ajax({
		        type: "POST",
		        url: "/api/user/public/login",
		        contentType: "application/json",
		        data: JSON.stringify({
		            "nickname": nickname,
		            "password": password,
		        }),
		        success: function (response) {
		            if (response.code === "SUCCESS") {
		                location.href = "/";
		            } else {
		                alert("로그인에 실패했습니다. 닉네임과 비밀번호를 확인하세요.");
		            }
		        },
		        error: function (jqXHR, textStatus, errorThrown) {
		            console.error("Error:", textStatus, errorThrown);
		            alert("로그인 중 오류가 발생했습니다. 다시 시도하세요.");
		        },
		        complete: function () {
		            $("#nickname").val("");
		            $("#password").val("");
		        },
		    });
		})
	})
</script>