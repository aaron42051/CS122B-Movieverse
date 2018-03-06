page_limit = 10;
page_start = 0;
current_movies = "";
sort = "";
titleSort = 1;
yearSort = 1;
shopping_cart = {};

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

function submitMainSearch() {
	console.log("Searching for title: " + $("#autocomplete").val());
	jQuery.post("FullTextServlet", {"title": $("#autocomplete").val()}, searchSuccess);
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

function handleSelectSuggestion(suggestion) {
	console.log(suggestion);
	if (suggestion["data"].indexOf("tt") >= 0) {
//		movie_page(movie_title, movie_director, movie_year, movie_genres, movie_stars)
		movie_page(suggestion["value"], suggestion["director"], suggestion["year"], suggestion["genres"].toString(), suggestion["stars"].toString());
	}
	if (suggestion["data"].indexOf("nm") >= 0) {
		star_page(suggestion["value"]);
	}
}

function ajaxFunction(query, doneCallback) {
	var ajaxRequest;  
	console.log("initiating ajaxFunction, checking for 3 characters");
	try{
		ajaxRequest = new XMLHttpRequest();
	} catch (e){
		try{
			ajaxRequest = new ActiveXObject("Msxml2.XMLHTTP");
		} catch (e) {
			try{
				ajaxRequest = new ActiveXObject("Microsoft.XMLHTTP");
			} catch (e){
				alert("Browser Error");
				return false;
			}
		}
	}

		if($("#autocomplete").val().length > 2){
			console.log("3 confirmed, initiate autocomplete");

			jQuery.ajax({
				"method": "POST",
				"url": "FullTextServlet?title=" + $("#autocomplete").val() + "&" + "ajax=true",
				"success":function(data) {
					AutocompleteSuccess(data, query, doneCallback);
				},
				"error": function(errorData) {
					console.log("lookup ajax error");
					console.log(errorData);
				}
			});
	}
}

function setupMainPage() {
    $("body").empty().append($("<div>", {class:"background"}));

    title_div = $("<div>", {class: "title-div"});
    welcome = $("<h1>", {class: "welcome", text: "Welcome to Movieverse!"});
    br = $("<br>");
    welcome_desc = $("<h2>", {text: "Choose a way to get started: "});

    title_div.append(welcome).append(br).append(welcome_desc);

    button_div = $("<div>", {class: "btn-div"});
    browse = $("<button>", {type: "button", class: "btn btn-primary", id: "browse-movies", text: "Browse", onclick: "jQuery.post(\"BrowseServlet\", browseSuccess)"});
    search = $("<button>", {type: "button", class: "btn btn-secondary", id: "search-movies", text: "Search"});
    
    
    // auto complete search bar

    autocomplete_div = $("<div>", {class: "autocomplete-div"});
    
	search_title_div = $("<div>", {class:"inline"});
  	search_title_div.append($("<h3>", {class:"h3", text: "Search: "}));
  	
  	input_div = $("<div>", {class:"search-bar inline"});
  	input_div.append($("<input>", {type:"text",  class:"form-control inline", id:"autocomplete"}));

  	ac_btn_div = $("<div>", {class: "inline"});
  	ac_btn_div.append($("<button>", {class:"btn btn-success", id:"autocomplete_btn", text: "Submit", onClick:"submitMainSearch()"}));
  	
  	
  	autocomplete_div.append(search_title_div).append(input_div).append(ac_btn_div);

    button_div.append(browse).append(search);
    $("body").append(title_div).append(button_div).append(autocomplete_div);
    setupSearchPage();
    setupAutoComplete();
}

function loginSuccess(data) {
   console.log("data: " + data);

   var result = JSON.parse(data);
   console.log("status: " + result["status"]);
   if(result["status"] == "success")
   {
    console.log("login success");
    setupMainPage();
    
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
	
	page_start = 0;
	sort = "";
	titleSort = 1;
	yearSort = 1;
	postRequest("SearchServlet", $("#search-form"), searchSuccess);

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
	$("body").empty().append($("<div>", {class:"background"}));
		
	browse_title = $("<h1>", {class:"search-title", text: "Browse the Movieverse"});
	genre_title = $("<h3>", {class: "genre-title", text: "Browse by Genre:"});
	genre_div = $("<div>", {class:"genre_div"});
    search = $("<button>", {type: "button", class: "btn btn-secondary inline results-search", id: "search-movies", text: "Search"});
	for (key in result) {
		genre_div.append($("<button>", {class: "btn btn-primary genre-btn", text: result[key], onclick: "jQuery.post(\"SearchServlet\", {genre: \"" + result[key] +
			"\"}, searchSuccess)"}));
	}
	
	$("body").append(browse_title).append(genre_title).append(genre_div).append(search);
	setupSearchPage();
	  	 
	alphabet = ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"];
	title_title = $("<h3>", {class: "title-title", text: "Browse by Title:"});
	title_div = $("<div>", {class:"genre_div"});
	for (letter in alphabet) {
		title_div.append($("<button>", {class: "btn btn-primary genre-btn", text: alphabet[letter], onclick: "jQuery.post(\"SearchServlet\", {\"movie-title\": \"" + alphabet[letter] +
			"\", browse: \"true\"}, searchSuccess)"}));
	}
	
	$("body").append(title_title).append(title_div);
	
}

function setupSearchPage() {
	$("#search-movies").click(function() {

  	  console.log("choose Search option");

  	  $("body").empty().append($("<div>", {class:"background"}));
      browse = $("<button>", {type: "button", class: "btn btn-primary", id: "browse-movies", text: "Browse", onclick: "jQuery.post(\"BrowseServlet\", browseSuccess)"});
	  cart = $("<button>", {type: "button", class: "btn btn-success inline results-cart", id: "cart-page", text: "Cart", onclick: "cartPage(\"\")"});

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
  	  $("body").append(search_title).append(search_div).append(browse).append(cart);
  	  
  	  $("#search-form").submit((event) => submitSearch(event));

  	});
}

function searchSuccess(data) {

	$("body").empty().append($("<div>", {class:"background"}));
	console.log("data: " + data);
	current_movies = data;
	
	
	// IF EMPTY
	if(data == "") {
		$("body").append($("<h3>", {style: "text-align: center", text: "No results found!"}));
	}
	else {
		
	
		var result = JSON.parse(data);

		last = Object.keys(result)[Object.keys(result).length-1];
		console.log("LAST: " + last);
		
		resultsTitle = $("<h1>", {class:"main-title", text: "Search Results:"});
	    search = $("<button>", {type: "button", class: "btn btn-secondary inline results-search", id: "search-movies", text: "Search"});
	    browse = $("<button>", {type: "button", class: "btn btn-primary inline results-browse", id: "browse-movies", text: "Browse", onclick: "jQuery.post(\"BrowseServlet\", browseSuccess)"});
	    cart = $("<button>", {type: "button", class: "btn btn-success inline results-cart", id: "cart-page", text: "Cart", onclick: "cartPage(\"\")"});
	    
//	    cart.click(function() {
//	    	cartPage("");
//	    });
//	    search.click(function() {
//	    	setupMainPage();
//	    });
	    setupSearchPage();
	    setupBrowsePage();
	    // LISTING INPUT
	    listingDiv = $("<div>", {class: "listing-div"});
	    listingTitle = $("<h3>", {class: "listing-title inline", text: "# of Listings: "});
	    listingInput = $("<input>", {type: "text", id:"form1", class:"form-control inline"});
	    listingLabel = $("<label>", {for: "form1", id:"listing-label", class: "", text: page_limit});
	    listingButton = $("<button>", {class:"btn btn-primary", id: "limit-button", text: "SHOW"} );
	    listingDiv.append(listingTitle).append(listingInput).append(listingLabel).append(listingButton);
	    
	    
	    // NEXT AND PREVIOUS
	    nextprevDiv = $("<div>", {class: "next-prev-div"});
	    nextBtn = $("<button>", {class: "next-btn btn btn-primary inline", text: "NEXT"});
	    showingNum = page_start + page_limit;
	    if(showingNum > Object.keys(result).length) {
	    	showingNum = Object.keys(result).length;
	    }
	    showListing = $("<h2>", {class: "show-listing inline", text: "Showing " + (page_start + 1) + " - " + showingNum});
	    prevBtn = $("<button>", {class: "prev-btn btn btn-primary inline", text: "PREV"});
	    nextprevDiv.append(nextBtn).append(showListing).append(prevBtn);
	    
	    nextBtn.click(function() {
	    	totalLimit = page_start + page_limit;
	    	console.log(Object.keys(result).length + " vs. " + totalLimit);
	    	if (Object.keys(result).length > totalLimit) {
	    		page_start += page_limit;
	    		searchSuccess(current_movies);
	    	}
	    	else {
	    		alert("No more movies to show");
	    	}
	    });
	    
	    prevBtn.click(function() {
	    	floor = page_start - page_limit;
	    	if(floor >= 0) {
	    		page_start -= page_limit;
	    		searchSuccess(current_movies);
	    	}
	    	else {
	    		alert("No more movies to show");
	    	}
	    });
	    
	    
		$("body").append(resultsTitle).append(listingDiv).append(nextprevDiv).append(search).append(browse).append(cart);
		setupSearchPage();
		keys = Object.keys(result);
		
		// LIMIT # OF LISTINGS
		temp_limit = page_limit;
		if(keys.length - page_start < temp_limit) {
			temp_limit = keys.length;
		}
		else {
			temp_limit += page_start;
		}
		
		// CREATE MOVIE TABLE
		
		movieTable = $("<table>", {class:"table table-striped table-bordered", style:"background: #FFFFFF", id:"movie-table"});
		tableHead = $("<thead>");
		headRow = $("<tr>").append($("<th>", {text: "id"})).append($("<td>", {text:"title", class:"dropdown-toggle", onclick: "sortTable(\"title\")"})).append(
				$("<td>", {text: "year", class:"dropdown-toggle", onclick: "sortTable(\"year\")"})).append(
				$("<td>", {text: "director"})).append($("<td>", {text:"Click for genres"})).append($("<td>", {text: "Click for stars"})).append(
				$("<td>", {text: "Add to Cart"}));
		tableBody = $("<tbody>");
	
		movieTable.append(tableHead.append(headRow)).append(tableBody);
		
		
			
		for (i = page_start; i < temp_limit; i++) {

			if (sort == "title") {
				var items = Object.keys(result).map(function(key) {
				    return [key, result[key]];
				});				
				
				items.sort(function(a, b) {
					var A = a[1]["title"].toLowerCase(), B = b[1]["title"].toLowerCase();
					if (A < B) {
						return -titleSort;
					}
					if(A > B) {
						return titleSort;
					}
					else {
						return 0;
					}
				});
				new_result = {};
				for (item in items) {
					new_result[items[item][0]] = items[item][1];
				}
				result = new_result;
				keys = Object.keys(result);
			}
			if (sort == "year") {
				var items = Object.keys(result).map(function(key) {
				    return [key, result[key]];
				});				
				
				items.sort(function(a, b) {
					if(yearSort == 1) {
						return a[1]["year"] - b[1]["year"];
					}
					else {
						return b[1]["year"] - a[1]["year"];
					}
				});
				new_result = {};
				for (item in items) {
					new_result[items[item][0]] = items[item][1];
				}
				result = new_result;
				keys = Object.keys(result);
			}
			id = keys[i];

			movieRow = $("<tr>");
			movieID = $("<td>", {text: id});
			movieTitle = $("<td>");
			movieDirector = $("<td>", {text: result[id]["director"]});
			movieYear = $("<td>", {text: result[id]["year"]});
			
			
	//		GENRE LIST
			genreList = result[id]["genres"];
			movieGenres = $("<td>");
			dropdownG = $("<a>", {class:"dropdown-toggle", "data-toggle": "dropdown", "aria-haspopup": true, "aria-expanded":"false", text:"Genres"});
			dropdownMenuG = $("<div>", {class:"dropdown-menu"});
			buildGenre = "";
			length = Object.keys(genreList).length;
			index = 1;
			for (key in genreList) {
				dropdownMenuG.append($("<a>", {class:"dropdown-item", text: genreList[key]}));
				buildGenre += genreList[key];
				if(length != index) {
					buildGenre += ", ";
				}
				index++;
			}
			movieGenres.append(dropdownG.append(dropdownMenuG));
	
	//		STAR LIST
			starList = result[id]["stars"];

			movieStars = $("<td>");
			dropdownS = $("<a>", {class:"dropdown-toggle", "data-toggle": "dropdown", "aria-haspopup": true, "aria-expanded":"false", text:"Stars"});
			dropdownMenuS = $("<div>", {class:"dropdown-menu"});
			length = Object.keys(starList).length;
			buildStars = "";
			index = 1;
			for (star in starList) {
				dropdownMenuS.append($("<a>", {class:"dropdown-item", onclick: "star_page(\"" + starList[star] + "\")", text: starList[star]}));
				buildStars += starList[star];
				if(length != index) {
					buildStars += ", ";
				}
				index++;
			}
			movieStars.append(dropdownS.append(dropdownMenuS));
			
			
			titleSpan = $("<span>", {style:"color:blue; font-size: 3vh;", text: result[id]["title"], onclick: "movie_page(\"" +
				result[id]["title"] + "\", \"" +
				result[id]["director"] + "\", \"" +
				result[id]["year"] + "\", \"" +
				buildGenre + "\", \"" +
				buildStars + "\")"
			});
			movieTitle.append(titleSpan);
	
			movieRow.append(movieID).append(movieTitle).append(movieYear).append(movieDirector).append(movieGenres).append(movieStars).append(
					$("<button>", {class:"btn btn-success", text: "Add to cart", onclick: "cartPage(\""+result[id]["title"]+"\")"}));
			tableBody.append(movieRow);
		}
		
		

		$("body").append(movieTable);
		$("#limit-button").click(function() {
			page_start = 0;
			submitLimit();
		});
	}
}

function setupAutoComplete() {
	$('#autocomplete').autocomplete({
	    lookup: function (query, doneCallback) {
	    		ajaxFunction(query, doneCallback);
	    },
	    onSelect: function(suggestion) {
	    		handleSelectSuggestion(suggestion)
	    },
	    // set the groupby name in the response json data field
	    groupBy: "category",
	    // set delay time
	    deferRequestBy: 300,
	});
}

function sortTable(new_sort) {
	if(sort == "title" && sort == new_sort) {
		titleSort = -titleSort;
	}
	else if (sort == "year" && sort == new_sort) {
		yearSort = -yearSort;
	}
	else {
		sort = new_sort;
		titleSort = 1;
		yearSort = 1;
	}
	searchSuccess(current_movies);
}

function submitLimit() {
	console.log("SUBMIT LIMIT");
	console.log(parseInt($("#form1").val()));
	page_limit = parseInt($("#form1").val());
	$("#listing-label").text = page_limit;
	searchSuccess(current_movies);
}

function star_page(star) {
	console.log("STAR PAGE");
	jQuery.post("StarServlet", {"star": star}, (resultDataString) => starSuccess(resultDataString));	
	
}

function AutocompleteSuccess(data, query, doneCallback) {
	console.log("Autocomplete full text query results: ");
	console.log("data: " + data);

	var results = JSON.parse(data);
	
	console.log(results);
	doneCallback( { suggestions: results } );

}

function starSuccess(data) {
	results = JSON.parse(data);
	console.log("star data: " + data);
	
	$("body").empty().append($("<div>", {class:"background"}));
	
	title = $("<h1>", {class:"main-title", text: results["name"]});
	details = $("<h3>", {class: "details-title", text: "Star Details: (Placeholder Image)"});
	img = $("<img>", {src: "movie-star.jpg"});
	movie_div = $("<div>", {class:"movie-div-single"});
	year = $("<h3>", {class:"movie-info", text: "Birth Year: " + results["birthYear"]});
	
	movies = $("<h3>", {class: "movie-genre", text: "Movies: "});
	index = 0;
	for (m in results["movies"]) {
		var span = results["movies"][m];
		console.log(span);
		var span2 = span;
		if(Object.keys(results["movies"]).length != (index + 1)) {
			span2 +=", ";
		}	
		movies.append($("<span>", {style: "color: blue", text: span2, onclick:"jQuery.post(\"SearchServlet\", {\"movie-title\": \"" + span + "\", \"search\":\"true\"}, movieSuccess)"}));
		index++;
	}
	if (results["birthYear"] != 0) {
		movie_div.append(year);
	}
	movie_div.append(movies);
	$("body").append(title).append(details).append(img).append(movie_div);
	
 
}

function movieSuccess(data) {
	var results = JSON.parse(data);
	
	console.log("movie data: " + data);
	
	// CONSTRUCT GENRE LIST
	
	id = Object.keys(results)[0];
	
	genreList = results[id]["genres"];
	var buildGenre = "";

	length = Object.keys(genreList).length;
	index = 0;
	for (key in genreList) {
		buildGenre += genreList[key];

		if(length != (index+1)) {
			buildGenre += ", ";
		}
		index++;
	}

	// CONSTRUCT STAR LIST
	starList = results[id]["stars"];
	var buildStars = "";
	length = Object.keys(starList).length;
	listSpans = [];
	index = 0;
	for (star in starList) {
		buildStars += starList[star];

		if(length != (index + 1)) {
			buildStars += ", ";
		}	
		index++;
	}
	movie_page(results[id]["title"], results[id]["director"], results[id]["year"], buildGenre, buildStars);
}

function movie_page(movie_title, movie_director, movie_year, movie_genres, movie_stars) {
	$("body").empty().append($("<div>", {class:"background"}));
	
	title = $("<h1>", {class:"main-title", text: movie_title});
	details = $("<h3>", {class: "details-title", text: "Movie Details: (Placeholder Image)"});
	img = $("<img>", {src: "movie-poster.jpg"});
	movie_div = $("<div>", {class:"movie-div-single"});
	//rating = $("<h3>", {class: "movie-rating":, text: "Rating: " + })
	director = $("<h3>", {class:"movie-info", text: "Director: " + movie_director});
	year = $("<h3>", {text: "Year: " + movie_year});
	
//	GENRES
	genres = $("<h3>", {class: "movie-genre", text: "Genres: "});
	genreList = movie_genres.split(",");
	console.log("GENRELIST: " + genreList);
	spanList = [];
	for (g in genreList) {
		span_text = genreList[g];
		genres.append($("<span>", {style:"color:blue;", onclick: "jQuery.post(\"SearchServlet\", {genre: \"" + span_text +
			"\"}, searchSuccess)", text:span_text}));
//		spanList.push("<span style= \"color: blue;\" onclick= \"genre_page(\"" + span_text + "\")\"> " + span_text + " </span>");
	}
//	genres.append(spanList.join());
	
	
//	STARS
	stars = $("<h4>", {class:"movie-stars", text: "Starring: "});
	starList = movie_stars.split(", ");
	spanList = [];
	index = 1;
	for (s in starList) {
		span_text = starList[s];
		span_text2 = span_text;
		if(Object.keys(starList).length != index){
			span_text2+=", ";
		}
		console.log(starList[s]);
		index++;
		stars.append($("<span>", {style: "color:blue;", onclick: "star_page(\"" + span_text + "\")", text: span_text2}));
//		spanList.push("<span style= \"color: blue;\" onclick= \"star_page(\"" + span_text + "\")\"> " + span_text + " </span>");
	}
//	stars.append(spanList.join());
	
	movie_div.append(img).append(director).append(year).append(genres).append(stars);
	$("body").append(title).append(details).append(img).append(movie_div);
}

function cartPage(title) {
	console.log("CART PAGE");
	$("body").empty().append($("<div>", {class:"background"}));
	cartDiv = $("<div>", {class:"cart-div"});
	cartTitle = $("<h1>", {class: "cart-title", text: "Shopping Cart:"});
	$("body").append(cartDiv.append(cartTitle));
	
	cartTable = $("<table>", {class:"table table-striped table-bordered", style:"background: #FFFFFF", id:"cart-table"});
	tableHead = $("<thead>");
	headRow = $("<tr>").append($("<td>", {text: "Movie Title"})).append($("<td>", {text: "Price"})).append($("<td>", {text:"Qty"})).append($("<td>")).append($("<td>"));
	
	tableBody = $("<tbody>");	
	
	cartTable.append(tableHead.append(headRow));
	if ($.inArray(title, Object.keys(shopping_cart)) == -1 && title != "") {
		shopping_cart[title] = 1;
	}

	else if(title != ""){
		shopping_cart[title] += 1;
		console.log("HERE");
	}
	
	for (k in shopping_cart) {
		console.log("Adding: " + k);
		new_row = $("<tr>", {id: k});
		inputBox = $("<td>").append($("<input>", {type:"number", value: shopping_cart[k], id: "inputBox"}));
		updateBtn = $("<td>").append($("<button>", {text: "Update", id: "update-btn"}));
		removeBtn = $("<td>").append($("<button>", {text: "Remove", id: "remove-btn", value:k}));
		new_row.append($("<td>", {text: k, style: "font-size: 3vh"})).append($("<td>", {style: "font-size: 3vh", text: "$5.99"})).append(inputBox).append(updateBtn).append(removeBtn);
		
		updateBtn.click(function() {
			shopping_cart[k] = $("#inputBox").val();
			console.log($("#inputBox").val());
			alert(k + " quantity updated to: " + shopping_cart[k]);
			cartPage("");
		});
		removeBtn.click(function() {
			delete shopping_cart[k];
			alert(k + " removed from cart!");
			cartPage("");
		});
		tableBody.append(new_row);
	}
	cartTable.append(tableBody);
	cartDiv.append(cartTable);
	
    browse = $("<button>", {type: "button", class: "btn btn-primary", id: "browse-movies", text: "Browse", onclick: "jQuery.post(\"BrowseServlet\", browseSuccess)"});
    search = $("<button>", {type: "button", class: "btn btn-secondary", id: "search-movies", text: "Search"});

    $("body").append(browse).append(search);
}
