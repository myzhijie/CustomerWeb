package com.jingsky.customer.service;

import com.jingsky.customer.dao.CustomerDao;
import com.jingsky.customer.entity.Customer;
import com.jingsky.customer.iservice.ICustomerService;
import com.jingsky.customer.util.base.BaseService;
import com.jingsky.customer.util.base.MybatisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("customerService")
public class CustomerService extends BaseService<Customer, Long> implements ICustomerService {
    
    @Autowired
	private CustomerDao customerDao;

	@Override
	public MybatisDao<Customer, Long> getEntityDao() {
		return customerDao;
	}
    
}