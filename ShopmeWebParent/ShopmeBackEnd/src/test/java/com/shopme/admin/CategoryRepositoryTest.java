package com.shopme.admin;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.category.CategoryRespository;
import com.shopme.common.entity.Category;

@DataJpaTest
@AutoConfigureTestDatabase(replace=Replace.NONE)
@Rollback(false)

public class CategoryRepositoryTest {
	
	
		@Autowired private CategoryRespository repo;
		@Autowired private EntityManager entityManager;
		@Test
		public void listChildrenTest() {
			Category cate= repo.findById(1).get();
			
			Set<Category> children = cate.getChildren();
			for (Category sub : children ) {
				
				System.out.println("--" + sub.getName());
				listChildren(sub, 1);
			}
			
		}
		private void listChildren(Category parent, int subLevel) {
			 int newLevel = subLevel+1;
			String name="";
			 Set<Category> children = parent.getChildren();
			 for(Category subChildren : children) {
				 for (int i=0; i < newLevel;i++ ) {
					 name += "--";
				 }
				 System.out.println(name + subChildren.getName());
				 listChildren(subChildren,newLevel);
			 }
		}
		@Test
		public void createSub() {
			Category parent = new Category(2);
			Category sub = new Category("children",parent);
			Category save = repo.save(sub);
			assertThat(save.getId()).isGreaterThan(0);
		}
		@Test
		public void delete() {
			repo.deleteById(9);
			assertThat(repo.findById(9).get().getId()).isGreaterThan(0);
			
		}
		
		
	}
