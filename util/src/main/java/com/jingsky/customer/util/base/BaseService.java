/**  
 * @Title: BaseService.java
 * @Package rml.service
 * @date 2015年10月10日 下午1:16:32
 * @version V1.0  
 */
package com.jingsky.customer.util.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;


/**
 * 该类的功能用途 项目名称: [yfx]<br/>
 * 类名称: [BaseService]<br/>
 * 创建人: [GaoNan]<br/>
 * 创建时间: [2015年10月10日 下午1:16:32]<br/>
 * 修改人: [GaoNan]<br/>
 * 修改时间: [2015年10月10日 下午1:16:32]<br/>
 * 修改备注: [说明本次修改内容]<br/>
 * 版本: [v1.0]<br/>
 */
public abstract class BaseService<T extends BaseEntity<PK , T>, PK extends Serializable> {

	/**
	 * 子类需要实现该方法，提供注入的dao
	 */
	public abstract MybatisDao<T, PK> getEntityDao();

	public T get(final PK id) {
		return getEntityDao().get(id);
	}

	public int insert(final T entity) {
		return getEntityDao().insert(entity);
	}

	public int insertBatch(final List<T> list) {
		return getEntityDao().insertBatch(list);
	}

	public int update(final Map<String, Object> params) {
		return getEntityDao().update(params);
	}

	public int remove(final Map<String, Object> params) {
		return getEntityDao().remove(params);
	}

	public List<T> find(final Map<String, Object> params) {
		return getEntityDao().find(params);
	}

	public long count(final Map<String, Object> params) {
		return getEntityDao().count(params);
	}

	public long countByEntity(final T entity) {
		return getEntityDao().countByEntity(entity);
	}

	public int updateByEntity(final T entity) {
		return getEntityDao().updateByEntity(entity);
	}

	public int removeByEntity(final T entity) {
		return getEntityDao().removeByEntity(entity);
	}

	public List<T> findByEntity(final T entity) {
		return getEntityDao().findByEntity(entity);
	}

	public Page<T> page(Map<String, Object> params) {
		long count = getEntityDao().count(params);
		int limitStart = Objects.strToInt(params.get(MagicConstants.LIMIT_START).toString(), 0);
		int limitEnd = Objects.strToInt(params.get(MagicConstants.LIMIT_END).toString(), 0);
		Page<T> dPageTest = new Page<T>(limitStart, limitEnd);
		dPageTest.setData(getEntityDao().find(params));
		dPageTest.setTotalCount(count);
		return dPageTest;
	}

	public T findOne(final T entity) {
		entity.setLimitStart(0);
		entity.setLimitEnd(1);
		List<T> datas = getEntityDao().findByEntity(entity);
		if (datas != null && datas.size() > 0) {
			return datas.get(0);
		}
		return null;
	}
	
	public T findOne(Map<String, Object> params) {
		params.put(MagicConstants.LIMIT_START, 0);
		params.put(MagicConstants.LIMIT_END, 1);
		List<T> datas = getEntityDao().find(params);
		if (datas != null && datas.size() > 0) {
			return datas.get(0);
		}
		return null;
	}

}
