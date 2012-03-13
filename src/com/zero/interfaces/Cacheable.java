package com.zero.interfaces;


public interface Cacheable {
	public void prepareForCache();
	public void reuse();
	public String getCacheIdentifier();
}
