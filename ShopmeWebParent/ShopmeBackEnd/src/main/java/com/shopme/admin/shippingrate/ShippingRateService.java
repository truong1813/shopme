package com.shopme.admin.shippingrate;

import java.util.List;
import java.util.NoSuchElementException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.product.ProductRepository;
import com.shopme.admin.setting.country.CountryRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.entity.product.Product;

@Service
@Transactional
public class ShippingRateService {
	public final int SHIPPINGRATE_BY_PAGE = 10;
	public final int DIM_DIVISOR = 5000;
	@Autowired private ShippingRateRespository shippingRepo;
	@Autowired private CountryRepository countryRepo;
	@Autowired private ProductRepository productRepository;
	
	public List<Country> listCounties(){
		return countryRepo.findAllByOrderByNameAsc();
	}
	public void listByPage(Integer pageNum,PagingAndSortingHelper helper){
		helper.listEntities(pageNum, SHIPPINGRATE_BY_PAGE, shippingRepo);
	}
	
	public void save(ShippingRate rateInForm) throws ShippingRateAlraedyExistsException {
		ShippingRate rateInDB = shippingRepo.findByCoutryAndState(rateInForm.getCountry().getId(), rateInForm.getState());
		boolean foundExistingRateInNewMode = rateInForm.getId()==null && rateInDB !=null;
		boolean foundExistingRateInEditMode = rateInForm.getId()!=null && rateInDB !=null;
		if(foundExistingRateInNewMode||foundExistingRateInEditMode) {
			throw new ShippingRateAlraedyExistsException("There's already a rate for the destination "
					+ rateInForm.getCountry().getName() +"," + rateInForm.getState());
		}
		shippingRepo.save(rateInForm);
	}
	public ShippingRate get(Integer id) throws ShippingRateNotFoundExcepion {
		try {
			return shippingRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			 throw new ShippingRateNotFoundExcepion("Could not found shipping rate with id: " + id);
		}
	}
	public void updateCODSupported(Integer id, boolean status) throws ShippingRateNotFoundExcepion {
		Long count = shippingRepo.countById(id);
		if(count==null || count==0) {
			 throw new ShippingRateNotFoundExcepion("Could not found shipping rate with id: " + id);
		}
		shippingRepo.updateCODSupported(id, status);
	}
	
	public void delete(Integer id) throws ShippingRateNotFoundExcepion {
		Long count = shippingRepo.countById(id);
		if(count==null || count==0) {
			 throw new ShippingRateNotFoundExcepion("Could not found shipping rate with id: " + id);
		}
		shippingRepo.deleteById(id);
	}
	
	public float calculateShippingCost(Integer productId, Integer countryId, String state) throws ShippingRateNotFoundExcepion {
		
		ShippingRate shippingRate = shippingRepo.findByCoutryAndState(countryId, state);
		if(shippingRate==null) {
			throw new  ShippingRateNotFoundExcepion("No shipping rate found for the given "
					+ "destination. You have to enter shipping cost manually.");
		}
		
		Product product  = productRepository.findById(productId).get();
		
		float dimWeight = (product.getLength()*product.getWidth()*product.getHeight())/DIM_DIVISOR;
		float finalWeight = product.getWeight()>dimWeight ? product.getWeight():dimWeight;
		
		return finalWeight*shippingRate.getRate();
	}
	
	
}
