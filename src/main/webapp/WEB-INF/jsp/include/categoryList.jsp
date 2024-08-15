<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<div class="container mt-5">
    <h2 class="text-center mb-5">전체 카테고리 보기</h2>
    <div class="row">
        <c:forEach var="category" items="${categoryList}">
            <div class="col-md-6 mb-4">
                <a href="/category?categoryId=${category.categoryId}" class="category-card-link">
                    <div class="category-card p-4">
                        <h3 class="category-title">${category.categoryName}</h3>
                        <ul class="list-unstyled mt-3">
                            <c:forEach var="post" items="${category.postList}">
                                <li class="mb-2">
                                    <a href="/board/${post.id}" class="post-link">
                                        <span class="post-title">${post.title}</span>
                                        <span class="post-date">${fn:substring(post.createdAt.toString(), 0, 10)}</span>
                                    </a>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </a>
            </div>
        </c:forEach>
    </div>
</div>
<style>
	body {
	    background-color: #f8f9fa;
	}
	
	.container h2 {
	    font-weight: bold;
	    font-size: 2rem;
	}
	
	.category-card-link {
	    text-decoration: none;
	}
	
	.category-card {
	    background-color: #fff;
	    border-radius: 10px;
	    border: 1px solid #dedede;
	    transition: box-shadow 0.3s ease;
	}
	
	.category-card:hover {
	    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
	}
	
	.category-title {
	    font-size: 1.5rem;
	    font-weight: 600;
	    color: #333;
	    margin-bottom: 15px;
	    border-bottom: 2px solid #e0e0e0;
	    padding-bottom: 8px;
	    white-space: nowrap;
	    overflow: hidden;
	    text-overflow: ellipsis;
	}
	
	.post-link {
	    display: flex;
	    justify-content: space-between;
	    align-items: center;
	    text-decoration: none;
	    font-size: 1rem;
	    color: #007bff;
	    padding: 5px 0;
	    transition: color 0.3s ease;
	}
	
	.post-link:hover {
	    color: #0056b3;
	    text-decoration: underline;
	}
	
	.post-title {
	    flex: 1;
	    margin-right: 10px;
	    white-space: nowrap;
	    overflow: hidden;
	    text-overflow: ellipsis;
	}
	
	.post-date {
	    font-size: 0.9rem;
	    color: #6c757d;
	}
</style>
