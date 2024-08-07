package com.community.utils;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {
	private static final String LOGIN_USER_ID = "LOGIN_USER_ID";
	private static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";

	private SessionUtil() {
	}

	public static Integer getLoginId(HttpSession session) {
		Integer memberId = getLoginUserId(session);
		if (memberId != null) {
			return memberId;
		}
		return getLoginAdminId(session);
	}

	public static Integer getLoginUserId(HttpSession session) {
		return (Integer) session.getAttribute(LOGIN_USER_ID);
	}

	public static void setLoginMemberId(HttpSession session, int id) {
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
}
