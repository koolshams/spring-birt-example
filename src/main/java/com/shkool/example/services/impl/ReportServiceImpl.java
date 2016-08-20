package com.shkool.example.services.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IParameterDefn;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import com.shkool.example.dto.AppException;
import com.shkool.example.dto.Report;
import com.shkool.example.dto.Report.Parameter;
import com.shkool.example.dto.Report.ParameterType;
import com.shkool.example.services.ReportService;

/**
 * Report service implementations
 * 
 * @author koolshams
 *
 */
@Service
public class ReportServiceImpl implements ReportService, ApplicationContextAware {

	@Autowired
	private ServletContext servletContext;

	private IReportEngine birtEngine;
	private ApplicationContext context;

	private Map<String, IReportRunnable> thumbnails = new HashMap<>();
	private Map<String, IReportRunnable> reports = new HashMap<>();

	private static final String IMAGE_FOLDER = "/images";

	@SuppressWarnings("unchecked")
	@PostConstruct
	protected void initialize() throws BirtException {
		EngineConfig config = new EngineConfig();
		config.getAppContext().put("spring", this.context);
		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		birtEngine = factory.createReportEngine(config);
		loadReports();
	}

	@Override
	public void setApplicationContext(ApplicationContext context) {
		this.context = context;
	}

	/**
	 * Load report files to memory
	 * 
	 * @throws EngineException
	 */
	public void loadReports() throws EngineException {
		File folder = new File(servletContext.getRealPath("/") + "WEB-INF/reports");
		for (String file : folder.list()) {
			if (!file.endsWith(".rptdesign")) {
				continue;
			}
			if (file.contains("thumb")) {
				thumbnails.put(file.replace("-thumb.rptdesign", ""),
						birtEngine.openReportDesign(folder.getAbsolutePath() + File.separator + file));
			} else {
				reports.put(file.replace(".rptdesign", ""),
						birtEngine.openReportDesign(folder.getAbsolutePath() + File.separator + file));
			}
		}
	}

	@Override
	public List<Report> getReports() {
		List<Report> response = new ArrayList<>();
		for (Map.Entry<String, IReportRunnable> entry : thumbnails.entrySet()) {
			IReportRunnable report = reports.get(entry.getKey());
			IGetParameterDefinitionTask task = birtEngine.createGetParameterDefinitionTask(report);
			Report reportItem = new Report(report.getDesignHandle().getProperty("title").toString(), entry.getKey());
			for (Object h : task.getParameterDefns(false)) {
				IParameterDefn def = (IParameterDefn) h;
				reportItem.getParameters()
						.add(new Parameter(def.getPromptText(), def.getName(), getParameterType(def)));
			}
			response.add(reportItem);
		}
		return response;
	}

	private ParameterType getParameterType(IParameterDefn param) {
		if (IParameterDefn.TYPE_INTEGER == param.getDataType()) {
			return ParameterType.INT;
		}
		return ParameterType.STRING;
	}

	@Override
	public void generateReportThumb(String reportName, HttpServletResponse response, HttpServletRequest request) {
		generateReport(thumbnails.get(reportName), response, request);
	}

	@Override
	public void generateMainReport(String reportName, HttpServletResponse response, HttpServletRequest request) {
		generateReport(reports.get(reportName), response, request);
	}

	/**
	 * Generate a report as html
	 * 
	 * @param report
	 * @param response
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public void generateReport(IReportRunnable report, HttpServletResponse response, HttpServletRequest request) {
		IRunAndRenderTask runAndRenderTask = birtEngine.createRunAndRenderTask(report);
		response.setContentType(birtEngine.getMIMEType("html"));
		IRenderOption options = new RenderOption();
		HTMLRenderOption htmlOptions = new HTMLRenderOption(options);
		htmlOptions.setOutputFormat("html");
		htmlOptions.setImageHandler(new HTMLServerImageHandler());
		htmlOptions.setBaseImageURL(request.getContextPath() + IMAGE_FOLDER);
		htmlOptions.setImageDirectory(servletContext.getRealPath(IMAGE_FOLDER));
		runAndRenderTask.setRenderOption(htmlOptions);
		runAndRenderTask.getAppContext().put(EngineConstants.APPCONTEXT_BIRT_VIEWER_HTTPSERVET_REQUEST, request);
		IGetParameterDefinitionTask task = birtEngine.createGetParameterDefinitionTask(report);
		Map<String, Object> params = new HashMap<>();
		for (Object h : task.getParameterDefns(false)) {
			IParameterDefn def = (IParameterDefn) h;
			if (def.getDataType() == IParameterDefn.TYPE_INTEGER) {
				params.put(def.getName(), Integer.parseInt(request.getParameter(def.getName())));
			} else {
				params.put(def.getName(), request.getParameter(def.getName()));
			}
		}
		try {
			htmlOptions.setOutputStream(response.getOutputStream());
			runAndRenderTask.run();
		} catch (Exception e) {
			throw new AppException(e.getMessage(), e);
		} finally {
			runAndRenderTask.close();
		}
	}

}
