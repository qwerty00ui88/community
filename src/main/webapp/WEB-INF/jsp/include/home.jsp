<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<div class="row">
	<aside class="col-md-4">
		<div class="sidebar">
			<sec:authorize access="isAuthenticated()">
				<div class="form-group">
					<div class="welcome-message">
           				<label>Welcome!</label>
			            <div>환영합니다, <strong>${nickname}</strong>님!<br />즐거운 시간 되세요.</div>
			        </div>
				</div>
				<form id="sidebar-logoutForm" action="/api/user/logout" method="post">
					<button type="submit" class="btn btn-primary-custom btn-block">로그아웃</button>
				</form>
				<c:if test="${not empty LOGIN_ADMIN_ID}">
					<hr>
					<h4>관리자 메뉴</h4>
					<ul class="list-group">
						<li class="list-group-item"><a href="/admin/user/manage">사용자 관리</a></li>
						<li class="list-group-item"><a href="/admin/category/manage">카테고리 관리</a></li>
					</ul>
				</c:if>
			</sec:authorize>
			<sec:authorize access="isAnonymous()">
				<form id="loginForm" action="/api/user/login" method="post">
					<div class="form-group">
						<label for="nickname">Nickname</label>
						<input type="text" class="form-control" id="nickname" name="nickname" placeholder="Enter nickname" autocomplete="current-password">
					</div>
					<div class="form-group">
						<label for="password">Password</label>
						<input type="password" class="form-control" id="password" name="password" placeholder="Enter password" autocomplete="current-password">
					</div>
					<button type="submit" class="btn btn-primary-custom btn-block">로그인</button>
				</form>
			</sec:authorize>
			<hr>
            <h4>사이트 메뉴</h4>
            <ul class="list-group">
                <li class="list-group-item"><a href="/category/all">전체 카테고리 보기</a></li>
            </ul>
			<hr>
			<h4>조회수 높은 게시글</h4>
			<ul class="list-group">
			    <c:forEach items="${homeDTO.mostViewedPosts}" var="post" varStatus="status">
			        <a href="/board/${post.id}" class="list-group-item d-flex justify-content-between align-items-center">
			            <span class="rank-badge">${status.index + 1}</span>
			            <span class="post-title">${post.title}</span>
			            <span class="view-count-badge">조회수: ${post.views}</span>
			        </a>
			    </c:forEach>
			</ul>
		</div>
	</aside>
	<main class="col-md-8">
		<div class="main">
			<ul class="nav nav-pills nav-justified nav-custom">
    			<!-- 전체 탭 고정 -->
    			<li class="category nav-link active" data-category="0">전체</li>

    			<!-- 카테고리 탭 반복문 -->
    			<c:forEach var="category" items="${homeDTO.showOnHomeCategoryList}">
        			<li class="category nav-link" data-category="${category.id}">${category.name}</li>
    			</c:forEach>
			</ul>
			<div class="form-group mt-3">
			    <div class="input-group">
			        <div class="input-group-prepend">
			            <select class="custom-select" id="searchFieldSelect">
			                <option value="title" selected>제목</option>
			                <option value="contents">내용</option>
			                <option value="nickname">작성자</option>
			            </select>
			        </div>
			        <input type="text" class="form-control" id="searchKeyword" placeholder="검색어를 입력하세요">
			        <div class="input-group-append">
			            <button class="btn btn-primary" type="button" id="searchButton">검색</button>
			        </div>
			    </div>
			</div>
			<div class="d-flex justify-content-between align-items-center mt-3">
				<h4>글 목록</h4>
				<c:if test="${not empty LOGIN_USER_ID || not empty LOGIN_ADMIN_ID}">
					<a href="/board/create" class="btn btn-primary">글쓰기</a>
				</c:if>
			</div>
			<ul class="list-group mt-3" id="post-list-all">
				<c:forEach items="${homeDTO.recentPosts.postList}" var="post">
					<jsp:include page="../common/postItem.jsp">
						<jsp:param name="post" value="${post}" />
			        </jsp:include>
				</c:forEach>
			</ul>
			<div id="pagination-container">
			 	<jsp:include page="../common/pagination.jsp">
				    <jsp:param name="totalPages" value="${homeDTO.recentPosts.pagination.totalPages}" />
				    <jsp:param name="currentPage" value="${homeDTO.recentPosts.pagination.currentPage}" />
				</jsp:include>
			</div>
		</div>
	</main>
</div>

<script>
	$(document).ready(function() {
		let categoryId = '0';
		
		// 로그인
		$("#loginForm").on("submit", function (e) {
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
		});

		/* $("#loginForm").on("submit", function(e) {
			e.preventDefault();
			let nickname = $("#nickname").val().trim();
			let password = $("#password").val();
			$.post("/api/user/public/login", {
				"nickname": nickname,
				"password": password
			}).done(function(response) {
				if (response.code === "SUCCESS") {
					location.href = "/";
				} else {
					alert("로그인에 실패했습니다. 닉네임과 비밀번호를 확인하세요.");
				}
			}).fail(function(_, _, error) {
				console.log(error);
				alert("로그인 중 오류가 발생했습니다.");
			}).always(function() {
				$("#nickname").val("");
				$("#password").val("");
			});
		}); */
	
		// 로그아웃
		$("#sidebar-logoutForm").on("submit", function(e) {
			e.preventDefault();
			$.post("/api/user/logout").always(function() {
				location.reload();
			});
		});
		
		// 페이지네이션
	    const paginator = new Paginator(loadData, renderPosts, 16);
	    paginator.loadPage(0);
	    
	    
	    
		// *** 이벤트 ***
		// 검색 버튼 클릭 이벤트
	    $("#searchButton").on("click", function() {
	        searchPosts();
	        $("#searchKeyword").val("");
	    });

	    // 엔터 키 검색 이벤트
	    $("#searchKeyword").on("keyup", function(e) {
	        if (e.key === "Enter") {
	            searchPosts();
	        }
	    });
		
		// 카테고리 탭 이벤트
	    $(".category").on("click", function(e) {
	        e.preventDefault();
	        $(".category").removeClass("active");
	        $(this).addClass("active");

	        categoryId = $(this).data("category");
	        paginator.loadPage(0);
	    
	    });

	    
	    
	    // *** 함수 ***
	 	// 게시글 검색 함수
	    function searchPosts() {
	        const field = $("#searchFieldSelect").val();
	        const keyword = $("#searchKeyword").val().trim();
	        if (keyword === "") {
	            alert("검색어를 입력하세요.");
	            return;
	        }
	        location.href = `/search?page=0&size=10&field=\${field}&keyword=\${encodeURIComponent(keyword)}`;
	        $("#searchKeyword").val("");
	    }
	    
	 	// 게시글 데이터 로드 콜백 함수
	    function loadData(params) {
	        return $.ajax({
	            url: '/api/post/public',
	            type: 'GET',
	            data: {...params, categoryId: categoryId}
	        });
	    }

	    // 게시글 데이터 렌더링 콜백 함수
	    function renderPosts(data) {
	        let postList = $("#post-list-all");
	        postList.empty();
	        data.postList.forEach(post => {
	            let postItem = `
	                <a href="/board/\${post.id}" class="list-group-item d-flex justify-content-between align-items-center">
	                    <span class="post-title">\${post.title}</span>
	                    <span class="view-count-badge">조회수: \${post.views}</span>
	                </a>
	            `;
	            postList.append(postItem);
	        });
	    }
	});
</script>

<style>
	.sidebar {
	    padding: 15px;
	    border-radius: 8px;
	}
	
	.welcome-message {
	    text-align: center;
	    margin-bottom: 20px;
	}
	
	.welcome-message label {
	    font-size: 1.2rem;
	    color: #007bff;
	    font-weight: bold;
	}
	
	.welcome-message div {
	    margin-top: 10px;
	    font-size: 1rem;
	}
	
	.btn-primary-custom {
	    background-color: #007bff;
	    border: none;
	    color: white;
	    padding: 10px 15px;
	    text-align: center;
	    text-decoration: none;
	    display: inline-block;
	    font-size: 16px;
	    margin: 4px 2px;
	    cursor: pointer;
	    border-radius: 4px;
	}
	
	.btn-primary-custom:hover {
	    background-color: #0056b3;
	}
	
	.list-group-item {
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	    padding: 10px 15px;
	    border-radius: 5px;
	    transition: background-color 0.2s;
	}
	
	.list-group-item:hover {
	    background-color: #f8f9fa;
	    text-decoration: none;
	    color: inherit;
	}
	
	.post-title {
	    flex-grow: 1;
	    margin-right: 10px;
	    white-space: nowrap;
	    overflow: hidden;
	    text-overflow: ellipsis;
	}
	
	.view-count-badge {
	    flex-shrink: 0;
	    color: #6c757d;
	    font-size: 0.9rem;
	}
	
	.rank-badge {
	    font-weight: bold;
	    font-size: 1.5rem;
	    color: #007bff;
	    font-style: italic;
	    margin-right: 10px;
	    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.1);
	}
</style>
