package com.yiyao.app.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

import com.yiyao.app.common.request.SaveProjectRequest;
import com.yiyao.app.model.Project;
import com.yiyao.app.repository.ProjectRepository;
import com.yiyao.app.utils.BeanUtil;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@Controller
public class ProjectController {

	private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
	
	@Autowired
    private ProjectRepository projectRepository;
	
	@Autowired
	private ElasticsearchTemplate esTemplate;
	
	@PostMapping(value = "project/save")
	public String saveProject(SaveProjectRequest request,Model model) {
		
		Project project = new Project();
		BeanUtil.copyBean(request, project);
		if(project.getId() == null) {
			project.setId(UUID.randomUUID().toString());
		}
		project.setDescription(request.getInfo());
		project.setNow(System.currentTimeMillis());
		projectRepository.save(project);
		return "redirect:/project/list";
	}
	
	@GetMapping(value = "project/get")
	public String getProject(@RequestParam(required=false,value="id") String id, Model model) {
		Project project = new Project();
		if(id != null) {
			project = projectRepository.findById(id).get();
		}
		model.addAttribute("project", project);
		return "manageProCon";
	}
	
	@GetMapping(value = "project/list")
	public String users(@RequestParam(required=false,value="title") String title,
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
		List<Project> projectList = new ArrayList<Project>();
		if(esTemplate.indexExists(Project.class)) {
			totalCount = projectRepository.count();
			totalPages = Math.round(totalCount/pageSize); 
			if (totalCount != 0) {
				Sort sort = new Sort(Direction.DESC, "now");
				Pageable pageable = new PageRequest(pageIndex, pageSize,sort);
				SearchQuery searchQuery = new NativeSearchQueryBuilder()
						.withPageable(pageable).build();
				Page<Project> projectsPage = projectRepository.search(searchQuery);
				projectList = projectsPage.getContent();
			}
		}
		model.addAttribute("projectList", projectList);
		model.addAttribute("pageSize", pageSize);
		model.addAttribute("pageIndex", pageIndex);
		model.addAttribute("totalPages", totalPages);
			
		return "manage_pro";
	}
	
	
	
	
}
