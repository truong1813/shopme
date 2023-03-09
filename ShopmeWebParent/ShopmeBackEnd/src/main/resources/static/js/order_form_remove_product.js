$(document).ready(function(){
	$("#productList").on("click", ".linkRemove", function(e){
		e.preventDefault();
		if(doesOrderHaveOnlyOneProduct()){
			showModalDialog("Warning", "Could not remove product.The order must have eat least one product.");	
		}else{
			removeproduct($(this));
			updateOrderAmounts();
		}
		
	});
})

function removeproduct(link){
	rowNumber = link.attr("rowNumber");
	
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
	$(".divCount").each(function(index,element){
		element.innerHTML = ""  + (index+1);
	});
}

function doesOrderHaveOnlyOneProduct(){
	productCount = $(".hiddenProductId").length;
	return productCount ==1;
}