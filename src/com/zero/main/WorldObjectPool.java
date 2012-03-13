package com.zero.main;

import java.util.ArrayList;
import java.util.HashMap;

import com.zero.interfaces.Cacheable;

public class WorldObjectPool {

	private static WorldObjectPool pool = null;
	private HashMap<String, ArrayList<Cacheable>> cacheMap = null;

	public void cache(Cacheable object) {
		String identifier = object.getCacheIdentifier();

		ArrayList<Cacheable> list;

		if (cacheMap.containsKey(identifier)) {
			list = cacheMap.get(identifier);
		} else {
			list = new ArrayList<Cacheable>(20);
			cacheMap.put(identifier, list);
		}

		object.prepareForCache();
		list.add(object);
	}

	public Cacheable reuse(String indentifier) {
		if (!cacheMap.containsKey(indentifier)) {
			return null;
		}

		ArrayList<Cacheable> list = cacheMap.get(indentifier);
		if (list.size() < 1) {
			return null;
		}

		Cacheable object = list.get(0);
		list.remove(0);

		object.reuse();
		return object;
	}

	protected WorldObjectPool() {
		cacheMap = new HashMap<String, ArrayList<Cacheable>>(5);
	}

	public static WorldObjectPool getInstance() {
		if (pool == null) {
			pool = new WorldObjectPool();
		}

		return pool;
	}
}
