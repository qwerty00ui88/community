<%@ page language="java" contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% 
    String field = request.getParameter("field");
	String keyword = request.getParameter("keyword");
%>

<div class="container mt-5">
    <h2 class="category-title text-center mb-5">${title}</h2>
    <div class="row">
        <div class="col-md-12">
	      	<ul class="list-group mt-3" id="post-list-all">
				<c:forEach items="${postList.postList}" var="post">
					<jsp:include page="../common/postItem.jsp">
						<jsp:param name="post" value="${post}" />
			        </jsp:include>
				</c:forEach>
			</ul>
        </div>
    </div>
    <jsp:include page="../common/pagination.jsp">
	    <jsp:param name="totalPages" value="${postList.pagination.totalPages}" />
	    <jsp:param name="currentPage" value="${postList.pagination.currentPage}" />
	</jsp:include>
</div>

<script>
$(document).ready(function() {
	const categoryId = ${categoryId};
	
	// 페이지네이션
    const paginator = new Paginator(loadData, renderPosts);
    paginator.loadPage(0);
    
 	// 게시글 데이터 로드 콜백 함수
    function loadData(params) {
        return $.ajax({
            url: ${categoryId} == '0' ? 'api/post/search' : 'api/post',
            type: 'GET',
            data: ${categoryId} == '0' ? {...params, field : "<%= field %>", keyword : "<%= keyword %>"} : {...params, categoryId: categoryId}
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
	
})
</script>
<style>
	.container h2 {
	    font-weight: bold;
	    font-size: 1.75rem;
	    margin-bottom: 30px;
	    white-space: normal;
	    max-width: 100%;
	    word-wrap: break-word;
	}
	
	.list-group-item {
	    background-color: #ffffff;
	    border: 1px solid #dedede;
	    padding: 10px 15px;
	    border-radius: 5px;
	    margin-bottom: 10px;
	    text-decoration: none;
	    color: inherit;
	    display: block;
	    transition: background-color 0.2s ease;
	}
	
	.list-group-item:hover {
	    background-color: #f8f9fa;
	    text-decoration: none;
	    color: inherit;
	}
	
	.post-link {
	    font-size: 1rem;
	    font-weight: 600;
	    color: #007bff;
	}
	
	.post-preview {
	    font-size: 0.9rem;
	    color: #555555;
	    margin-left: 10px;
	}
	
	.text-muted {
	    font-size: 0.8rem;
	    color: #6c757d;
	}
	
	.pagination .page-link {
	    color: #007bff;
	}
	
	.pagination .page-link:hover {
	    color: #0056b3;
	}
	
	.pagination .page-item.active .page-link {
	    background-color: #007bff;
	    border-color: #007bff;
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
	
</style>
