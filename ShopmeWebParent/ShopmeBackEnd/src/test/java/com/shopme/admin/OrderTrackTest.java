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
public class OrderTrackTest {
	@Autowired OrderRespository orderRespository;
	
	@Test
	public void createOrderTrack() {
		Order order = orderRespository.findById(28).get();
		
		
		OrderTrack track = new OrderTrack();
		track.setOrder(order);
		track.setStatus(order.getStatus());
		track.setNotes((order.getStatus().defaultDescription()));
		track.setUpdateTime(new Date());
		OrderTrack track2 = new OrderTrack();
		
		track2.setOrder(order);
		track2.setStatus(OrderStatus.DELIVERED);
		track2.setNotes((OrderStatus.DELIVERED.defaultDescription()));
		track2.setUpdateTime(new Date());
		
		List<OrderTrack> orderTracks = order.getOrderTracks();
		orderTracks.add(track);
		orderTracks.add(track2);
		
		Order updateOrder =  orderRespository.save(order);
		assertThat(updateOrder.getOrderTracks()).hasSizeGreaterThan(1);
	}
}
