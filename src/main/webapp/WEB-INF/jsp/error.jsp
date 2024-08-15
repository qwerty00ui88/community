<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

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
<c:if test="${not empty LOGIN_ADMIN_ID}">
	<link rel="stylesheet" type="text/css" href="/static/css/admin.css">
</c:if>

</head>
<body>
<div class="error-container">
    <div class="error-code">Error <%= request.getAttribute("status") %></div> <!-- 에러 코드를 동적으로 설정할 수 있습니다. -->
    <div class="error-message"><%= request.getAttribute("error") %></div> <!-- 기본 메시지 -->    
    <a href="/" class="btn btn-primary-custom">홈으로 돌아가기</a>
</div>

<style>
    body {
        background-color: #f8f9fa; /* 밝은 회색 배경 */
        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        color: #495057;
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;
        margin: 0;
    }

    .error-container {
        text-align: center;
        padding: 30px;
        background-color: #f8f9fa; /* 배경과 동일한 색상으로 테두리 효과 제거 */
        max-width: 380px;
        width: 100%;
    }

    .error-code {
        font-size: 4rem;
        font-weight: 500;
        color: #495057;
        margin-bottom: 15px;
    }

    .error-message {
        font-size: 1.125rem;
        color: #6c757d;
        margin-bottom: 25px;
    }

    .btn-primary-custom {
        background-color: #495057;
        border: none;
        color: white;
        padding: 10px 20px;
        text-align: center;
        text-decoration: none;
        font-size: 16px;
        border-radius: 4px;
        transition: background-color 0.2s;
        display: inline-block;
    }

    .btn-primary-custom:hover {
        background-color: #343a40;
    }

    @media (max-width: 576px) {
        .error-code {
            font-size: 3.5rem;
        }

        .error-message {
            font-size: 1rem;
        }
    }
</style>
</body>
</html>