<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div class="container signup-container">
	<h2 class="text-center">회원가입</h2>
	<form id="signupForm" action="/api/user/signup" method="post">
		<div class="form-group">
			<label for="username">이름</label>
			<input type="text"
				class="form-control" id="name" name="name" placeholder="이름 입력"
				required>
			<div class="feedback"></div>
		</div>
		<div class="form-group">
			<label for="username">닉네임</label>
			<input type="text"
				class="form-control" id="nickname" name="nickname"
				placeholder="닉네임 입력" autocomplete="nickname" required>
			<div class="feedback"></div>
		</div>
		<!-- <button id="nicknameCheck" type="button"
			class="btn btn-primary-custom btn-block">닉네임 중복 확인</button> -->
		<div class="form-group">
			<label for="password">비밀번호</label>
			<input type="password"
				class="form-control" id="password" name="password"
				placeholder="비밀번호 입력" autocomplete="new-password" required>
			<div class="feedback"></div>
		</div>
		<div class="form-group">
			<label for="confirmPassword">비밀번호 확인</label>
			<input type="password"
				class="form-control" id="confirmPassword" name="confirmPassword"
				placeholder="비밀번호 확인" autocomplete="new-password" required>
			<div class="feedback"></div>
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
		// 이름 유효성 검사
        $("#name").on("input", function() {
            const name = $(this).val().trim();
            validateField(this, name.length > 0, "이름을 입력해 주세요.", "");
        });

		// 닉네임 유효성 검사
        $("#nickname").on("input", function() {
        	const nickname = $(this).val().trim();
            
            if (nickname.length > 0) {
                // 닉네임 중복 확인
                $.get("/api/user/isDuplicatedNickname", { "nickname": nickname })
                    .done(function(response) {
                        if (response.data) {
                            validateField("#nickname", false, "이미 사용 중인 닉네임입니다.", "");
                        } else {
                            validateField("#nickname", true, "", "사용 가능한 닉네임입니다.");
                        }
                    })
                    .fail(function() {
                        validateField("#nickname", false, "닉네임 확인 중 오류가 발생했습니다.", "");
                    });
            } else {
                validateField("#nickname", false, "닉네임을 입력해 주세요.", "");
            }
        });

		// 비밀번호 유효성 검사
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        $("#password").on("input", function() {
        	const password = $(this).val().trim();
            const isValid = passwordRegex.test(password);
            validateField(this, isValid, "비밀번호는 8자 이상, 영문자, 숫자, 특수문자(@$!%*?&)를 포함해야 하며, 한글을 포함할 수 없습니다.", "비밀번호가 유효합니다.");
        });

        $("#confirmPassword").on("input", function() {
        	const confirmPassword = $(this).val().trim();
            const password = $("#password").val().trim();
            const isValid = confirmPassword === password && passwordRegex.test(confirmPassword);
            validateField(this, isValid, "비밀번호가 일치하지 않거나 유효하지 않습니다.", "비밀번호가 일치합니다.");
        });

        // 회원가입 버튼 이벤트
        $("#signupForm").on("submit", function(e) {
            e.preventDefault();

            const isNameValid = $("#name").next(".feedback").hasClass("valid-feedback");
            const isNicknameValid = $("#nickname").next(".feedback").hasClass("valid-feedback");
            const isPasswordValid = $("#password").next(".feedback").hasClass("valid-feedback");
            const isConfirmPasswordValid = $("#confirmPassword").next(".feedback").hasClass("valid-feedback");

            if (isNameValid && isNicknameValid && isPasswordValid && isConfirmPasswordValid) {
            	const name = $("#name").val();
    			const nickname = $("#nickname").val();
    			const password = $("#password").val();
    			
            	$.post("/api/user/signup", {
    				"name" : name,
    				"nickname" : nickname,
    				"password" : password
    			}).done(function(response) {
    				if (response.code === "SUCCESS") {
    					alert("회원가입이 성공적으로 완료되었습니다.");
    					location.href = "/login";
    				} else {
    					alert("회원가입 중 오류가 발생했습니다.");
    				}
    			}).fail(function() {
    				alert("회원가입 중 오류가 발생했습니다.");
    			}).always(function() {
    				$("#name").val("");
    				$("#nickname").val("");
    				$("#password").val("");
    				$("#confirmPassword").val("");
    			});
            } else {
                alert("모든 필드를 올바르게 입력해 주세요.");
            }
        });
        
        
        
        // *** 함수 ***
		function validateField(field, condition, invalidMsg, validMsg) {
            const feedback = $(field).next(".feedback");

            if (condition) {
                feedback.text(validMsg).removeClass("invalid-feedback").addClass("valid-feedback").show();
            } else {
                feedback.text(invalidMsg).removeClass("valid-feedback").addClass("invalid-feedback").show();
            }
        }
	})
</script>
<style>
	.feedback {
	    margin-top: 0.5rem;
	    font-size: 0.875rem;
	}
	
	.invalid-feedback {
	    color: red;
	    display: none;
	}
	
	.valid-feedback {
	    color: green;
	    display: none;
	}
</style>