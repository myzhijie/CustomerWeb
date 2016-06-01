package com.jingsky.customer.entity;

import com.jingsky.customer.util.base.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;


public class Customer extends BaseEntity<Long,Customer> {

	private static final long serialVersionUID = 1L;
    
	/**
	 * 
	 */
    private String password;
	/**
	 * 
	 */
    private Integer age;

    private String name;
    
		    
    public Customer(){}
    
    
//    public Customer(Long id){
//    	this.id=id;
//    }
    
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getAge() {
        return this.age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}