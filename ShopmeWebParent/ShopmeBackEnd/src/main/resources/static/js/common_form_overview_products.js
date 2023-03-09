var fieldProductCost
var fieldShippingCost
var fieldTax
var fieldSubTotal
var fieldTotal

decimalSeparator = decimalPointType == "COMMA" ? "," : ".";
thousandsSeparator = thousandsPointType == "COMMA" ? "," : ".";

	$(document).ready(function(){
	 fieldProductCost = $("#productCost");
	 fieldShippingCost = $("#shippingCost");
	 fieldTax	= $("#tax");
	 fieldSubTotal = $("#subTotal");
	 fieldTotal = $("#total");
	 
	 formatOrderAmounts();
	 formatProductAmounts();
	 
	 $("#productList").on("change", ".quantity-input", function(e){
		  updateSubtotalWhenQuantityChange($(this));
		  updateOrderAmounts();
		  
	 });
	 $("#productList").on("change", ".price-input", function(e){
		 updateSubtotalWhenPriceChange($(this));
		 updateOrderAmounts();
	});
	
	$("#productList").on("change", ".cost-input", function(e){
		updateOrderAmounts();
	});
	
	$("#productList").on("change", ".ship-input", function(e){
		updateOrderAmounts();
	});
});

	function updateOrderAmounts(){
		total = 0.0;
		$(".cost-input").each(function(){
			costInputField = $(this);
			rowNumber = costInputField.attr("rowNumber");
			quantityValue = $("#quantity" + rowNumber).val();
			
			productCost = clearCurrencyFormat(costInputField.val());
			total += parseFloat(productCost) * parseInt(clearCurrencyFormat(quantityValue));
			
		});
		
		setValueForField("productCost", total);
		orderSubtotal = 0.0;
		
		$(".subtotal-output").each(function(){
			subtotalField = $(this);
				
			productSubtotal = clearCurrencyFormat(subtotalField.val());
			orderSubtotal +=parseFloat(productSubtotal);
			
		});
		
		setValueForField("subTotal", orderSubtotal);
		shippingCost=0.0;
		
		$(".ship-input").each(function(){
			shippingField = $(this);
			productShip = clearCurrencyFormat(shippingField.val());
			shippingCost +=parseFloat(productShip);
			
		});
		
		setValueForField("shippingCost", shippingCost);
		tax = parseFloat(clearCurrencyFormat($("#tax").val()));
		orderTotal = orderSubtotal  + shippingCost + tax;
			
		setValueForField("total", orderTotal);
	}
	
	function updateSubtotalWhenPriceChange(input){
		valueChange = parseFloat(clearCurrencyFormat(input.val()));
		rowNumber = input.attr("rowNumber");
		quantityField= $("#quantity" + rowNumber);
		
		
		valueQuantity= parseFloat(clearCurrencyFormat(quantityField.val()));
		subtotal = valueQuantity*valueChange;
			
		setValueForField("subtotal" + rowNumber,subtotal);
		
	}
	
	function setValueForField(fieldId,value){
		$("#" + fieldId).val(formatCurrency(value));
	}
	function updateSubtotalWhenQuantityChange(input){
			
			quantityValue = input.val();
			rowNumber = input.attr("rowNumber");
			priceField = $("#price" + rowNumber);
		
			priceValue = parseFloat(clearCurrencyFormat(priceField.val()));
			subtotal = parseFloat(quantityValue)*priceValue;
			
			setValueForField("subtotal" + rowNumber,subtotal);
		
	}
	
	function formatProductAmounts(){
		$(".cost-input").each(function(e){
			formatNumberForField($(this));
		});
		$(".price-input").each(function(e){
			formatNumberForField($(this));
		});
		$(".ship-input").each(function(e){
			formatNumberForField($(this));
		});
		$(".subtotal-output").each(function(e){
			formatNumberForField($(this));
		});
	};
	
	function formatOrderAmounts(){
		formatNumberForField(fieldProductCost);
		formatNumberForField(fieldShippingCost);
		formatNumberForField(fieldTax);
		formatNumberForField(fieldSubTotal);
		formatNumberForField(fieldTotal);
	};
	function formatNumberForField(fieldRef){
		fieldRef.val(formatCurrency(fieldRef.val()));
	}
	function formatCurrency(amount){
		return $.number(amount,decimalDigits,decimalSeparator,thousandsSeparator);
	}
		
	function clearCurrencyFormat(numberString){
		result = numberString.replaceAll(thousandsSeparator,"");
		return result.replaceAll(decimalSeparator,".");
	}
	
	function processFormBeforeSubmit(){
		setCountryName();
		fieldShippingCost.val(clearCurrencyFormat(fieldShippingCost.val()));
		fieldProductCost.val(clearCurrencyFormat(fieldProductCost.val()));
		fieldTax.val(clearCurrencyFormat(fieldTax.val()));
		fieldSubTotal.val(clearCurrencyFormat(fieldSubTotal.val()));
		fieldTotal.val(clearCurrencyFormat(fieldTotal.val()));
		
		$(".cost-input").each(function(e){
			$(this).val(clearCurrencyFormat($(this).val()));
		});
		
		$(".price-input").each(function(e){
			$(this).val(clearCurrencyFormat($(this).val()));
		});
		
		$(".subtotal-output").each(function(e){
			$(this).val(clearCurrencyFormat($(this).val()));
		});
		$(".ship-input").each(function(e){
			$(this).val(clearCurrencyFormat($(this).val()));
		});
		
	}
	
	function setCountryName(){
		selectedCountry = $("#country option:selected");
		countryName = selectedCountry.text();
		$("#countryName").val(countryName);
	}
