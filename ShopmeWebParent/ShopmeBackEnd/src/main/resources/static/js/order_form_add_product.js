
var productDetailCount;
$(document).ready(function(){
	productDetailCount = $(".hiddenProductId").length;
	$("#products").on("click" , "#linkAddProduct",function(e){
		
		e.preventDefault();
		link = $(this);
		url = link.attr("href");
		$("#addProductModal").on("shown.bs.modal",function(){
			$(this).find("iframe").attr("src",url);
		});
		$("#addProductModal").modal();
	});
});

function addProduct(productId,productName){
	getShippingCost(productId);
}

function getProductInfo(productId,shippingCost){
	url = contextPath +"products/get/" + productId;
	$.get(url,function(productJson){
		productName = productJson.name;
		productCost = formatCurrency(productJson.cost);
		productPrice = formatCurrency(productJson.price);
		mainImagePath = contextPath.substring(0,contextPath.length-1) + productJson.imagePath; 
				
		htmlCode= generateProductCode(productId,productName,mainImagePath,productCost, productPrice, formatCurrency(shippingCost))	
		$("#productList").append(htmlCode);
		updateOrderAmounts();
	}).fail(function(err){
	showModalDialog("Warning", err.responseJSON.message);	
	});
}
function getShippingCost(productId){
	selectedCountry = $("#country option:selected");
	countryId = selectedCountry.val();
	state = $("#state").val();
	
	if(state.lenght==0){
		state = $("#city").val();
	}
	
	url = contextPath + "get_shipping_cost";
	params = {productId:productId,countryId:countryId, state:state};
	$.ajax({
		type:"POST",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		},
		data:params,
		
	}).done(function(shippingCost){
		getProductInfo(productId,shippingCost);
		
	}).fail(function(err){
		
		showModalDialog("Warning", err.responseJSON.message);
		shippingCost= 0.0;
		getProductInfo(productId,shippingCost);
	}).always(function(){
		$("#addProductModal").modal("hide");
	});
}

function isProductAlreadyAdded(productId){
	productExists = false;
	
	$(".hiddenProductId").each(function(e){
		aProductId = $(this).val();
		if(aProductId == productId){
			productExists = true;
			return;
		}
		
	});
	return productExists;
}

function generateProductCode(productId,productName,mainImagePath,productCost, productPrice, productShipCost){
	nextCount= productDetailCount + 1;
	productDetailCount++;
	quantityId = "quantity" + nextCount;
	rowId = "row" + nextCount;
	blankLineId = "blankLine" + nextCount;
	priceId = "price" + nextCount;
	subtotalId = "subtotal" + nextCount;
	htmlCode = `<div class="border rounded p-1" id="${rowId}">
			<input type="hidden" name="detailId" value ="0" />
			<input type="hidden" name="productId" value="${productId}" class="hiddenProductId" />
				<div class= "row">
					<div class="col-1">
						<div class="divCount">${nextCount}</div>
						<div><a class="fas fa-trash icon-dark linkRemove" href="" rowNumber="${nextCount}" ></a></div>
					</div>
					<div class="col-3">
						<img src="${mainImagePath}" class="img-fluid" />
					</div>
				</div>
				
				<div class="row m-2">
					<b>${productName}</b>
				</div>
				
				<div class="row m-2">
					<table>
						<tr>
							<td>Product Cost:</td>
							<td>
								<input type="text" required class="form-control m-1 cost-input"
								name="productDetailCost"
								rowNumber="${nextCount}"
								value="${productCost}" style="max-width: 140px" />
							</td>
						</tr>
						<tr>	
							<td>Quantity:</td>
							<td>
								<input type="number" min="1"  max="5" required class="form-control m-1 quantity-input"
								 name="quantity"
								 id="${quantityId}" 
								 rowNumber="${nextCount}" 
								 value="1" style="max-width: 140px" />
							</td>
						</tr>
						<tr>	
							<td>Unit Price:</td>
							<td>
								<input type="text" required  class="form-control m-1 price-input"
								name="productPrice"
								id="${priceId}"
								rowNumber="${nextCount}"
								value="${productPrice}" style="max-width: 140px" />
							</td>
						</tr>
						<tr>	
							<td>SubTotal:</td>
							<td>
								<input type="text" required readonly="readonly"  class="form-control m-1 subtotal-output"
								name="productSubtotal"
								id="${subtotalId}"
								rowNumber="${nextCount}"
								value="${productPrice}" style="max-width: 140px" />
							</td>
						</tr>
						
						<tr>	
							<td>Shipping Cost:</td>
							<td>
								<input type="text" required class="form-control m-1 ship-input"
								name="productShipCost"
								value="${productShipCost}" style="max-width: 140px" />
							</td>
						</tr>
					</table>
				</div>
			</div>
			<div id="${blankLineId}" class="row">&nbsp;</div>`;
			
			return htmlCode;
}

		
