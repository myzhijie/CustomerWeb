/**  
 * @Title: MybatisDao.java
 * @Package rml.dao
 * @date 2015年10月10日 下午1:17:28
 * @version V1.0  
 */
package com.jingsky.customer.util.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 该类的功能用途 项目名称: [yfx]<br/>
 * 类名称: [MybatisDao]<br/>
 * 创建人: [GaoNan]<br/>
 * 创建时间: [2015年10月10日 下午1:17:28]<br/>
 * 修改人: [GaoNan]<br/>
 * 修改时间: [2015年10月10日 下午1:17:28]<br/>
 * 修改备注: [说明本次修改内容]<br/>
 * 版本: [v1.0]<br/>
 */
public interface MybatisDao<T  extends BaseEntity<PK, T>, PK extends Serializable> {

	T get(final PK id);

	int insert(final T entity);

	int insertBatch(final List<T> list);

	int update(final Map<String, Object> params);

	int remove(final Map<String, Object> params);

	List<T> find(final Map<String, Object> params);

	long count(final Map<String, Object> params);
	
	long countByEntity(final T entity);
	
	int updateByEntity(final T entity);
	
	int removeByEntity(final T entity);
	
	List<T> findByEntity(final T entity);

}
