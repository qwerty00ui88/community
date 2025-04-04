<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>👋 Hi️ Community</title>
<%-- Bootstrap --%>
<link rel="stylesheet"
	href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/css/bootstrap.min.css"
	integrity="sha384-xOolHFLEh07PJGoPkLv1IbcEPTNtaed2xpHsD9ESMhqIYd0nLMwNLD69Npy4HI+N"
	crossorigin="anonymous">
<script src="https://code.jquery.com/jquery-3.7.1.js"
	integrity="sha256-eKhayi8LEQwp4NKxN+CfCh+3qOVUtJn3QNZ0TciWLP4="
	crossorigin="anonymous"></script>
<script
	src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js"
	integrity="sha384-Fy6S3B9q64WdZWQUiU+q4/2Lc9npb8tCaSX9FK7E8HnRr0Jz8D6OP9dO5Vg3Q9ct"
	crossorigin="anonymous"></script>
<script src="/static/js/pagination.js"></script>
<%-- Common CSS --%>
<link rel="stylesheet" type="text/css" href="/static/css/style.css">
<%-- 사용자별 CSS --%>
<sec:authorize access="hasRole('ROLE_ADMIN')">
	<link rel="stylesheet" type="text/css" href="/static/css/admin.css">
</sec:authorize>

</head>
<body class="<sec:authorize access="hasRole('ROLE_ADMIN')">admin-theme</sec:authorize>
              <sec:authorize access="!hasRole('ROLE_ADMIN')">user-theme</sec:authorize>">
	<div class="container">
		<header class="text-center mb-4">
			<jsp:include page="../common/header.jsp" />
		</header>
		<section>
			<jsp:include page="../${viewName}.jsp" />
		</section>
		<footer class="footer text-center mt-4">
			<jsp:include page="../common/footer.jsp" />
		</footer>
	</div>
</body>
</html>