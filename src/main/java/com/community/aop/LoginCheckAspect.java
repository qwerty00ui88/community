package com.community.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.community.utils.SessionUtil;

import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;

@Component
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE)
@Log4j2
public class LoginCheckAspect {

	@Around("@annotation(com.community.aop.LoginCheck) && @annotation(loginCheck)")
	public Object loginCheck(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
		HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest()
				.getSession();

		Integer id = getSessionId(session, loginCheck.type());
		if (id == null) {
			log.info("Unauthorized access attempt: " + joinPoint.toString());
			throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요.") {
			};
		}

		return proceedWithModifiedArgs(joinPoint, id);
	}

	private Integer getSessionId(HttpSession session, LoginCheck.UserType userType) {
		if (userType == LoginCheck.UserType.ADMIN) {
			return SessionUtil.getLoginAdminId(session);
		} else {
			return SessionUtil.getLoginMemberId(session);
		}
	}

	private Object proceedWithModifiedArgs(ProceedingJoinPoint joinPoint, Integer id) throws Throwable {
		Object[] args = joinPoint.getArgs();
		if (args != null && args.length > 0) {
			args[0] = id; // 첫 번째 인수에 사용자 ID를 설정
		}
		return joinPoint.proceed(args);
	}
}
