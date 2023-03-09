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
	getCategories();

	$("input[name='extraImage']").each(function(index) {
		
		extraImagesCount++;
		
		$(this).change(function() {
			
		if(!checkFileSize(this)){
				return;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        
		}
			showExtraImageThumbnail(this, index);
		});
	});
});

function getCategories() {
	brandId = dropdownBrand.val();
	url = brandModulURL + "/" + brandId + "/categories";
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});
}


function checkFileSize(fileInput) {

	fileSize = fileInput.files[0].size;

	if (fileSize > MAX_FILE_SIZE) {
		fileInput.setCustomValidity("You must choose an image lass than 1MB!");
		fileInput.reportValidity();
		return false;
	} else {
		fileInput.setCustomValidity("");
		return true;
	}
}
function showExtraImageThumbnail(fileInput, index) {
	var file = fileInput.files[0];
	var reader = new FileReader();
	reader.onload = function(e) {
		$("#extraThumbnail" + index).attr("src", e.target.result);
	}
	reader.readAsDataURL(file);
	if(index >= extraImagesCount-1){
		addNextExtraImageSection(index + 1);
	}
	

}
function addNextExtraImageSection(index) {
	htmlExtraImage = `<div class="col border m-3 p-2" id="divExtraImage${index}">
				<div id="extraImageHeader${index}"><label>Extra Image #${index + 1}:</label></div>
				<div class= "m-2">
					<img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="image-fluid"
					src="${defaultImageThumbnailSrc}"/>
				</div>
				<div>
					<input type="file"  name="extraImage" 
					onchange="showExtraImageThumbnail(this,${index})"
					accept="image/png,image/jpeg"/>
				</div> 
				
			</div> `;
	htmlLinkRemove =
		`
			<a class="btn fas fa-times-circle fa-2x icon-dark float-right" 
			href="javascript:removeExtraImage(${index - 1})"
			title="Remove this image"></a>;
		
			`;
		
	$("#divProductImages").append(htmlExtraImage);
	$("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);
	extraImagesCount++	
}

function removeExtraImage(index) {
	$("#divExtraImage" + index).remove();
	
}
		