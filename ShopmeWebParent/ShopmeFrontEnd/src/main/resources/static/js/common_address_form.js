  var dropDownCountry;
	var listStates;
	var fieldState;
		$(document).ready(function(){
			
			dropDownCountry = $("#dropDownCountries");
			listStates = $("#listStates");
			fieldState = $("#state");
			$("#dropDownCountries").on("change",function(){
				loadStatesForCountry();
				fieldState.val("").focus();
			});
		});
		
		
		function loadStatesForCountry(){
			countryId = $("#dropDownCountries option:selected").val();
			url= contextPath + "states/list_by_country/" + countryId;
			$.get(url,function(responseJson){
				listStates.empty();
				$.each(responseJson,function(index,state){
					$("<option>").val(state.name).text(state.name).appendTo(listStates);
				});
			}).fail(function(){
				alert('Failed to connect to the server!')
			});
					
		}
		
	function showModalDialog(title,message){
		$("#modalTitle").text(title);
		$("#modalBody").text(message);
		$("#modalDialog").modal();
	}