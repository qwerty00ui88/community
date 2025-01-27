<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="container mt-5">
    <div class="main p-4">
        <h2 class="text-center mb-4">사용자 관리</h2>
        <table class="table table-bordered table-hover">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>이름</th>
                    <th>닉네임</th>
                    <th>상태</th>
                    <th>작업</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="user" items="${userList}">
                    <tr>
                        <td>${user.id}</td>
                        <td>${user.name}</td>
                        <td>${user.nickname}</td>
                        <td>
						    <div class="input-group">
						        <select class="form-control status-select" data-user-id="${user.id}">
						            <option value="ACTIVE" ${user.status == 'ACTIVE' ? 'selected' : ''}>ACTIVE</option>
						            <option value="INACTIVE" ${user.status == 'INACTIVE' ? 'selected' : ''}>INACTIVE</option>
						            <option value="DELETED" ${user.status == 'DELETED' ? 'selected' : ''}>DELETED</option>
						        </select>
						        <div class="input-group-append">
						            <button class="btn btn-primary update-status-btn" type="button">저장</button>
						        </div>
						    </div>
						</td>
                        <td>
                            <a href="/profile/${user.id}" class="btn btn-sm btn-primary-custom">프로필 보기</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script>
$(document).ready(function() {
 	// 사용자 상태 변경
    $('.update-status-btn').on('click', function() {
        const userId = $(this).closest('.input-group').find('.status-select').data('user-id');
        const selectedStatus = $(this).closest('.input-group').find('.status-select').val().trim();

        $.ajax({
            url: '/api/user/admin/updateUserStatus',
            type: 'Patch',
            data: {
            	id: userId, 
            	status: selectedStatus 
            },
            success: function(response) {
                alert('사용자 상태가 성공적으로 변경되었습니다.');
            },
            error: function(xhr, status, error) {
                console.log('Error:', error);
                alert('사용자 상태 변경 중 오류가 발생했습니다.');
                location.reload();
            }
        });
    });
});
</script>
