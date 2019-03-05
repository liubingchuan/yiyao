package com.yiyao.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms.Bucket;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.ResultsExtractor;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiyao.app.common.request.SaveProjectRequest;
import com.yiyao.app.model.Project;
import com.yiyao.app.model.Report;
import com.yiyao.app.repository.ProjectRepository;
import com.yiyao.app.repository.ReportRepository;
import com.yiyao.app.utils.BeanUtil;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@Controller
public class ReportController {

	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	
	@Autowired
    private ReportRepository reportRepository;
	
	@Autowired
	private ElasticsearchTemplate esTemplate;
	
	@PostMapping(value = "report/save")
	public String saveReport(SaveProjectRequest request,Model model) {
		
		Report report = new Report();
		BeanUtil.copyBean(request, report);
		if(report.getId() == null || "".equals(report.getId())) {
			report.setId(UUID.randomUUID().toString());
		}
		report.setNow(System.currentTimeMillis());
		report.setSubject(request.getInfo());
		reportRepository.save(report);
		return "redirect:/report/list";
	}
	
	@GetMapping(value = "report/get")
	public String getReport(@RequestParam(required=false,value="id") String id, Model model) {
		Report report = new Report();
		if(id != null) {
			report = reportRepository.findById(id).get();
		}
		model.addAttribute("report", report);
		
		String view = "qiyezhikufenxibaogaoxiangqing";
//		if(esTemplate.indexExists(Report.class)) {
//			// 分页参数
//			Pageable pageable = new PageRequest(pageIndex, pageSize);
//
//			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", project.getName()));
////			if(entrust != null) {
////				String[] entrusts = entrust.split("-");
////				queryBuilder.filter(QueryBuilders.termsQuery("entrust", entrusts));
////			}
//			
//			// 分数，并自动按分排序
//			FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder, ScoreFunctionBuilders.weightFactorFunction(1000));
//
//			// 分数、分页
//			SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
//					.withQuery(functionScoreQueryBuilder).build();
//
//			Page<Project> searchPageResults = projectRepository.search(searchQuery);
//			projectList = searchPageResults.getContent();
//			totalCount = esTemplate.count(searchQuery, Project.class);
//			
//			
//			totalPages = Math.round(totalCount/pageSize);
//			model.addAttribute("projectList", projectList);
//			view = "result-xmCon";
//		}
		return view;
	}
	
	@GetMapping(value = "report/list")
	public String projects(@RequestParam(required=false,value="q") String q,
			@RequestParam(required=false,value="front") String front,
			@RequestParam(required=false,value="pageSize") Integer pageSize, 
			@RequestParam(required=false, value="pageIndex") Integer pageIndex, 
			Model model) {
		if(pageSize == null) {
			pageSize = 10;
		}
		if(pageIndex == null) {
			pageIndex = 0;
		}
		long totalCount = 0L;
		long totalPages = 0L;
		List<Report> reportList = new ArrayList<Report>();
		String view = "qiyezhikufenxibaogaoliebiao";
		if(esTemplate.indexExists(Report.class)) {
			if(q == null) {
				totalCount = reportRepository.count();
				if(totalCount >0) {
					Sort sort = new Sort(Direction.DESC, "now");
					Pageable pageable = new PageRequest(pageIndex, pageSize,sort);
					SearchQuery searchQuery = new NativeSearchQueryBuilder()
							.withPageable(pageable).build();
					Page<Report> reportsPage = reportRepository.search(searchQuery);
					reportList = reportsPage.getContent();
					if (front != null) {
						view = "qiyezhikufenxibaogaoqiantai";
					}
				}
			}else {
				// 分页参数
				Pageable pageable = new PageRequest(pageIndex, pageSize);

				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", q));
//				if(entrust != null) {
//					String[] entrusts = entrust.split("-");
//					queryBuilder.filter(QueryBuilders.termsQuery("entrust", entrusts));
//				}
				
				
				// 分数，并自动按分排序
				FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder, ScoreFunctionBuilders.weightFactorFunction(1000));

				// 分数、分页
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
						.withQuery(functionScoreQueryBuilder).build();

				Page<Report> searchPageResults = reportRepository.search(searchQuery);
				reportList = searchPageResults.getContent();
				totalCount = esTemplate.count(searchQuery, Report.class);
				
				
				totalPages = Math.round(totalCount/pageSize);
				
				
			}
		}
		model.addAttribute("reportList", reportList);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("name", q);
			
		return view;
	}
	
	
	
	
}
