package com.junova.huizhong.common.http;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

public class AsyncConnectionManager {
	public static final int MAX_CONNECTIONS = 5;
	private ArrayList<Runnable> active = new ArrayList<Runnable>();
	private ArrayList<Runnable> queue = new ArrayList<Runnable>();

	private static AsyncConnectionManager instance;

	public static AsyncConnectionManager getInstance() {
		if (instance == null)
			instance = new AsyncConnectionManager();
		return instance;
	}

	public void push(Runnable runnable) {
		queue.add(runnable);
		if (active.size() < MAX_CONNECTIONS)
			startNext();
	}

	public void pushOnce(Runnable runnable) {
		queue.add(runnable);
		if (active.size() < 2)
			startNext();
	}

	private void startNext() {
		if (!queue.isEmpty()) {
			Runnable next = queue.get(0);

			queue.remove(0);
			active.add(next);

			Thread thread = new Thread(next);
			thread.start();
		}
	}

	public void didComplete(Runnable runnable) {
		active.remove(runnable);
		startNext();
	}

	public void didRemove(Runnable runnable) {
		if (active.size() > 0) {
			active.remove(runnable);
		}
	}
}
