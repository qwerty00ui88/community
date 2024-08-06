<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div class="container create-post-container">
	<h2 class="text-center">게시글 작성</h2>
	<form action="createPost" method="post" enctype="multipart/form-data"
		onsubmit="return submitForm()">
		<div class="form-group">
			<label for="title">제목</label> <input type="text" class="form-control"
				id="title" name="title" placeholder="제목 입력" required>
		</div>
		<div class="form-group">
			<label for="category">카테고리</label> <select class="form-control"
				id="category" name="category" required>
				<option value="category1">카테고리 1</option>
				<option value="category2">카테고리 2</option>
				<option value="category3">카테고리 3</option>
			</select>
		</div>
		<div class="form-group">
			<label for="editor">내용</label>
			<div id="editor" class="editor" contenteditable="true"></div>
			<textarea id="content" name="content" style="display: none;"></textarea>
		</div>
		<div class="form-group">
			<label for="imageUpload">이미지 업로드</label> <input type="file"
				class="form-control-file" id="imageUpload" accept="image/*"
				onchange="insertImage(event)">
		</div>
		<button type="submit" class="btn btn-primary-custom btn-block">게시글
			작성</button>
	</form>

	<script>
		function insertImage(event) {
			var file = event.target.files[0];
			if (file) {
				var reader = new FileReader();
				reader.onload = function(e) {
					var img = document.createElement('img');
					img.src = e.target.result;
					document.getElementById('editor').appendChild(img);
				}
				reader.readAsDataURL(file);
			}
		}

		function submitForm() {
			var editorContent = document.getElementById('editor').innerHTML;
			document.getElementById('content').value = editorContent;
			return true;
		}
	</script>
</div>