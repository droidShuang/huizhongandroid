package com.junova.huizhong.common;

import android.util.Log;

import com.junova.huizhong.AppConfig;

//common log
public class Logger {
	private int logLevel = Log.VERBOSE;
	private static Logger logger;
	private static String developer = "xiaohui_tu";
	private String className;

	private Logger(String name) {
		className = name;
	}

	public static Logger getInstance() {
		if (logger == null) {
			logger = new Logger(AppConfig.os);
		}
		return logger;
	}

	private String getFunctionName() {
		StackTraceElement[] sts = Thread.currentThread().getStackTrace();
		if (sts == null) {
			return null;
		}
		for (StackTraceElement st : sts) {
			if (st.isNativeMethod()) {
				continue;
			}
			if (st.getClassName().equals(Thread.class.getName())) {
				continue;
			}
			if (st.getClassName().equals(this.getClass().getName())) {
				continue;
			}
			return className + "[ " + Thread.currentThread().getName() + ": "
					+ st.getFileName() + ":" + st.getLineNumber() + " "
					+ st.getMethodName() + " ]";
		}
		return null;
	}

	public void i(String tag, Object str) {
		if (AppConfig.isDebug) {
			if (logLevel <= Log.INFO) {
				String name = getFunctionName();
				if (name != null) {
					Log.i(tag, name + " - " + str);
				} else {
					Log.i(tag, str.toString());
				}
			}
		}
	}

	public void d(String tag, Object str) {
		if (AppConfig.isDebug) {
			if (logLevel <= Log.DEBUG) {
				String name = getFunctionName();
				if (name != null) {
					Log.d(tag, name + " - " + str);
				} else {
					Log.d(tag, str.toString());
				}
			}
		}
	}

	public void v(String tag, Object str) {
		if (AppConfig.isDebug) {
			if (logLevel <= Log.VERBOSE) {
				String name = getFunctionName();
				if (name != null) {
					Log.v(tag, name + " - " + str);
				} else {
					Log.v(tag, str.toString());
				}
			}
		}
	}

	public void w(String tag, Object str) {
		if (AppConfig.isDebug) {
			if (logLevel <= Log.WARN) {
				String name = getFunctionName();
				if (name != null) {
					Log.w(tag, name + " - " + str);
				} else {
					Log.w(tag, str.toString());
				}
			}
		}
	}

	public void e(String tag, Object str) {
		if (AppConfig.isDebug) {
			if (logLevel <= Log.ERROR) {
				String name = getFunctionName();
				if (name != null) {
					Log.e(tag, name + " - " + str);
				} else {
					Log.e(tag, str.toString());
				}
			}
		}
	}

	public void e(String tag, Exception ex) {
		if (AppConfig.isDebug) {
			if (logLevel <= Log.ERROR) {
				Log.e(tag, "error", ex);
			}
		}
	}

	public void e(String tag, String log, Throwable tr) {
		if (AppConfig.isDebug) {
			String line = getFunctionName();
			Log.e(tag, "{Thread:" + Thread.currentThread().getName() + "}"
					+ "[" + className + line + ":] " + log + "\n", tr);
		}
	}
}
