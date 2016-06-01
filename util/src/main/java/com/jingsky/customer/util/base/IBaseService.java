package com.jingsky.customer.util.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * base service interface
 * Created by pg on 16/6/1.
 */
public interface IBaseService <T extends BaseEntity<PK , T>, PK extends Serializable>{

    public T get(final PK id);

    public int insert(final T entity) ;

    public int insertBatch(final List<T> list);

    public int update(final Map<String, Object> params);

    public int remove(final Map<String, Object> params);

    public List<T> find(final Map<String, Object> params);

    public long count(final Map<String, Object> params);

    public long countByEntity(final T entity);

    public int updateByEntity(final T entity);

    public int removeByEntity(final T entity);

    public List<T> findByEntity(final T entity);

    public Page<T> page(Map<String, Object> params) ;

    public T findOne(final T entity);

    public T findOne(Map<String, Object> params) ;
}
