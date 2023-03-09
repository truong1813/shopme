	var country;
	var listStates;
	
		$(document).ready(function(){
			country = $("#country");
			listStates = $("#listStates");
			$("#country").on("change",function(){
			loadStates4Country();
							
			});
		});
		
		function loadStates4Country(){
			
			countryId = $("#country option:selected").val();
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