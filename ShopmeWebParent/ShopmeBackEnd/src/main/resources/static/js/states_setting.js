var buttonLoadCountryForState;
var dropdownState
var dropdownCountryForState;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;

$(document).ready(function(){
	buttonLoadCountryForState= $("#buttonLoadCountriesForStates");
	dropdownCountryForState = $("#dropdownCountriesForStates");
	dropdownState=$("#dropdownStates");
	buttonAddState=$("#buttonAddState");
	buttonUpdateState=$("#buttonUpdateState");
	buttonDeleteState=$("#buttonDeleteState");
	labelStateName=$("#labelStateName");
	fieldStateName=$("#fieldStateName");
	
	buttonLoadCountryForState.click(function(){
		loadCountriesForState();
	});
	
	dropdownCountryForState.on("change" , function(){
		loadStatesForCountry();
	});
	
	dropdownState.on("change",function(){
		changeFormStateToSelectedState();
	});
	
	buttonAddState.click(function(){
		if(buttonAddState.val()=="Add"){
			addState();
		}else{
			changeFormStateToNew();
		}
		
	});
	
	
	buttonUpdateState.click(function(){
		updateState();
	});
	
	buttonDeleteState.click(function(){
		deleteState();
	});
});

function deleteState(){
	
	stateId= dropdownState.val();
	url = contextPath  + "states/delete/" + stateId;
	$.ajax({
		type:"DELETE",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);	
		}
		
	}).done(function(){
		$("#dropdownStates option[value='"+ stateId + "']").remove();
		showToastMessageForStates("The state has been deleted");
		changeFormStateToNew();
	}).fail(function(){
		showToastMessageForStates("ERROR: Could not connect to server or server encountered an error");
	
	});
}

function updateState(){
	if(!validateFormState()) return;
	url=contextPath + "states/save";
	stateName = fieldStateName.val();
	stateId= dropdownState.val();
	
	selectedCountry =$("#dropdownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	countryName= selectedCountry.text();
	jsonData ={id:stateId, name:stateName, country:{id:countryId, name: countryName}};
	$.ajax({
		type:"POST",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue)	
		},
		data:JSON.stringify(jsonData),
		contentType:"application/json"
		
	}).done(function(stateId){
		$("#dropdownStates option:selected").text(stateName);
		showToastMessageForStates("The state has been updated");
		changeFormStateToNew();
	}).fail(function(){
		showToastMessageForStates("ERROR: Could not connect to server or server encountered an error");
	
	});
}

function validateFormState(){
	formCountryForState = document.getElementById("formCountryForState");
	formState = document.getElementById("formState");
	if(!formCountryForState.checkValidity()||!formState.checkValidity()){
		formCountryForState.reportValidity();
		formState.reportValidity();
		return false;
		
		};
	return true;	
}

function addState(){
	
	if(!validateFormState()) return;
	
	url=contextPath + "states/save";
	stateName = fieldStateName.val();
	
	selectedCountry =$("#dropdownCountriesForStates option:selected");
	
	countryId = selectedCountry.val().split("-")[[0]];
	countryName= selectedCountry.text();
	jsonData ={name:stateName,country:{id:countryId, name: countryName}};
	$.ajax({
		type:"POST",
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue)	
		},
		data:JSON.stringify(jsonData),
		contentType:"application/json"
		
	}).done(function(stateId){
		selectNewlyAddedState(stateId,stateName)
		showToastMessageForStates("The state has been added");
		
	}).fail(function(){
		showToastMessageForStates("ERROR: Could not connect to server or server encountered an error");
	
	});
	
}

function loadStatesForCountry(){
	selectedCountry =$("#dropdownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	
	url =contextPath +"states/list_by_country/" + countryId;
	
	$.get(url, function(responseJson){
		dropdownState.empty();
		$.each(responseJson,function(index,state){
			$("<option>").val(state.id).text(state.name).appendTo(dropdownState);
		})
	}).done(function(){
		changeFormStateToNew()
		showToastMessageForStates("All States have been loaded");
		
	}).fail(function(){
		showToastMessageForStates("ERROR: Could not connect to server or server encountered an error");
	
	});
}

function selectNewlyAddedState(stateId,stateName){
	$("<option>").val(stateId).text(stateName).appendTo(dropdownState);
	$("#dropdownStates option[value='"+ stateId +"']").prop("selected",true);
	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState(){
	buttonAddState.prop("value","New")
	buttonUpdateState.prop("disabled", false);
	buttonDeleteState.prop("disabled", false);
	
	labelStateName.text("Selected State:");
	seclectedStateName = $("#dropdownStates option:selected").text();
	
	fieldStateName.val(seclectedStateName);
}


function changeFormStateToNew(){
	buttonAddState.val("Add");
	labelStateName.text("State/Province Name:")
	buttonUpdateState.prop("disabled", true);
	buttonDeleteState.prop("disabled", true);
	fieldStateName.val("").focus();
	
}

function loadCountriesForState(){
	url = contextPath +"countries/list";
	$.get(url,function(responseJson){
		dropdownCountryForState.empty();
		$.each(responseJson,function(index,country){
			optionValue = country.id + "-" + country.code
			$("<option>").val(optionValue).text(country.name).appendTo(dropdownCountryForState);
		})
	}).done(function(){
		buttonLoadCountryForState.val("Refesh Country List");
		showToastMessageForStates("All countries have been loaded");
		
	}).fail(function(){
		showToastMessageForStates("ERROR: Could not connect to server or server encountered an error");
	});
}

function showToastMessageForStates(message){
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}
