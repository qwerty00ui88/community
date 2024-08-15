<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<a href="/board/${post.id}" class="list-group-item d-flex justify-content-between align-items-center">
    <span class="post-title post-item-title">${post.title}</span>
    <span class="view-count-badge">조회수: ${post.views}</span>
</a>
