package com.shopme.admin.category;

public class CategoryPageInfo {
	private int totalPage;
	private long totalElements;
	
	
	public CategoryPageInfo() {
		
	}
	public long getTotalElements() {
		return totalElements;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}
}
