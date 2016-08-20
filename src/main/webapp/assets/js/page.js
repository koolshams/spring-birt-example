/**
 * 
 */
$(function() {
	var container = $('.report-rows');
	var viewer = $('.report-view');
	var reports = {};
	var reportForm = $('.report-form form');
	var report;
	
	function loadReports() {
		$.get('report', function(result) {
			reports = {};
			result.forEach(function(report) {
				reports[report.name] = report;
				$.get('report/thumb/' + report.name, function(res) {
					$('<div class="col-md-6"></div>').append(
							'<h3>' + report.title
									+ '(<a href="javascript:void(0);" data-report="'
									+ report.name + '">View Full Report</a>)</h3>')
							.append($(res).find('embed').parent().html()).appendTo(
									container);
				});
			});
		});
	}
	
	$(container).on('click', 'a', function() {
		viewer.show();
		container.hide();
		report = reports[$(this).data('report')];
		viewer.find('h2 span').text(report.name);
		viewer.find('.content').html("");
		reportForm.find('div').remove();
		report.parameters.forEach(function(param) {
			var element = $('<div class="form-group"></div>');
			element.append($('<label></label> ').text(param.title + ": "));
			element.append($(' <input type="text"/>').attr("name", param.name));
			reportForm.prepend(element);
		});
	});
	
	reportForm.on('submit', function(evt) {
		evt.preventDefault();
		var data = {};
		$(this).find('input').each(function(i, el) {
			data[el.name] = el.value;
		});
		loadMainReport(data)
	});
	
	function loadMainReport(data) {
		viewer.find('.content').html("");
		$.post('report/main/' + report.name, data, function(res) {
			$(res).each(function(i, item) {
				if (item.nodeName === 'STYLE') {
					$('head').append(item);
				} else if (item.nodeName === 'TABLE') {
					viewer.find('.content').append(item);
				}
			})			
		});
	}

	$(viewer).on('click', 'h2 a', function() {
		viewer.hide();
		container.show();
		$('head').find('style').remove();
	});
	
	$('.reload-link').click(function() {
		$('head').find('style').remove();
		container.show();
		viewer.hide();
		container.html("");
		$.get('report/reload', function() {
			loadReports();
		})
	});
	
	loadReports();
})