package com.shopme.admin;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	@Autowired private UserRepository repo;
	@Autowired private EntityManager entityManager;
	@Test
	public void create() {
		User user = new User();
		user.setEmail("a@gmail.com");
		user.setFirstName("fdfd");
		user.setLastName("fdf");
		user.setPassword("111111111");
		user.addRole(entityManager.find(Role.class, 1));
		User save = repo.save(user);
		assertThat(save.getId()).isGreaterThan(0);
	}
}
