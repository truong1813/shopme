var buttonLoad;
var dropdownCountry;
var buttonAddCountry;
var buttonUpdateCountry;
var buttonDeleteCountry;
var labelCountryName;
var fieldCountryName;
var fieldCountryCode;
$(document).ready(function(){
	buttonLoad= $("#buttonLoadCountries");
	dropdownCountry = $("#dropdownCountries");
	buttonAddCountry=$("#buttonAddCountry");
	buttonUpdateCountry= $("#buttonUpdateCountry");
	buttonDeleteCountry= $("#buttonDeleteCountry");
	labelCountryName= $("#labelCountryName");
	fieldCountryName= $("#fieldCountryName");
	fieldCountryCode= $("#fieldCountryCode");
	buttonLoad.on("click",function(){
		loadCountries();
	});
	
	dropdownCountry.on("change" , function(){
		changeFormStateSelectedCountry();
	});
	
	buttonAddCountry.click(function(){
		if(buttonAddCountry.val()=="Add"){
			addCountry();
		}else{
			changeFormStateToNewCountry();
		}
		
	});
	
	buttonUpdateCountry.click(function(){
		updateCountry();
	});
	
	buttonDeleteCountry.click(function(){
		deleteCountry();
	});
});

function deleteCountry(){
	optionValue = dropdownCountry.val();
	countryId = optionValue.split("-")[0];
	url =contextPath + "countries/delete/" + countryId;
	$.ajax({
		type:"DELETE",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
			}
		}).done(function(){
			$("#dropdownCountries option[value='"+ optionValue + "']").remove();
			changeFormStateToNewCountry();
			showToastMessage("The country have been deleted");
		}).fail(function(){
			showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function updateCountry(){
	
	if(!formValidity()) return;
	
	url = contextPath +"countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	countryId = dropdownCountry.val().split("-")[0];
	jsonData = {id:countryId, name:countryName, code:countryCode}
	
	$.ajax({
		type:"POST",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		},
		data:JSON.stringify(jsonData),
		contentType:'application/json'
	}).done(function(countryId){
		
		$("#dropdownCountries option:selected").val(countryId + "-" + countryCode);
		$("#dropdownCountries option:selected").text(countryName);
		showToastMessage("The country has been updated");
		
		changeFormStateToNewCountry();
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function formValidity(){
	formCountry=document.getElementById("formCountry");
	if(!formCountry.checkValidity()){
		formCountry.reportValidity();
		return false;
		
		};
	return true;	
}
function addCountry(){
	
	if(!formValidity()) return;
	
	url = contextPath +"countries/save";
	countryName = fieldCountryName.val();
	countryCode = fieldCountryCode.val();
	
	jsonData = {name:countryName, code:countryCode}
	
	$.ajax({
		type:"POST",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);
		},
		data:JSON.stringify(jsonData),
		contentType:'application/json'
	}).done(function(countryId){
		selectNewlyAddedCountry(countryId, countryName, countryCode);
		showToastMessage("The newly country has been added");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function selectNewlyAddedCountry(countryId, countryName, countryCode){
	optionValue = countryId + "-" + countryCode;
	$("<option>").val(optionValue).text(countryName).appendTo(dropdownCountry);
	$("#dropdownCountries option[value='" + optionValue + "']").prop("selected",true);
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
	
}
function changeFormStateToNewCountry(){
	buttonAddCountry.val("Add");
	buttonUpdateCountry.prop("disabled",true);
	buttonDeleteCountry.prop("disabled",true);
	
	fieldCountryCode.val("");
	fieldCountryName.val("").focus();
	
}


function changeFormStateSelectedCountry(){
	buttonAddCountry.prop("value","New");
	buttonUpdateCountry.prop("disabled",false);
	buttonDeleteCountry.prop("disabled",false);
	
	labelCountryName.text("Selected Country:")
	
	selectedCountryName= $("#dropdownCountries option:selected").text();
	fieldCountryName.val(selectedCountryName);
		
	countryCode= dropdownCountry.val().split("-")[1];
	fieldCountryCode.val(countryCode);
}

function loadCountries(){
	
	url = contextPath +"countries/list";
	$.get(url,function(responseJson){
		dropdownCountry.empty();
		$.each(responseJson,function(index,country){
			optionValue = country.id + "-" + country.code
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountries);
		})
	}).done(function(){
		buttonLoad.val("Refesh Country List");
		showToastMessage("All countries have been loaded");
		
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error");
	});
}

function showToastMessage(message){
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}
