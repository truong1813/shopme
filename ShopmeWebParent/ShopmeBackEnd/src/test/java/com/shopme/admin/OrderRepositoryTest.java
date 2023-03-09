package com.shopme.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.order.OrderRespository;
import com.shopme.common.entity.order.Order;
import com.shopme.common.entity.order.OrderStatus;
import com.shopme.common.entity.order.OrderTrack;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class OrderRepositoryTest {
	@Autowired private OrderRespository orderRespository;
	
	@Test
	public void testOrderTracks() {
		Order order = orderRespository.findById(13).get();
		OrderTrack orderTrack = new OrderTrack();
		
		
		orderTrack.setOrder(order);
		orderTrack.setUpdateTime(new Date());
		orderTrack.setStatus(OrderStatus.NEW);
		orderTrack.setNotes(OrderStatus.NEW.defaultDescription());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		
		orderTracks.add(orderTrack);
		
		Order updateOrder = orderRespository.save(order);
		
		assertThat(updateOrder.getId()).isNotNull();
		
	}
}
