package com.jingsky.customer.web;

import com.jingsky.customer.iservice.ICustomerService;
import com.jingsky.customer.util.base.BaseController;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "customer")
public class CustomerController extends BaseController {

	private static final Logger logger=Logger.getLogger(CustomerController.class);
	
	@Autowired
	private ICustomerService customerService;

}