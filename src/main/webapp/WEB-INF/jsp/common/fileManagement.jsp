<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<div class="form-group">
    <label for="newFiles">파일 첨부</label>

    <!-- 기존 파일 리스트 -->
    <ul id="fileList" class="mt-2">
        <c:forEach var="file" items="${fileList}">
            <li class="d-flex justify-content-between align-items-center mb-1">
                <span>
                    <a href="${file.url}" target="_blank">${file.metadataObject.originalFilename}</a>
                </span>
                <button type="button" class="btn btn-sm btn-danger remove-existing-file-btn" data-file-id="${file.id}">
                    삭제
                </button>
            </li>
        </c:forEach>
    </ul>

    <!-- 새 파일 업로드 -->
    <input type="file" class="form-control d-none" id="newFiles" name="newFiles" multiple>
    <button type="button" id="addFileBtn" class="btn btn-primary mt-2">파일 추가</button>
</div>
<script>
$(document).ready(function () {
    const fileList = document.getElementById('fileList');
    window.newFilesArray = window.newFilesArray || [];
    window.removedFilesArray = window.removedFilesArray || [];

    // 파일 추가 버튼
    $("#addFileBtn").on("click", function () {
        $("#newFiles").click();
    });

    // 파일 선택
    $("#newFiles").on("change", function (e) {
    	const files = Array.from(e.target.files);
        files.forEach((file, index) => {
        	newFilesArray.push(file);

            const li = document.createElement('li');
            li.classList.add('d-flex', 'justify-content-between', 'align-items-center', 'mb-1');

            const span = document.createElement('span');

            const a = document.createElement('a');
            a.href = "#";
            a.target = "_blank";
            a.textContent = file.name;

            const button = document.createElement('button');
            button.type = "button";
            button.classList.add('btn', 'btn-sm', 'btn-danger', 'remove-new-file-btn');
            button.dataset.index = newFilesArray.length - 1;
            button.textContent = "삭제";

            span.appendChild(a);
            li.appendChild(span);
            li.appendChild(button);
            
            fileList.appendChild(li);
        });
        $("#newFiles").val('');
    });

 	// 파일 삭제 버튼
    fileList.addEventListener("click", function (event) {
        // 기존 파일 삭제 처리
        if (event.target.classList.contains("remove-existing-file-btn")) {
            const fileId = parseInt(event.target.getAttribute("data-file-id"), 10);
            removedFilesArray.push(fileId);
            event.target.parentElement.remove();
            console.log("삭제된 기존 파일 ID:", removedFilesArray);
        }

        // 새 파일 삭제 처리
        if (event.target.classList.contains("remove-new-file-btn")) {
            const index = parseInt(event.target.getAttribute("data-index"), 10);
            newFilesArray.splice(index, 1);
            event.target.parentElement.remove();
            console.log("남아있는 새 파일:", newFilesArray);
        }
    });
});
</script>