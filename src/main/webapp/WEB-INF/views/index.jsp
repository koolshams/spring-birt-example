<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Reports</title>

<!-- Bootstrap -->
<link href="assets/bootstrap/css/bootstrap.css" rel="stylesheet">
<link href="assets/css/page.css" rel="stylesheet">
</head>
<body>
	<div class="container">
		<h3>
			Reports (<a href="javascript:void(0);" class="reload-link">Reload</a>)
		</h3>
		<div class="row report-rows"></div>
		<div class="report-view" style="display: none">
			<h2>
				<span></span> <a href="javascript:void(0)">back</a>
			</h2>
			<div class="clearfix report-form">
				<form class="form-inline">
					<div class="form-group">
						<label for="exampleInputName2">Name</label> <input type="text"
							class="form-control" id="exampleInputName2"
							placeholder="Jane Doe">
					</div>
					<div class="form-group">
						<label for="exampleInputEmail2">Email</label> <input type="email"
							class="form-control" id="exampleInputEmail2"
							placeholder="jane.doe@example.com">
					</div>
					<button type="submit" class="btn btn-default">Generate</button>
				</form>
			</div>
			<div class="clearfix content"></div>
		</div>
	</div>

	<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
	<script src="assets/jquery/jquery-3.1.0.min.js""></script>
	<!-- Include all compiled plugins (below), or include individual files as needed -->
	<script src="assets/js/page.js"></script>
</body>
</html>