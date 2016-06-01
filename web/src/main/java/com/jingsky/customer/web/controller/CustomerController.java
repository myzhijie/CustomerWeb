package com.jingsky.customer.web.controller;

import com.jingsky.customer.entity.Customer;
import com.jingsky.customer.iservice.ICustomerService;
import com.jingsky.customer.util.util.ResultMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
public class CustomerController {

	private static final Logger logger=Logger.getLogger(CustomerController.class);

	@Autowired
	private ICustomerService customerService;

	/**
	 * update
	 * @return
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public Object update(@RequestBody Customer customer, HttpServletRequest request) {
		//read name from session
		String name=request.getSession().getAttribute("name").toString();
		Customer customerOld=new Customer();
		customerOld.setName(name);
		//find
		customerOld=customerService.findOne(customerOld);
		customerOld.setAge(customer.getAge());
		//update,return 0 if age not change
		customerService.updateByEntity(customerOld);

		return new ResultMessage();
	}

	/**
	 * me
	 * @return
	 */
	@RequestMapping(value = "/me")
	@ResponseBody
	public Object me(HttpServletRequest request) {
		//read name from session
		String name=request.getSession().getAttribute("name").toString();
		Customer customer=new Customer();
		customer.setName(name);
		//find
		customer=customerService.findOne(customer);

		return customer;
	}

	/**
	 * login
	 * @return
	 */
	@RequestMapping(value = "/login")
	@ResponseBody
	public Object login(@RequestBody Customer customer, HttpServletRequest request) {
		//return
		ResultMessage resultMessage=new ResultMessage();
		//find
		customer=customerService.findOne(customer);
		if(customer!=null){
			//save to session
			request.getSession().setAttribute("name",customer.getName());
		}else{
			resultMessage.put("status","failure");
			resultMessage.put("msg","name or password error !");
		}
		return resultMessage;
	}

	/**
	 * register
	 * @return
	 */
	@RequestMapping(value = "/register")
	@ResponseBody
	public Object register(@RequestBody Customer customer) {
		//return
		ResultMessage resultMessage=new ResultMessage();

		//check name is exist
		Customer customerCheck=new Customer();
		customerCheck.setName(customer.getName());
		customerCheck=customerService.findOne(customerCheck);
		if(customerCheck!=null){
			resultMessage.put("status","failure");
			resultMessage.put("msg","name:"+customer.getName()+" has exist!");
			return resultMessage;
		}

		int result=customerService.insert(customer);

		if(result!=1){
			resultMessage.put("status","failure");
		}
		return resultMessage;
	}

}