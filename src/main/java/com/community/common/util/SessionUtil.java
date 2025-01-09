package com.community.common.util;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {
	private static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	private static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";
	private static final String LOGIN_NICKNAME = "LOGIN_NICKNAME";

	public static Integer getLoginId(HttpSession session) {
		Integer userId = getLoginUserId(session);
		if (userId != null) {
			return userId;
		}
		return getLoginAdminId(session);
	}

	public static Integer getLoginUserId(HttpSession session) {
		return (Integer) session.getAttribute(LOGIN_USER_ID);
	}

	public static void setLoginUserId(HttpSession session, int id) {
		session.setAttribute(LOGIN_USER_ID, id);
	}

	public static Integer getLoginAdminId(HttpSession session) {
		return (Integer) session.getAttribute(LOGIN_ADMIN_ID);
	}

	public static void setLoginAdminId(HttpSession session, int id) {
		session.setAttribute(LOGIN_ADMIN_ID, id);
	}

	public static void clear(HttpSession session) {
		session.invalidate();
	}

	public static boolean isLoggedIn(HttpSession session) {
		return getLoginUserId(session) != null || getLoginAdminId(session) != null;
	}
	
	public static void setLoginNickname(HttpSession session, String nickname) {
		session.setAttribute(LOGIN_NICKNAME, nickname);
	}
	
	public static String getLoginNickname(HttpSession session) {
		return (String) session.getAttribute(LOGIN_NICKNAME);
	}

}
