package com.community.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.community.category.CategoryNotFoundException;
import com.community.category.InvalidCategoryException;
import com.community.comment.CommentNotFoundException;
import com.community.comment.InvalidCommentException;
import com.community.common.presentation.dto.CommonResponse;
import com.community.post.InvalidPostException;
import com.community.post.PostNotFoundException;
import com.community.user.PasswordMismatchException;
import com.community.user.UnauthorizedException;
import com.community.user.UserAlreadyExistsException;
import com.community.user.UserNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	// 에러 페이지 반환
	@ExceptionHandler({ UserNotFoundException.class, PostNotFoundException.class, CommentNotFoundException.class,
			CategoryNotFoundException.class })
	public ModelAndView handleNotFoundException(RuntimeException ex, HttpServletRequest request) {
		request.setAttribute("status", HttpStatus.NOT_FOUND.value());
	    request.setAttribute("error", ex.getMessage());

	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("error");
	    mav.setStatus(HttpStatus.NOT_FOUND);
	    return mav;
	}

	@ExceptionHandler(UnauthorizedException.class)
	public ModelAndView handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
		request.setAttribute("status", HttpStatus.UNAUTHORIZED.value());
	    request.setAttribute("error", ex.getMessage());
	    
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("error");
	    mav.setStatus(HttpStatus.UNAUTHORIZED);
	    return mav;
	}
	
	@ExceptionHandler({ InternalServerErrorException.class, PageProcessingException.class })
	public ModelAndView handleInternalServerErrorException(InternalServerErrorException ex, HttpServletRequest request) {
		request.setAttribute("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
	    request.setAttribute("error", ex.getMessage());
	    
	    ModelAndView mav = new ModelAndView();
	    mav.setViewName("error");
	    mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
	    return mav;
	}
	
	// 에러 코드 반환
	@ExceptionHandler({ PasswordMismatchException.class, InvalidPostException.class,
			InvalidCommentException.class, InvalidCategoryException.class, })
	public ResponseEntity<CommonResponse<Void>> handleBadRequestException(RuntimeException ex) {
		CommonResponse<Void> response = CommonResponse.error(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ UserAlreadyExistsException.class })
	public ResponseEntity<CommonResponse<Void>> handleConflictException(RuntimeException ex) {
		CommonResponse<Void> response = CommonResponse.error(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<CommonResponse<Void>> handleGeneralException(Exception ex) {
		CommonResponse<Void> response = CommonResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR",
				"예기치 않은 오류가 발생했습니다.");
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
