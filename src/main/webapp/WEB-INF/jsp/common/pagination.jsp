<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<c:set var="totalPages" value="${param.totalPages}" />
<c:set var="currentPage" value="${param.currentPage}" />
<c:set var="startPage" value="${currentPage - 2}" />
<c:set var="endPage" value="${currentPage + 2}" />

<c:if test="${startPage < 0}">
    <c:set var="startPage" value="0" />
</c:if>

<c:if test="${endPage >= totalPages}">
    <c:set var="endPage" value="${totalPages - 1}" />
</c:if>


<div class="d-flex justify-content-center mt-4">
	<c:if test="${totalPages > 1}">
		<ul class="pagination justify-content-center">
			<li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                <a class="page-link pagination-link" data-page="0"><<</a>
            </li>

			<li class="page-item ${currentPage == 0 ? 'disabled' : ''}">
                <a class="page-link pagination-link" data-page="${currentPage - 1}"><</a>
            </li>

			<c:forEach var="i" begin="${startPage}" end="${endPage}">
				<li class="page-item ${i == currentPage ? 'active' : ''}">
                    <a class="page-link pagination-link" data-page="${i}">${i + 1}</a>
                </li>
			</c:forEach>

			<li class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link pagination-link" data-page="${currentPage + 1}">></a>
            </li>

			<li class="page-item ${currentPage == totalPages - 1 ? 'disabled' : ''}">
                <a class="page-link pagination-link" data-page="${totalPages - 1}">>></a>
            </li>
		</ul>
	</c:if>
</div>


