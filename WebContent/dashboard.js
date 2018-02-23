function submitLoginForm(formSubmitEvent) {

  console.log("========== DASHBOARD ==========");

  formSubmitEvent.preventDefault();

  postRequest("LoginServlet", $("#login_form"), loginSuccess);
}

function submitAddStar(formSubmitEvent) {
	console.log ("======== ADD STAR =========");
	
	formSubmitEvent.preventDefault();
	
	postRequest("AddStarServlet", $("#add-star-form"), addStarSuccess);
}

//function submitMetadata(formSubmitEvent) {
//	console.log ("======== SHOW METADATA =========");
//	
//	formSubmitEvent.preventDefault();
//	
//	postRequest("MetadataServlet", $("#login_form"), metadataSuccess);
//}

function postRequest (servlet, form, successFunction) {
	console.log("POSTING: " + servlet);
	jQuery.post(
		servlet,
		form.serialize(),
		(resultDataString) => successFunction(resultDataString));
}

function loginSuccess(data) {
	result = JSON.parse(data);
	console.log("data: " + data);

	console.log("status: " + result["status"]);
	if(result["status"] == "success")
	{
		console.log("login success");
	    setupMainPage();
	    
	}
	  
	else {
	     alert(result["message"]);
	}

}

function setupMainPage() {
	$("body").empty();
	
    title_div = $("<div>", {class: "title-div"});
    welcome = $("<h1>", {class: "welcome", text: "Movieverse Employee Dashboard"});
    br = $("<br>");
    welcome_desc = $("<h2>", {text: "Choose functionality: "});

    title_div.append(welcome).append(br).append(welcome_desc);

    button_div = $("<div>", {class: "btn-div"});
    add_movie = $("<button>", {type: "button", class: "btn btn-primary", id: "add-movie", text: "Add Movie", onclick: ""});
    metadata = $("<button>", {type: "button", class: "btn btn-secondary", id: "metadata", text: "Metadata"});
    add_star = $("<button>", {type:"button", class: "btn btn-success", id: "add-star", text: "Add Star"});

    button_div.append(add_movie).append(metadata).append(add_star);
    $("body").append(title_div).append(button_div);
    
    setupAddStar();
    setupMetadata();
}

function setupAddStar() {

		$("#add-star").click(function() {

	  	  console.log("Add New Star");

	  	  $("body").empty();

	  	  // ADD FORM INPUTS TO PAGE
	  	  add_star_title = $("<h1>", {class:"search-title", text: "Add a Star"});
	  	  add_star_div = $("<form>", {class:"search-div", id:"add-star-form", action: "#", method:"get"});
	  	  
	  	  
	  	  // STAR NAME
	  	  title_div = $("<div>", {class:"inline"});
	  	  title_div.append($("<h3>", {class:"h3", text: "Star Name: "}));
	  	  input_div = $("<div>", {class:"bar-div inline"});
	  	  input_div.append($("<input>", {type:"text", id:"star-name", class:"form-control inline", name:"star-name"}));
	  	  
	  	  // STAR BIRTHYEAR
	  	  title_div_y = $("<div>", {class:"inline"});
	  	  title_div_y.append($("<h3>", {class:"h3", text: "Birth Year: "}));
	  	  input_div_y = $("<div>", {class:"bar-div inline"});
	  	  input_div_y.append($("<input>", {type:"text", id:"star-year", class:"form-control inline", name:"star-year"}));
	  	  add_star_div.append(title_div).append(input_div).append(title_div_y).append(input_div_y);
	  	  

	  	  as_button_div = $("<div>", {class:"search_button_div"});
	  	  as_button_div.append($("<input>", {class:"btn btn-primary", id:"add_stars_button", value:"Submit", type:"submit"}));
	  	  add_star_div.append(as_button_div);
	  	  $("body").append(add_star_title).append(add_star_div);
	  	  
	  	  $("#add-star-form").submit((event) => submitAddStar(event));

	  	});
	
}

function setupMetadata() {
	$("#metadata").click(function() {
		postRequest("MetadataServlet", $("#login_form"), metadataSuccess);
	});
}
function addStarSuccess(data) {
	console.log("data: " + data);
	
	var result = JSON.parse(data);
	console.log(result);
	console.log("show me change");
	alert("Adding star into database: " + result["status"] + ". " + result["message"]);
}

function metadataSuccess(data) {
	console.log("data: " + data);
	var result = JSON.parse(data);
	$("body").empty().append($("<h1>", {text: "Movieverse Metadata", style: "font-size: 4vh; "}))
	for (table in result) {
		table_div = $("<div>", {class: "table-div"});
		table_title = $("<h2>", {text: table, class:"table-title"});
		table_table = $("<table>", {class:"table table-striped table-bordered", style:"background: #FFFFFF", id: "table-table"});
		tableHead = $("<thead>");
		headRow = $("<tr>").append($("<th>", {text: "Field"})).append($("<th>", {text: "Type"})).append($("<th>", {text: "Null"})).append($("<th>", {text: "Key"}));
		tableBody = $("<tbody>");
		
		for (field in result[table]) {
			fieldRow = $("<tr>");
			fieldName = $("<td>", {text: field});
			fieldType = $("<td>", {text: result[table][field]["type"]});
			fieldNull = $("<td>", {text: result[table][field]["Null"]});
			fieldPrimary = $("<td>", {text: result[table][field]["Primary"]});
			tableBody.append(fieldRow.append(fieldName).append(fieldType).append(fieldNull).append(fieldPrimary));
		}

		
		table_table.append(tableHead.append(headRow)).append(tableBody);
		table_div.append(table_table)
		$("body").append(table_title).append(table_div);
	}
	
	
}

$("#login_form").submit((event) => submitLoginForm(event));
