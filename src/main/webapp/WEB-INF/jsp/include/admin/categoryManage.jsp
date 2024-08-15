<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container mt-5">
    <div class="main p-4">
        <h2 class="text-center mb-4">카테고리 관리</h2>

        <div class="d-flex justify-content-between mb-4">
            <button class="btn btn-primary-custom" data-toggle="modal" data-target="#addCategoryModal">카테고리 추가</button>
        </div>

        <div class="row">
            <c:forEach var="category" items="${categoryList}">
                <div class="col-md-4">
                    <div class="card mb-4">
                        <div class="card-body">
                            <h5 class="card-title"><a href="/category?categoryId=${category.id}" class="text-decoration-none">${category.name}</a></h5>
                            <p class="card-text">
                                상태: 
                                <strong class="${category.status == 'ACTIVE' ? 'text-success' : 'text-danger'}">
                                    ${category.status == 'ACTIVE' ? '활성' : '비활성'}
                                </strong>
                                <br>
                                <c:if test="${category.status == 'ACTIVE'}">
                                    홈에 표시: 
                                    <input type="checkbox" class="show-on-home-checkbox" data-category-id="${category.id}" data-category-status="${category.status}" ${category.showOnHome ? 'checked' : ''}>
                                </c:if>
                            </p>
                            <div class="d-flex justify-content-between">
                                <button type="button" class="btn btn-primary-custom btn-sm" data-category-id="${category.id}" data-toggle="modal" data-target="#editCategoryModal">수정</button>
                                <button type="button" class="btn btn-outline-danger btn-sm" data-category-id="${category.id}">삭제</button>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>

<!-- 카테고리 추가 모달 -->
<div class="modal fade" id="addCategoryModal" tabindex="-1" role="dialog" aria-labelledby="addCategoryModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="addCategoryModalLabel">카테고리 추가</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="addCategoryForm">
                <div class="modal-body">
                    <div class="form-group">
                        <label for="categoryName">카테고리 이름</label>
                        <input type="text" class="form-control" id="categoryName" name="categoryName" required>
                    </div>
                    <div class="form-group">
                        <label for="categoryStatus">상태</label>
                        <select class="form-control" id="categoryStatus" name="categoryStatus">
                            <option value="ACTIVE">활성</option>
                            <option value="INACTIVE">비활성</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary-custom">추가</button>
                </div>
            </form>
        </div>
    </div>
</div>

<!-- 카테고리 수정 모달 -->
<div class="modal fade" id="editCategoryModal" tabindex="-1" role="dialog" aria-labelledby="editCategoryModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="editCategoryModalLabel">카테고리 수정</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="editCategoryForm">
                <div class="modal-body">
                    <input type="hidden" id="editCategoryId" name="editCategoryId">
                    <div class="form-group">
                        <label for="editCategoryName">카테고리 이름</label>
                        <input type="text" class="form-control" id="editCategoryName" name="editCategoryName" required>
                    </div>
                    <div class="form-group">
                        <label for="editCategoryStatus">상태</label>
                        <select class="form-control" id="editCategoryStatus" name="editCategoryStatus">
                            <option value="ACTIVE">활성</option>
                            <option value="INACTIVE">비활성</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="submit" class="btn btn-primary-custom">저장</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
$(document).ready(function() {
    // 홈에 표시 체크박스 변경 이벤트
    $(".show-on-home-checkbox").on("change", function() {
        const categoryId = $(this).data("category-id");
        const showOnHome = $(this).is(":checked");
        const categoryStatus = $(this).data("category-status"); 
        console.log(showOnHome, categoryStatus)
        
        if(showOnHome && categoryStatus == 'INACTIVE') {
        	$(this).prop("checked", false);
        	alert("상태를 활성화 시켜주세요.");
        	return;
        }
        
        $.ajax({
            url: "/api/category/admin/showOnHome",
            type: "PATCH",
            data: { categoryId: categoryId, showOnHome: showOnHome },
            success: function(response) {
                alert("카테고리 홈 표시 상태가 변경되었습니다.");
            },
            error: function(xhr, status, error) {
                alert("카테고리 홈 표시 상태 변경 중 오류가 발생했습니다.");
            }
        });
    });

    // 카테고리 추가
    $("#addCategoryForm").on("submit", function(e) {
        e.preventDefault();
        
        const categoryName = $("#categoryName").val();
        const categoryStatus = $("#categoryStatus").val();

        $.ajax({
            url: "/api/category/admin",
            type: "POST",
            data: { name: categoryName, status: categoryStatus },
            success: function(response) {
                alert("카테고리가 성공적으로 추가되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("카테고리 추가 중 오류가 발생했습니다.");
            }
        });
    });

    // 카테고리 수정 모달에 데이터 로드
    $('#editCategoryModal').on('show.bs.modal', function(event) {
        const button = $(event.relatedTarget);
        const categoryId = button.data('category-id');
        const categoryName = button.closest('.card-body').find('.card-title').text().trim();
        const categoryStatus = button.closest('.card-body').find('.card-text strong').text().trim() == '활성' ? 'ACTIVE' : 'INACTIVE';
        
        
        const modal = $(this);
        modal.find('#editCategoryId').val(categoryId);
        modal.find('#editCategoryName').val(categoryName);
        modal.find('#editCategoryStatus').val(categoryStatus);
    });

    // 카테고리 수정
    $("#editCategoryForm").on("submit", function(e) {
        e.preventDefault();
        
        const categoryId = $("#editCategoryId").val();
        const categoryName = $("#editCategoryName").val();
        const categoryStatus = $("#editCategoryStatus").val();

        $.ajax({
            url: "/api/category/admin/update",
            type: "PATCH",
            data: { categoryId: categoryId, name: categoryName, status: categoryStatus },
            success: function(response) {
                alert("카테고리가 성공적으로 수정되었습니다.");
                location.reload();
            },
            error: function(xhr, status, error) {
                alert("카테고리 수정 중 오류가 발생했습니다.");
            }
        });
    });

    // 카테고리 삭제
    $(".btn-outline-danger").on("click", function(e) {
        e.preventDefault();

        if (confirm("정말로 삭제하시겠습니까?")) {
            const categoryId = $(this).data("category-id");
            
            $.ajax({
                url: "/api/category/admin/" + categoryId,
                type: "DELETE",
                data: { categoryId: categoryId },
                success: function(response) {
                    alert("카테고리가 성공적으로 삭제되었습니다.");
                    location.reload();
                },
                error: function(xhr, status, error) {
                    alert("카테고리 삭제 중 오류가 발생했습니다.");
                }
            });
        }
    });
});
</script>
<style>
	.card-title > a {
		color: inherit;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		display: block;
	}
</style>
