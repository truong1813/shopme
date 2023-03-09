package com.shopme.admin.order;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;
@Service
@Transactional
public class OrderService {
	public static final int ORDER_BY_PAGE = 10;
	@Autowired private OrderRespository repo;
	@Autowired private CountryRepository countryRepository;
	public Page<Order> listByPage(Integer pageNum, String sortDir, String sortField,String keyword){
		Sort sort = null;
		if("destination".equals(sortField)) {
			sort= Sort.by("country").and(Sort.by("state")).and(Sort.by("city"));
		}else {
			sort= Sort.by(sortField);
		}
		
		
		sort = sortDir.equals("asc")? sort.ascending():sort.descending();
		Pageable pageable = PageRequest.of(pageNum-1,ORDER_BY_PAGE, sort);
		
		if(keyword !=null) {
			return repo.findAll(keyword, pageable);
			
		}else {
			return repo.findAll(pageable);
		}
		
	}
	
	public Order get(Integer id) throws orderNotFoundException {
		try {
			return repo.findById(id).get();
		} catch (NoSuchElementException e) {
			throw new orderNotFoundException("Could not find any order with id: " +id);
		}
	}
	
	public void delete(Integer id) throws orderNotFoundException {
		Long count = repo.countById(id);
		if(count == null || count == 0) {
			throw new orderNotFoundException("Could not find any order with id: " + id);
		}
		repo.deleteById(id);
	}
	
	public List<Country> listCountries(){
		return countryRepository.findAllByOrderByNameAsc();
	}

	public void save(Order orderInForm) {
		Order orderInDB = repo.findById(orderInForm.getId()).get();
		orderInForm.setOrderTime(orderInDB.getOrderTime());
		orderInForm.setCustomer(orderInDB.getCustomer());
		
		repo.save(orderInForm);
	}
	
	public void updateStatus(Integer orderId,String status) {
		Order orderInDB = repo.findById(orderId).get();
		OrderStatus statusUpdate = OrderStatus.valueOf(status);
		
		if(!orderInDB.hasStatus(statusUpdate)) {
			List<OrderTrack> orderTracks = orderInDB.getOrderTracks();
			
			OrderTrack orderTrack = new OrderTrack();
			orderTrack.setOrder(orderInDB);
			orderTrack.setNotes(statusUpdate.defaultDescription());
			orderTrack.setStatus(statusUpdate);
			orderTrack.setUpdateTime(new Date());
			
			orderTracks.add(orderTrack);
			
			orderInDB.setStatus(statusUpdate);
			repo.save(orderInDB);
			
		}
		
	}
}
