/**  
 * @Title: BaseEntity.java
 * @Package com.core.entity
 * @date 2015年10月10日 下午2:14:46
 * @version V1.0  
 */
package com.jingsky.customer.util.base;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * 该类的功能用途 项目名称: [yfx]<br/>
 * 类名称: [BaseEntity]<br/>
 * 创建人: [GaoNan]<br/>
 * 创建时间: [2015年10月10日 下午2:14:46]<br/>
 * 修改人: [GaoNan]<br/>
 * 修改时间: [2015年10月10日 下午2:14:46]<br/>
 * 修改备注: [说明本次修改内容]<br/>
 * 版本: [v1.0]<br/>
 */
public abstract class BaseEntity<PK, T> extends ObjectSupport implements Serializable {

    private static final long serialVersionUID = 1L;

    //protected PK id;
    @JsonIgnore
    protected boolean sqldecode=true;
    @JsonIgnore
    protected Integer pageIndex;
    @JsonIgnore
    protected Integer pageSize = 10;
    @JsonIgnore
    protected Integer limitStart;
    @JsonIgnore
    protected Integer limitEnd;
    @JsonIgnore
    protected String orderBy;
    @JsonIgnore
    protected Page<T> page;

    public Page<T> getPage() {
        return page;
    }

    public void setPage(Page<T> page) {
        this.page = page;
    }
    
    public boolean isSqldecode() {
		return sqldecode;
	}

	public void setSqldecode(boolean sqldecode) {
		this.sqldecode = sqldecode;
	}

	public Integer getLimitStart() {
        if (pageIndex != null && pageIndex >=1 && pageSize != null && pageSize > 0) {
            limitStart = (pageIndex-1) * pageSize;
        }
        return limitStart;
    }

    public void setLimitStart(Integer limitStart) {
        this.limitStart = limitStart;
    }

    public Integer getLimitEnd() {
        if (pageIndex != null && pageIndex >= 0 && pageSize != null && pageSize > 0) {
            limitEnd = pageSize;
        }
        return limitEnd;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setLimitEnd(Integer limitEnd) {
        this.limitEnd = limitEnd;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

//    public PK getId() {
//        return id;
//    }
//
//    public void setId(PK id) {
//        this.id = id;
//    }

}
