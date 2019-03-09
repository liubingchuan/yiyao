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

import com.yiyao.app.common.request.SavePaperRequest;
import com.yiyao.app.model.Paper;
import com.yiyao.app.repository.PaperRepository;
import com.yiyao.app.utils.BeanUtil;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@Controller
public class PaperController {

	private static final Logger logger = LoggerFactory.getLogger(PaperController.class);
	
	@Autowired
    private PaperRepository paperRepository;
	
	@Autowired
	private ElasticsearchTemplate esTemplate;
	
//	@PostMapping(value = "papepr/save")
//	public String savePaper(SavePaperRequest request,Model model) {
//		
//		Paper paper = new Project();
//		BeanUtil.copyBean(request, project);
//		if(project.getId() == null || "".equals(project.getId())) {
//			project.setId(UUID.randomUUID().toString());
//		}
//		project.setDescription(request.getInfo());
//		project.setNow(System.currentTimeMillis());
////		List<String> list = new ArrayList<String>();
////		list.add("sdf");
////		list.add("gasdf");
////		list.add("kkkkkk");
////		project.setList(list);
//		projectRepository.save(project);
//		return "redirect:/project/list";
//	}
	
	@GetMapping(value = "paper/get")
	public String getPaper(@RequestParam(required=false,value="id") String id, Model model) {
		Paper paper = new Paper();
		if(id != null) {
			paper = paperRepository.findById(id).get();
		}
		model.addAttribute("paper", paper);
		return "manageProCon";
	}
	
	@GetMapping(value = "paper/list")
	public String projects(@RequestParam(required=false,value="q") String q,
			@RequestParam(required=false,value="year") String year,
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
		List<Paper> paperList = new ArrayList<Paper>();
		String view = "result-wx";
		if(esTemplate.indexExists(Paper.class)) {
			if(q == null) {
				totalCount = paperRepository.count();
				if(totalCount >0) {
					Sort sort = new Sort(Direction.DESC, "now");
					Pageable pageable = new PageRequest(pageIndex, pageSize,sort);
					SearchQuery searchQuery = new NativeSearchQueryBuilder()
							.withPageable(pageable).build();
					Page<Paper> projectsPage = paperRepository.search(searchQuery);
					paperList = projectsPage.getContent();
				}
			}else {
				// 分页参数
				Pageable pageable = new PageRequest(pageIndex, pageSize);

				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("title", q)).should(QueryBuilders.matchQuery("subject", q));
				if(year != null) {
					String[] years = year.split("-");
					queryBuilder.filter(QueryBuilders.termsQuery("year", years));
				}
				
				
				// 分数，并自动按分排序
				FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder, ScoreFunctionBuilders.weightFactorFunction(1000));

				// 分数、分页
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
						.withQuery(functionScoreQueryBuilder).build();

				Page<Paper> searchPageResults = paperRepository.search(searchQuery);
				paperList = searchPageResults.getContent();
				totalCount = esTemplate.count(searchQuery, Paper.class);
				
				
				BoolQueryBuilder queryBuilderAgg = QueryBuilders.boolQuery().filter(QueryBuilders.matchQuery("title", q));
				FunctionScoreQueryBuilder functionScoreQueryBuilderAgg = QueryBuilders.functionScoreQuery(queryBuilderAgg, ScoreFunctionBuilders.weightFactorFunction(1000));
				List<String> pList=new ArrayList<>();
				SearchQuery nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
						.withQuery(functionScoreQueryBuilderAgg)
						.withSearchType(SearchType.QUERY_THEN_FETCH)
						.withIndices("paper").withTypes("pr")
						.addAggregation(AggregationBuilders.terms("agyear").field("year").order(Terms.Order.count(false)).size(10))
						.build();
				Aggregations aggregations = esTemplate.query(nativeSearchQueryBuilder, new ResultsExtractor<Aggregations>() {
			        @Override
			        public Aggregations extract(SearchResponse response) {
			            return response.getAggregations();
			        }
			    });
				
				if(aggregations != null) {
					StringTerms yearTerms = (StringTerms) aggregations.asMap().get("agyear");
					Iterator<Bucket> yearbit = yearTerms.getBuckets().iterator();
					Map<String, Long> yearMap = new HashMap<String, Long>();
					while(yearbit.hasNext()) {
						Bucket yearBucket = yearbit.next();
						yearMap.put(yearBucket.getKey().toString(), Long.valueOf(yearBucket.getDocCount()));
					}
					model.addAttribute("agyear", yearMap);
					
				}
//				nativeSearchQueryBuilder.withQuery(functionScoreQueryBuilder);
//				nativeSearchQueryBuilder.withSearchType(SearchType.QUERY_THEN_FETCH);
//				TermsAggregationBuilder termsAggregation = AggregationBuilders.terms("aglist").field("list").order(Terms.Order.count(false)).size(10);
//				nativeSearchQueryBuilder.addAggregation(termsAggregation);
//		    	NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
//		    	Page<Project> search = projectRepository.search(nativeSearchQuery);
//		    	List<Project> content = search.getContent();
//		    	for (Project project : content) {
//		    		pList.add(esBlog.getUsername());
//				}
				totalPages = Math.round(totalCount/pageSize);
				
				
			}
		}
		model.addAttribute("paperList", paperList);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalPages", totalPages);
		model.addAttribute("totalCount", totalCount);
		model.addAttribute("query", q);
			
		return view;
	}
	
	
	
	
}
