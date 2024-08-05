<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="row">
	<aside class="col-md-4">
		<div class="sidebar">
			<form>
				<div class="form-group">
					<label for="username">Username</label> <input type="text"
						class="form-control" id="username" placeholder="Enter username">
				</div>
				<div class="form-group">
					<label for="password">Password</label> <input type="password"
						class="form-control" id="password" placeholder="Enter password">
				</div>
				<button type="submit" class="btn btn-primary-custom btn-block">로그인</button>
				<button type="button" class="btn btn-secondary btn-block">ID/PW
					찾기</button>
			</form>
			<hr>
			<h3>추천수가 높은순 리스트</h3>
			<ul class="list-group">
				<li class="list-group-item"><a href="post1.jsp">게시글 1</a></li>
				<li class="list-group-item"><a href="post2.jsp">게시글 2</a></li>
				<li class="list-group-item"><a href="post3.jsp">게시글 3</a></li>
			</ul>
		</div>
	</aside>
	<main class="col-md-8">
		<div class="main">
			<nav class="nav nav-pills nav-justified nav-custom">
				<a class="nav-link active" href="#">전체</a> <a class="nav-link"
					href="#">카테고리 1</a> <a class="nav-link" href="#">카테고리 2</a>
			</nav>
			<div class="form-group">
				<div class="input-group">
					<input type="text" class="form-control" id="search"
						placeholder="검색 단어">
					<div class="input-group-append">
						<button class="btn" type="button">검색</button>
					</div>
				</div>
			</div>
			<h2>글 목록</h2>
			<ul class="list-group">
				<li class="list-group-item"><a href="post1.jsp">게시글 1</a></li>
				<li class="list-group-item"><a href="post2.jsp">게시글 2</a></li>
				<li class="list-group-item"><a href="post3.jsp">게시글 3</a></li>
				<!-- 더 많은 게시글을 추가할 수 있습니다. -->
			</ul>
		</div>
	</main>
</div>