<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container mt-5">
	<div class="profile-header">
		<img src="profile-image-url" alt="프로필 이미지" class="profile-image">
		<h2 class="mt-3">사용자 닉네임</h2>
		<p class="text-muted">사용자 이메일</p>
	</div>
	<div class="row">
		<div class="col-md-4">
			<div class="card mb-4">
				<div class="card-header">프로필 정보</div>
				<div class="card-body">
					<p>
						<strong>이름:</strong> 사용자 이름
					</p>
					<p>
						<strong>가입 날짜:</strong> 가입 날짜
					</p>
					<p>
						<strong>활동 상태:</strong> 활동 상태
					</p>
				</div>
			</div>
			<div class="card mb-4">
				<div class="card-header">비밀번호 변경</div>
				<div class="card-body">
					<form id="updatePasswordForm">
						<div class="form-group">
							<label for="currentPassword">현재 비밀번호</label> <input
								type="password" class="form-control" id="currentPassword"
								name="currentPassword" required>
						</div>
						<div class="form-group">
							<label for="newPassword">새 비밀번호</label> <input type="password"
								class="form-control" id="newPassword" name="newPassword"
								required>
						</div>
						<button type="submit" class="btn btn-primary btn-block">비밀번호
							변경</button>
					</form>
				</div>
			</div>
			<div class="card mb-4">
				<div class="card-header">탈퇴하기</div>
				<div class="card-body">
					<form id="deleteAccountForm">
						<div class="form-group">
							<label for="password">비밀번호 확인</label> <input type="password"
								class="form-control" id="password" name="password" required>
						</div>
						<button type="submit" class="btn btn-danger btn-block">계정
							탈퇴</button>
					</form>
				</div>
			</div>
		</div>
		<div class="col-md-8">
			<div class="section-header">
				<h4>내가 쓴 글</h4>
			</div>
			<ul class="list-group mb-4">
				<li class="list-group-item"><a href="post1.jsp">게시글 1</a></li>
				<li class="list-group-item"><a href="post2.jsp">게시글 2</a></li>
				<li class="list-group-item"><a href="post3.jsp">게시글 3</a></li>
				<!-- 더 많은 게시글을 추가할 수 있습니다. -->
			</ul>
			<div class="section-header">
				<h4>내가 쓴 댓글</h4>
			</div>
			<ul class="list-group">
				<li class="list-group-item">댓글 내용 1</li>
				<li class="list-group-item">댓글 내용 2</li>
				<li class="list-group-item">댓글 내용 3</li>
				<!-- 더 많은 댓글을 추가할 수 있습니다. -->
			</ul>
		</div>
	</div>

</div>
<script>
	$(document).ready(function() {
		// 비밀번호 변경
		$("#updatePasswordForm").on("submit", function(e) {
			e.preventDefault();

			if (!confirm("정말로 비밀번호를 변경하시겠습니까?")) {
				return;
			}
			
			const beforePassword = $("#beforePassword").val();
			const afterPassword = $("#afterPassword").val();

			$.ajax({
				url : "/user/updatePassword",
				type : "PATCH",
				data : {
					"beforePassword" : beforePassword,
					"afterPassword" : afterPassword
				},
				success : function(response) {
					alert("비밀번호가 성공적으로 변경되었습니다.");
				},
				error : function(_, _, error) {
					console.log(error);
					alert("비밀번호 변경 중 오류가 발생했습니다.");
				}
			});
		});

		// 계정 탈퇴
		$("#deleteAccountForm").on("submit", function(e) {
			e.preventDefault();

			if (!confirm("정말로 계정을 삭제하시겠습니까?")) {
				return;
			}

			const password = $("#password").val();

			$.ajax({
				url : "/user/deleteId",
				type : "DELETE",
				data : {
					"password" : password
				},
				success : function(response) {
					alert("계정이 성공적으로 탈퇴되었습니다.");
					location.href = "/";
				},
				error : function(_, _, error) {
					console.log(error);
					alert("계정 탈퇴 중 오류가 발생했습니다.");
				}
			});
		});
	})
</script>