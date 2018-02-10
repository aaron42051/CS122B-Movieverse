
// LOGIN FORM

function submitLoginForm(formSubmitEvent) {

  console.log("submit form");

  formSubmitEvent.preventDefault();


  postRequest("LoginServlet", $("#login_form"), loginSuccess);
}

function submitBrowseForm(formSubmitEvent) {
	console.log("browse movies");
	
	formSubmitEvent.preventDefault();
	
	postRequest("BrowseServlet", $("#login_form"), browseSuccess);
}

function postRequest (servlet, form, successFunction) {
	console.log("POSTING: " + servlet);
	jQuery.post(
		servlet,
		// serialize the login form to the data sent by POST request
		// jQuery("#login_form").serialize(),
		form.serialize(),
		(resultDataString) => successFunction(resultDataString));
}

function getRequest(servlet, form, successFunction) {
	jQuery.get(servlet, form.serialize(), (resultDataString) => successFunction(resultDataString));
}

function loginSuccess(data) {
   
   var result = JSON.parse(data);
   console.log("data: " + data);
   console.log("status: " + result["status"]);
   if(result["status"] == "success")
   {
    console.log("login success");

    $("body").empty();

    title_div = $("<div>", {class: "title-div"});
    welcome = $("<h1>", {class: "welcome", text: "Welcome to Movieverse!"});
    br = $("<br>");
    welcome_desc = $("<h2>", {text: "Choose a way to get started: "});

    title_div.append(welcome).append(br).append(welcome_desc);

    button_div = $("<div>", {class: "btn-div"});
    browse = $("<button>", {type: "button", class: "btn btn-primary", id: "browse-movies", text: "Browse"});
    search = $("<button>", {type: "button", class: "btn btn-secondary", id: "search-movies", text: "Search"});


    button_div.append(browse).append(search);
    $("body").append(title_div).append(button_div);
    
    //alert("Login success!");
   }
  
   else {
     alert(result["message"]);
   }
    
    setupSearchPage();
    setupBrowsePage();

}

$("#login_form").submit((event) => submitLoginForm(event));


// SEARCH PAGE

formTitles = {"Movie Title: " : "movie-title", "Movie Year: " : "movie-year",
              "Director:         ": "movie-director",
              "Star's Name: " : "movie-stars" };

function submitSearch(formSubmitEvent) {
	
	console.log("SUBMIT SEARCH");

	formSubmitEvent.preventDefault();
	
	postRequest("SearchServlet", $("#search-form"), searchSuccess);

}

function searchSuccess(data) {
	console.log()
	console.log("Returned with movie search results: ");
	console.log("data: " + data);
	var result = JSON.parse(data);
	
	$("body").empty();
	resultsTitle = $("<h1>", {class:"main-title", text: "Search Results:"});
	$("body").append(resultsTitle);
	
	if(data == "{}") {
		$("body").append($("<h3>", {text: "No results found!"}));
	}
	for (id in result) {
		movieDiv = $("<div>", {class: "movie-div"});
		movieTitle = $("<h2>", {class: "movie-title", text: result[id]["title"]});
		movieDirector = $("<h3>", {class: "movie-info", text: result[id]["director"]});
		movieYear = $("<h3>", {class: "movie-info", text: result[id]["year"]});
		
		genreList = result[id]["genres"];
		var buildGenre = "Genres: ";

		length = Object.keys(genreList).length;
		index = 1;
		for (key in genreList) {
			buildGenre = genreList[key];
			if(index != length) {
				buildGenre += ", ";
			}
			index++;
		}
		movieGenres = $("<h3>", {class: "movie-genres", text: buildGenre});

		
		
		
		
		starList = result[id]["stars"];
		var buildStars = "Starring: ";
		movieStars = $("<h4>", {class: "movie-stars", text: buildStars});
		
		length = Object.keys(starList).length;
		index = 1;
		for (star in starList) {
			var span = starList[star];
			if(index != length) {
				span2 = span + ", ";
			}
			index++;
			movieStars.append( $("<span>", {style:"color: blue", text:span2, onclick: "star_page(\"" + span + "\")" }) );
		}
		movieID = $("<h4>", {text: "id: " + id});
		
		movieDiv.append(movieTitle).append(movieDirector).append(movieYear).append(movieGenres).append(movieStars).append(movieID);
		$("body").append(movieDiv);
		
		
	}


}

function setupSearchPage() {
	$("#search-movies").click(function() {

  	  console.log("choose Search option");

  	  $("body").empty();

  	  // ADD FORM INPUTS TO PAGE
  	  search_title = $("<h1>", {class:"search-title", text: "Search the Movieverse"});
  	  search_div = $("<form>", {class:"search-div", id:"search-form", action: "#", method:"get"});
  	  for (title in formTitles) {
  	    title_div = $("<div>", {class:"inline"});
  	    title_div.append($("<h3>", {class:"h3", text: title}));

  	    input_div = $("<div>", {class:"bar-div inline"});
  	    input_div.append($("<input>", {type:"text", id:formTitles[title], class:"form-control inline", name:formTitles[title]}));

  	    search_div.append(title_div).append(input_div);
  	  }

  	  s_button_div = $("<div>", {class:"search_button_div"});
  	  s_button_div.append($("<input>", {class:"btn btn-primary", id:"search_button", value:"Submit", type:"submit"}));
  	  search_div.append(s_button_div);
  	  $("body").append(search_title).append(search_div);
  	  
  	  $("#search-form").submit((event) => submitSearch(event));

  	});
}

function setupBrowsePage() {
	$("#browse-movies").click(function() {
		console.log("BROWSE");
		jQuery.post("BrowseServlet", browseSuccess);
	});
}

function browseSuccess(data) {
	console.log("data: " + data);
	var result = JSON.parse(data);
	console.log("result dictionary: " + result);
	$("body").empty();
		
	browse_title = $("<h1>", {class:"search-title", text: "Browse the Movieverse"});
	genre_div = $("<div>", {class:"genre_div"});
	for (key in result) {
		genre_div.append($("<button>", {class: "btn btn-primary genre-btn", text: result[key]}));
	}
	
	$("body").append(browse_title).append(genre_div);
	
	  	 
	alphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
	title_div = $("<div>", {class:"genre_div"});
	for (letter in alphabet) {
		title_div.append($("<button>", {class: "btn btn-primary genre-btn", text: letter}));
	}
	
}


function star_page(star) {
	$("body").empty();
	
}



