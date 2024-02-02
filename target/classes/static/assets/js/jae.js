// clean up any single quote escape charater in Json
function cleanUpJson(obj){
	const jsonString = JSON.stringify(obj, (key, value) => {
  		// If the value is a string, remove escape characters from it
  		if (typeof value === 'string') {
    		return value.replace(/'/g, '&#39;');
  		}
  			return value;
	});
	return jsonString;
}

// StringUtils.isNotBlank()
function isNotBlank(value) {
	return typeof value === 'string' && value.trim().length > 0;
}
 
// String escape characters : single/double quotes, linefeed
function encodeDecodeString(str) {
	// Encoding
	let encodedStr = str
	  .replace(/\\/g, '\\\\')    // Backslash
	  .replace(/'/g, "\\'")       // Single quote
	//   .replace(/"/g, '\\"')       // Double quote
	  .replace(/"/g, "\\'")       // Double quote to single quote
	  .replace(/\n/g, '\\n');     // Line feed
  
	// Decoding
	let decodedStr = encodedStr
	  .replace(/\\n/g, '\n')      // Line feed
	  .replace(/\\"/g, '"')       // Double quote
	  .replace(/\\'/g, "'")       // Single quote
	  .replace(/\\\\/g, '\\');    // Backslash
  
	return { encoded: encodedStr, decoded: decodedStr };
  }

// format date - ex> 15/07/2023
  function getFormattedToday() {
	var date = new Date();
	var day = date.getDate();
	var month = date.getMonth() + 1; // Months are zero-based
	var year = date.getFullYear();
  
	// Add leading zeros if necessary
	if (day < 10) {
	  day = '0' + day;
	}
	if (month < 10) {
	  month = '0' + month;
	}
  
	var formattedDate = day + '/' + month + '/' + year;
	return formattedDate;
  }

 // avoid null string 
  function defaultIfEmpty(input, defaultValue) {
	if (typeof input === 'string' && input !== null && input !== '') {
	  return input;
	} else {
	  return defaultValue; // Or any other value you want to return for non-valid strings
	}
  }
  
// date format for datepicker. it changes date format from yyyy-mm-dd to dd/mm/yyyy
	function formatDate(dateString) {
		if (dateString.includes('-')) {
			const parts = dateString.split('-');
			const formattedDate = parts.reverse().join('/');
			return formattedDate;
		} else {
			return dateString; // Return the original string if it doesn't contain '-'
		}
	}

// Function to get URL parameters by name
function getParameterByName(name) {
    var url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

// Clear Form data
function clearFormData(elementId) {
	document.getElementById(elementId).reset();
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//	List state
function listState(selectElementId) {
	$.ajax({
		url: '/code/state',
		type: 'GET',
		success: function (data) {
			// Update display info
			// $("#editId").val(teacher.id);
			$.each(data, function (index, state) {
				var option = "<option value='" + state.value + "'>" + state.name + "</option>";
				$(selectElementId).append(option);	
			});
		},
		error: function (xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}
  
//	List branch
function listBranch(selectElementId) {
	$.ajax({
		url: '/code/branch',
		type: 'GET',
		success: function (data) {
			$.each(data, function (index, state) {
				var option = "<option value='" + state.value + "'>" + state.name + "</option>";
				$(selectElementId).append(option);	
			});
		},
		error: function (xhr, status, error) {
			console.log('Error : ' + error);
		}
	});
}
