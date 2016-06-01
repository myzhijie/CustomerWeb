package com.jingsky.customer.util.base;

import java.util.ArrayList;
import java.util.List;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * <p/>
 * 注意所有序号从1开始.
 *
 * @param <T>
 *            Page中记录的类型.
 */
public class Page<T> {

	protected int pageStart = 1;
	protected int pageSize = 0;
	protected List<T> data =new ArrayList<T>();
	protected long totalCount = 0;

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	public Page(int pageStartPos, int pageSize) {
		this.pageStart = pageStartPos;
		this.pageSize = pageSize;
	}

	public int getPageStart() {
		return pageStart;
	}

	public void setPageStart(final int pageStartPos) {
		this.pageStart = pageStartPos;
		if (pageStartPos < 1) {
			this.pageStart = 1;
		}
	}

	public Page<T> pageStartPos(final int pageStartPos) {
		setPageStart(pageStartPos);
		return this;
	}

	public int getPageSize() {
		return pageSize;
	}

	/**
	 * 设置每页的记录数量.
	 */
	public void setPageSize(final int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * 返回Page对象自身的setPageSize函数,可用于连续设置。
	 */
	public Page<T> pageSize(final int thePageSize) {
		setPageSize(thePageSize);
		return this;
	}

	/**
	 * 获得页内的记录列表.
	 */
	public List<T> getData() {
		return data;
	}

	/**
	 * 设置页内的记录列表.
	 */
	public void setData(final List<T> result) {
		this.data = result;
	}

	/**
	 * 获得总记录数, 默认值为-1.
	 */
	public long getTotalCount() {
		return totalCount;
	}

	/**
	 * 设置总记录数.
	 */
	public void setTotalCount(final long totalCount) {
		this.totalCount = totalCount;
	}

	/**
	 * 根据pageSize与totalCount计算总页数, 默认值为-1.
	 */
	public long getTotalPages() {
		if (totalCount < 0) {
			return -1;
		}
		long count = totalCount / pageSize;
		if (totalCount % pageSize > 0) {
			count++;
		}
		return count;
	}

	/**
	 * 是否还有下一页.
	 *
	 * @return boolean
	 */
	public boolean isHasNext() {
		return (pageStart + 1 <= getTotalPages());
	}

	/**
	 * 取得下页的页号, 序号从1开始. 当前页为尾页时仍返回尾页序号.
	 */
	public int getNextPage() {
		if (isHasNext()) {
			return pageStart + 1;
		} else {
			return pageStart;
		}
	}

	/**
	 * 是否还有上一页.
	 */
	public boolean isHasPre() {
		return (pageStart - 1 >= 1);
	}

	/**
	 * 取得上页的页号, 序号从1开始. 当前页为首页时返回首页序号.
	 */
	public int getPrePage() {
		if (isHasPre()) {
			return pageStart - 1;
		} else {
			return pageStart;
		}
	}

}
