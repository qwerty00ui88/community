<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<c:set var="isAdminViewingOtherProfile" value="false" />
<sec:authorize access="hasRole('ROLE_ADMIN')">
    <c:set var="isAdminViewingOtherProfile" value="${authentication.principal.id != profile.user.id}" />
</sec:authorize>

<div class="container mt-5">
    <div class="profile-header">
        <h2 class="mt-3">${profile.user.nickname}</h2>
    </div>
    <div class="row">
        <div class="col-md-4">
            <div class="card mb-4">
                <div class="card-header">프로필 정보</div>
                <div class="card-body">
                    <p><strong>이름:</strong> ${profile.user.name}</p>
                    <p><strong>가입 날짜:</strong> ${fn:substring(profile.user.createdAt.toString(), 0, 10)}</p>
                    <p><strong>활동 상태:</strong> ${profile.user.status}</p>
                </div>
            </div>
            <div class="card mb-4">
                <div class="card-header">비밀번호 변경</div>
                <div class="card-body">
                    <form id="updatePasswordForm">
                        <sec:authorize access="authentication.principal.id == ${profile.user.id}">
                            <div class="form-group">
                                <label for="beforePassword">현재 비밀번호</label>
                                <input type="password" class="form-control" id="beforePassword" name="beforePassword" required>
                            </div>
                        </sec:authorize>
                        <div class="form-group">
                            <label for="afterPassword">새 비밀번호</label>
                            <input type="password" class="form-control" id="afterPassword" name="afterPassword" required>
                        	<div class="feedback" id="afterPasswordFeedback"></div>
                        </div>
                         <div class="form-group">
                            <label for="afterPasswordCheck">새 비밀번호 확인</label>
                            <input type="password" class="form-control" id="afterPasswordCheck" name="afterPassword" required>
                        	<div class="feedback" id="afterPasswordCheckFeedback"></div>
                        </div>
                        <button type="submit" class="btn btn-primary btn-block">비밀번호 변경</button>
                    </form>
                </div>
            </div>
            <c:if test="${profile.user.status != 'DELETED'}">
	            <div class="card mb-4">
	                <div class="card-header">탈퇴하기</div>
	                <div class="card-body">
	                    <form id="deleteAccountForm">
	                        <c:if test="${!isAdminViewingOtherProfile}">
	                            <div class="form-group">
	                                <label for="password">비밀번호 확인</label>
	                                <input type="password" class="form-control" id="password" name="password" required>
	                            </div>
	                        </c:if>
	                        <button type="submit" class="btn btn-danger btn-block">계정 탈퇴</button>
	                    </form>
	                </div>
	            </div>
            </c:if>
        </div>
        <div class="col-md-8">
		    <div class="section-header">
		        <h4>작성한 게시글</h4>
		    </div>
		    <ul class="list-group mb-4" style="max-height: 345px; overflow-y: auto;">
		        <c:forEach items="${profile.postList}" var="post">
		            <li class="list-group-item">
		                <a href="/board/${post.id}" class="d-flex justify-content-between d-block text-decoration-none">
		                    <span class="title">${post.title}</span>
		                    <span class="date">${fn:substring(post.updatedAt.toString(), 0, 10)}</span>
		                </a>
		            </li>
		        </c:forEach>
		    </ul>
		    <div class="section-header">
		        <h4>작성한 댓글</h4>
		    </div>
		    <ul class="list-group" style="max-height: 345px; overflow-y: auto;">
		        <c:forEach items="${profile.commentList}" var="comment">
		            <li class="list-group-item">
		                <a href="#" class="comment-link d-flex justify-content-between d-block text-decoration-none" data-post-id="${comment.postId}" data-comment-id="${comment.id}">
		                    <c:choose>
		                        <c:when test="${comment.status == 'DELETED'}">
		                            <span class="title text-muted">삭제된 댓글입니다.</span>
		                            <span class="date text-muted">${fn:substring(comment.updatedAt.toString(), 0, 10)}</span>
		                        </c:when>
		                        <c:otherwise>
		                            <span class="title">${comment.contents}</span>
		                            <span class="date">${fn:substring(comment.updatedAt.toString(), 0, 10)}</span>
		                        </c:otherwise>
		                    </c:choose>
		                </a>
		            </li>
		        </c:forEach>
		    </ul>
		</div>
    </div>
</div>

<script>
    $(document).ready(function() {
    	const isAdminViewingOtherProfile = ${isAdminViewingOtherProfile};

        // 비밀번호 유효성 검사
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
        $("#afterPassword").on("input", function() {
            const password = $(this).val();
            if (validatePassword(password)) {
                $("#afterPasswordFeedback").removeClass("invalid-feedback").addClass("valid-feedback").text("사용할 수 있는 비밀번호입니다.");
            } else {
                $("#afterPasswordFeedback").removeClass("valid-feedback").addClass("invalid-feedback")
                    .text("비밀번호는 8자 이상, 영문자, 숫자, 특수문자(@$!%*?&)를 포함해야 하며, 한글을 포함할 수 없습니다.");
            }
        });

        // 비밀번호 일치 여부 검사
        $("#afterPasswordCheck").on("input", function() {
            const password = $("#afterPassword").val();
            const passwordCheck = $(this).val();
            if (password === passwordCheck) {
                $("#afterPasswordCheckFeedback").removeClass("invalid-feedback").addClass("valid-feedback").text("비밀번호가 일치합니다.");
            } else {
                $("#afterPasswordCheckFeedback").removeClass("valid-feedback").addClass("invalid-feedback")
                    .text("비밀번호가 일치하지 않습니다.");
            }
        });

        // 비밀번호 변경 폼 제출
        $("#updatePasswordForm").on("submit", function(e) {
            e.preventDefault();

            if (!confirm("정말로 비밀번호를 변경하시겠습니까?")) {
                return;
            }

            const beforePassword = $("#beforePassword").val();
            const afterPassword = $("#afterPassword").val();
            const afterPasswordCheck = $("#afterPasswordCheck").val();

            if (!validatePassword(afterPassword)) {
                alert("유효하지 않은 비밀번호입니다.");
                return;
            }

            if (afterPassword !== afterPasswordCheck) {
                alert("비밀번호가 일치하지 않습니다.");
                return;
            }

            const url = "/api/user/auth/updatePassword";
            const data = {
                "id": ${profile.user.id},
                "beforePassword": beforePassword || null,
                "afterPassword": afterPassword,
                "afterPasswordCheck": afterPasswordCheck
            };

            $.ajax({
                url: url,
                type: "PATCH",
                data: data,
                success: function(response) {
                    alert("비밀번호가 성공적으로 변경되었습니다.");
                    if(${isAdminViewingOtherProfile} == false) {
                        $.ajax({
                            url: "/user/auth/logout",
                            type: "POST"
                        });
                        location.href = "/login";
                    }
                },
                error: function(_, _, error) {
                    console.log(error);
                    alert("비밀번호 변경 중 오류가 발생했습니다.");
                },
                complete: function() {
                    $("#beforePassword").val("");
                    $("#afterPassword").val("");
                    $("#afterPasswordCheck").val("");
                    $("#afterPassword").removeClass("is-valid is-invalid");
                    $("#afterPasswordCheck").removeClass("is-valid is-invalid");
                    $("#afterPasswordFeedback").removeClass("valid-feedback invalid-feedback").text("");
                    $("#afterPasswordCheckFeedback").removeClass("valid-feedback invalid-feedback").text("");
                }
            });
        });
        
     	// 계정 탈퇴
        $("#deleteAccountForm").on("submit", function(e) {
            e.preventDefault();
            if (!confirm("정말로 계정을 삭제하시겠습니까?")) {
                return;
            }
            const data = {
                "id": ${profile.user.id},
            	"password": $("#password").val() || null
            };

            $.ajax({
                url: "/api/user/auth",
                type: "DELETE",
                data: data,
                success: function(response) {
                    alert("계정이 성공적으로 탈퇴되었습니다.");
                    location.href = isAdminViewingOtherProfile ? "/admin/user/manage" : "/";
                },
                error: function(_, _, error) {
                    console.log(error);
                    alert("계정 탈퇴 중 오류가 발생했습니다.");
                },
                complete: function() {
                	$("#password").val("");
                }
            });
        });
     	
     	// 댓글 링크
        $(".comment-link").on("click", function(event) {
            event.preventDefault();

            const postId = $(this).data("post-id");
            const commentId = $(this).data("comment-id");

            $.ajax({
                url: "/board/" + postId,
                type: 'GET',
                success: function(response) {
                    if(response.code === "NOT_FOUND") {
                    	alert("삭제된 게시물입니다.")
                    } else {
                    	location.href = "/board/" + postId;
                    }
                },
                error: function(xhr, status, error) {
                	alert("삭제된 게시물입니다.");
                }
            });
        });
     	
     	
     	
        // *** 함수 ***
        function validatePassword(password) {
            return passwordRegex.test(password);
        }
    });
</script>

<style>
	.feedback {
	    font-size: 0.875em;
	    margin-top: 0.25rem;
	    display: none;
	}
	
	.valid-feedback {
	    color: #28a745;
	    display: block;
	}
	
	.invalid-feedback {
	    color: #dc3545;
	    display: block;
	}
	
	.list-group-item a {
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	}

	.list-group-item .title {
	    white-space: nowrap;
	    overflow: hidden;
	    text-overflow: ellipsis;
	    max-width: 80%;
	}
	
	.list-group-item .date {
	    white-space: nowrap;
	    margin-left: 10px;
	    color: #6c757d;
	}
</style>
