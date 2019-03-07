package com.yiyao.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.yiyao.app.common.request.SaveOrgRequest;
import com.yiyao.app.common.request.SaveProjectRequest;
import com.yiyao.app.model.Org;
import com.yiyao.app.model.Project;
import com.yiyao.app.repository.OrgRepository;
import com.yiyao.app.repository.ProjectRepository;
import com.yiyao.app.utils.BeanUtil;
import com.yiyao.app.weixin.AesException;
import com.yiyao.app.weixin.SHA1;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@Controller
public class TXController {

	private static final Logger logger = LoggerFactory.getLogger(TXController.class);
	
	@Autowired
    private OrgRepository orgRepository;
	
	@Autowired
	private ElasticsearchTemplate esTemplate;
	
	@GetMapping(value = "check")
	public void check(@RequestParam("signature") String signature,
			@RequestParam("timestamp") String timestamp,
			@RequestParam("nonce") String nonce,
			@RequestParam("echostr") String echostr,
			HttpServletResponse response
			) {
		
		String token = "lexuan";
		String jiami = "";
		try {
            jiami=SHA1.getSHA1(token, timestamp, nonce,"");//这里是对三个参数进行加密
       } catch (AesException e) {
           // TODO Auto-generated catch block
           e.printStackTrace();
       }

		System.out.println("加密"+jiami);
        System.out.println("本身"+signature);
        PrintWriter out;
		try {
			out = response.getWriter();
			if(jiami.equals(signature)){
				out.print(echostr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@GetMapping(value = "org/get")
	public String getOrg(@RequestParam(required=false,value="front") String front,
			@RequestParam(required=false,value="id") String id, Model model) {
		String view = "qiyezhikuzhongdianjigouxiangqing";
		Org org = new Org();
		if(id != null) {
			org = orgRepository.findById(id).get();
		}
		if(front != null) {
			view = "qiyezhikujigouxiangqing";
		}
		
		model.addAttribute("org", org);
		return view;
	}
	
	@GetMapping(value = "org/list")
	public String orgs(@RequestParam(required=false,value="front") String front,
			@RequestParam(required=false,value="q") String q,
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
		List<Org> orgList = new ArrayList<Org>();
		String view = "qiyezhikuzhongdianjigouliebiao";
		if(esTemplate.indexExists(Project.class)) {
			if(q == null) {
				totalCount = orgRepository.count();
				if(totalCount > 0) {
					Sort sort = new Sort(Direction.DESC, "now");
					Pageable pageable = new PageRequest(pageIndex, pageSize,sort);
					SearchQuery searchQuery = new NativeSearchQueryBuilder()
							.withPageable(pageable).build();
					Page<Org> orgPage = orgRepository.search(searchQuery);
					orgList = orgPage.getContent();
					if (front != null) {
						view = "qiyezhikuzhongdianjigou";
					}
				}
			}else {
				// 分页参数
				Pageable pageable = new PageRequest(pageIndex, pageSize);

				// 分数，并自动按分排序
				FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("name", q)), ScoreFunctionBuilders.weightFactorFunction(1000));

				// 分数、分页
				SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
						.withQuery(functionScoreQueryBuilder).build();

				Page<Org> searchPageResults = orgRepository.search(searchQuery);
				orgList = searchPageResults.getContent();
				totalCount = esTemplate.count(searchQuery, Org.class);
				totalPages = Math.round(totalCount/pageSize);
				view = "qiyezhikuzhongdianjigou";
			}
		}
		model.addAttribute("orgList", orgList);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalPages", totalPages);
			
		return view;
	}
	
	
	
	
}
