//package com.community.common.aop;
//
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.springframework.core.Ordered;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.community.common.util.SessionUtil;
//import com.community.user.UnauthorizedException;
//
//import jakarta.servlet.http.HttpSession;
//
//@Component
//@Aspect
//@Order(Ordered.LOWEST_PRECEDENCE)
//public class LoginCheckAspect {
//
//    @Around("@annotation(com.community.common.aop.LoginCheck) && @annotation(loginCheck)")
//    public Object loginCheck(ProceedingJoinPoint joinPoint, LoginCheck loginCheck) throws Throwable {
//    	HttpSession session = getCurrentHttpSession();
//
//        Integer id = getSessionId(session, loginCheck.type());
//        if (id == null) {
//            throw new UnauthorizedException("해당 작업을 수행할 권한이 없습니다.");
//        }
//
//        return proceedWithModifiedArgs(joinPoint, id);
//    }
//
//    private HttpSession getCurrentHttpSession() {
//        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
//    }
//
//    private Integer getSessionId(HttpSession session, LoginCheck.UserType userType) {
//        switch (userType) {
//            case ADMIN:
//                return SessionUtil.getLoginAdminId(session);
//            case USER:
//                return SessionUtil.getLoginUserId(session);
//            case ANY:
//                return SessionUtil.getLoginId(session);
//            default:
//                return null;
//        }
//    }
//
//    private Object proceedWithModifiedArgs(ProceedingJoinPoint joinPoint, Integer id) throws Throwable {
//        Object[] args = joinPoint.getArgs();
//        // userId가 null이면 현재 세션의 userId 할당
//        if (args != null && args.length > 0 && args[0] == null) {
//            args[0] = id;
//        }
//        return joinPoint.proceed(args);
//    }
//}
