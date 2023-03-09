var extraImagesCount = 0;
dropdownBrand = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescription").richText();

	dropdownBrand.change(function() {
		dropdownCategories.empty();
		getCategories();
	});
	getCategoriesForNewForm();
});

function getCategoriesForNewForm(){
	catIdField = $("#categoryId");
	editMode = false;
	if(catIdField.length){
		editMode = true;
	}
	if(!editMode){
		getCategories();
	}
}

function getCategories() {
	brandId = dropdownBrand.val();
	url = brandModulURL + "/" + brandId + "/categories";
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}
function checkUnique(form){
			
			productId= $("#id").val();
			productName = $("#name").val();
			productAlias = $("#alias").val();
			csrfValue= $("input[name='_csrf']").val();
			params= {id: productId, name: productName, alias: productAlias, _csrf: csrfValue};
			
			$.post(checkUniqueURL,params,function(response){
				if(response=="OK"){
					
					form.submit();
				}else if(response=="Duplicated Name"){
					showModalDialog("Warning","There is another product having the name " + productName);
				}else if(response=="Duplicated Alias"){
					showModalDialog("Warning","There is another product having the alias " + productAlias);
				}else {
					showModalDialog("Error","Unknown response from server");
					};
				
				}).fail(function(){
					showModalDialog("Error","Could not connect to the server");
				});
			
			return false;
		}
		
		function showModalDialog(title,message){
			$("#modalTitle").text(title);
			$("#modalBody").text(message);
			$("#modalDialog").modal();
		}
		


		