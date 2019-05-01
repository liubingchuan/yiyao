package com.yiyao.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.Source;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yiyao.app.common.R;
import com.yiyao.app.common.request.SavePaperRequest;
import com.yiyao.app.mapper.PaperMapper;
import com.yiyao.app.mapper.UserMapper;
import com.yiyao.app.model.Paper;
import com.yiyao.app.model.PaperMysql;
import com.yiyao.app.model.Project;
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
	
	@Autowired
    private PaperMapper paperMapper;
	
	
//	@GetMapping(value = "paper/transfer")
//	@ResponseBody
//	public R transferPaper() {
//		int currentPage = 0;
//		int pageSize = 0;
//		int index = 0;
//		for(int i=2061;i<2359;i++){
//			
//			currentPage = (currentPage <= 0) ?1:currentPage;
//			pageSize = (pageSize<=0) ? 100:pageSize;
//			index = (currentPage - 1) * pageSize;
//			System.out.println(i);
//			List<PaperMysql> papers = paperMapper.getPapers(index, pageSize);
//			currentPage++;
//			List<Paper> tobesaved = new LinkedList<Paper>();
//			for(PaperMysql paperMysql: papers) {
//				Paper paperES = new Paper();
//				
//				paperES.setId(paperMysql.getId()+"");
//				
//				if (paperMysql.getTitle() != null) {
//					paperES.setTitle(paperMysql.getTitle().trim());
//				}
//				
//				String description = paperMysql.getDescription();
//				if (description != null) {
//					String regEx_html="<[^>]+>";
//					Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
//			        Matcher m_html=p_html.matcher(description); 
//			        description=m_html.replaceAll(""); 
//					paperES.setSubject(description.replaceAll("\\s+", " "));
//				}
//				
//				String author = paperMysql.getCreator();
//				if (author != null && !author.trim().equals("")) {
//					author = author.trim();
//					String regEx_html="<[^>]+>";
//					Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
//			        Matcher m_html=p_html.matcher(author); 
//			        author=m_html.replaceAll(""); 
//					
//					List<String> authorlist = new ArrayList<String>();
//					if (author.contains(";")) {
//						String[] authors = author.split(";");
//						for(String s:authors){
//							authorlist.add(s);
//						}
//					}else{
//						authorlist.add(author);
//					}
//					paperES.setAuthor(authorlist);
//				}
//				
//				String keyword = paperMysql.getKeywords();
//				if (keyword != null && !keyword.trim().equals("")) {
//					if (keyword.contains("keyword:")) {
//						keyword = keyword.replace("keyword:", "").trim();
//						List<String> keywordlist = new ArrayList<String>();
//						if (keyword.contains(",")) {
//							String[] keywords = keyword.split(",");
//							for(String s:keywords){
//								keywordlist.add(s);
//							}
//						}else{
//							keywordlist.add(keyword);
//						}
//						paperES.setKeywords(keywordlist);
//					}
//				}
//				
//				if (paperMysql.getSource()!= null) {
//					String source = paperMysql.getSource().trim();
//					int n = source.indexOf("(");
//					source = source.substring(0, n);
//					if (source.contains(",")) {
//						String[] so = source.split(",");
//						if (so.length == 3) {
//							if (!so[0].trim().equals("")) {
//								paperES.setJournal(so[0].trim());
//							}
//							if (!so[1].trim().equals("")) {
//								paperES.setVolume(so[1].trim());
//							}
//							if (!so[2].trim().equals("")) {
//								paperES.setIssue(so[2].trim());
//							}
//						}
//						if (so.length == 2) {
//							if (!so[0].trim().equals("")) {
//								paperES.setJournal(so[0].trim());
//							}
//							if (!so[1].trim().equals("")) {
//								paperES.setVolume(so[1].trim());
//							}
//							
//						}
//					}
//				}
//				
//				String date = paperMysql.getDates();
//				if (date != null && !date.trim().equals("")) {
//					date = date.trim();
//					paperES.setPublictime(date);
//					
//					if (date.contains("-")) {
//						paperES.setYear(date.split("-")[0]);
//					}else{
//						paperES.setYear(date);
//					}
//				}
//				
//				paperES.setNow(System.currentTimeMillis());
//				
//				String subjects = paperMysql.getSubjects();
//				if (subjects != null && !subjects.trim().equals("")) {
//					subjects = subjects.trim();
//					List<String> subjectslist = new ArrayList<String>();
//					if (subjects.contains(",")) {
//						String[] subjectss = subjects.split(",");
//						for(String s:subjectss){
//							subjectslist.add(s);
//						}
//					}else {
//						subjectslist.add(subjects);
//					}
//					paperES.setSubjects(subjectslist);
//				}
//				
//				if (paperMysql.getGooaidentifier()!= null) {
//					paperES.setGooalink(paperMysql.getGooaidentifier().trim());
//				}
//				
//				if (paperMysql.getDoiidentifier()!= null) {
//					paperES.setDoi(paperMysql.getDoiidentifier().trim());
//				}
//				
//				if (paperMysql.getRelation()!= null) {
//					paperES.setRelation(paperMysql.getRelation().trim());
//				}
//				
//				if (paperMysql.getPublisher()!= null) {
//					paperES.setPublisher(paperMysql.getPublisher().trim());
//				}
//				
//				if (paperMysql.getTypes()!= null) {
//					paperES.setType(paperMysql.getTypes().trim());
//				}
//				tobesaved.add(paperES);
////				paperRepository.save(paperES);
//				
//				
//			}
//			paperRepository.saveAll(tobesaved);
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		List<PaperMysql> papers = paperMapper.getPapers(0, 1);
//		for(PaperMysql paperMysql: papers) {
//			Paper paperES = new Paper();
//			paperES.setTitle(paperMysql.getTitle());
//			paperES.setNow(System.currentTimeMillis());
//			paperRepository.save(paperES);
//			System.out.println();
//		}
//		
//		return R.ok();
//	}
//	
	
	@GetMapping(value = "paper/get")
	public String getPaper(@RequestParam(required=false,value="id") String id, Model model) {
		Paper paper = new Paper();
		if(id != null) {
			paper = paperRepository.findById(id).get();
		}
		model.addAttribute("paper", paper);
		Integer pageIndex = 0;
		Integer pageSize = 10;
		long totalCount = 0L;
		long totalPages = 0L;
		List<Paper> paperList = new ArrayList<Paper>();
		if(esTemplate.indexExists(Paper.class) && paper.getTitle() != null) {
			// 分页参数
			Pageable pageable = new PageRequest(pageIndex, pageSize);

			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("title", paper.getTitle()));
//			if(entrust != null) {
//				String[] entrusts = entrust.split("-");
//				queryBuilder.filter(QueryBuilders.termsQuery("entrust", entrusts));
//			}
			
			// 分数，并自动按分排序
			FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(queryBuilder, ScoreFunctionBuilders.weightFactorFunction(1000));

			// 分数、分页
			SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
					.withQuery(functionScoreQueryBuilder).build();

			Page<Paper> searchPageResults = paperRepository.search(searchQuery);
			paperList = searchPageResults.getContent();
			totalCount = esTemplate.count(searchQuery, Paper.class);
			
			
			totalPages = Math.round(totalCount/pageSize);
			if(paperList.size()<2) {
				paperList = new ArrayList<Paper>();
			}else {
				paperList = paperList.subList(1, paperList.size());
			}
			model.addAttribute("paperList", paperList);
		}
		return "result-wxCon";
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

				BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().should(QueryBuilders.matchPhraseQuery("title", q)).should(QueryBuilders.matchPhraseQuery("subject", q));
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
						.addAggregation(AggregationBuilders.terms("aginstitution").field("institution").order(Terms.Order.count(false)).size(10))
						.addAggregation(AggregationBuilders.terms("agjournal").field("journal").order(Terms.Order.count(false)).size(10))
						.addAggregation(AggregationBuilders.terms("agauthor").field("author").order(Terms.Order.count(false)).size(10))
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
					
					StringTerms institutionTerms = (StringTerms) aggregations.asMap().get("aginstitution");
					Iterator<Bucket> institutionbit = institutionTerms.getBuckets().iterator();
					Map<String, Long> institutionMap = new HashMap<String, Long>();
					while(institutionbit.hasNext()) {
						Bucket institutionBucket = institutionbit.next();
						institutionMap.put(institutionBucket.getKey().toString(), Long.valueOf(institutionBucket.getDocCount()));
					}
					model.addAttribute("aginstitution", institutionMap);
					
					StringTerms journalTerms = (StringTerms) aggregations.asMap().get("agjournal");
					Iterator<Bucket> journalbit = journalTerms.getBuckets().iterator();
					Map<String, Long> journalMap = new HashMap<String, Long>();
					while(journalbit.hasNext()) {
						Bucket journalBucket = journalbit.next();
						journalMap.put(journalBucket.getKey().toString(), Long.valueOf(journalBucket.getDocCount()));
					}
					model.addAttribute("agjournal", journalMap);
					
					StringTerms authorTerms = (StringTerms) aggregations.asMap().get("agauthor");
					Iterator<Bucket> authorbit = authorTerms.getBuckets().iterator();
					Map<String, Long> authorMap = new HashMap<String, Long>();
					while(authorbit.hasNext()) {
						Bucket authorBucket = authorbit.next();
						authorMap.put(authorBucket.getKey().toString(), Long.valueOf(authorBucket.getDocCount()));
					}
					model.addAttribute("agauthor", authorMap);
					
				}
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
	
	 
	public static void main(String[] args){
		String subjects ="         <h4>Background</h4>  <tt>    <p style=\"line-height:160%\">Prognosis of ";
		String regEx_html="<[^>]+>";
		Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE); 
        Matcher m_html=p_html.matcher(subjects); 
        subjects=m_html.replaceAll(""); 
		System.out.println(subjects.replaceAll("\\s+", " "));
	}
	
	
}
