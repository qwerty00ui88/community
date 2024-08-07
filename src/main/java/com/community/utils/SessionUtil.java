package com.community.utils;

import jakarta.servlet.http.HttpSession;

public class SessionUtil {
	private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";
	private static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";

	private SessionUtil() {
	}

	public static Integer getLoginId(HttpSession session) {
		Integer memberId = getLoginMemberId(session);
		if (memberId != null) {
			return memberId;
		}
		return getLoginAdminId(session);
	}

	public static Integer getLoginMemberId(HttpSession session) {
		return (Integer) session.getAttribute(LOGIN_MEMBER_ID);
	}

	public static void setLoginMemberId(HttpSession session, int id) {
		session.setAttribute(LOGIN_MEMBER_ID, id);
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
		return getLoginMemberId(session) != null || getLoginAdminId(session) != null;
	}
}
