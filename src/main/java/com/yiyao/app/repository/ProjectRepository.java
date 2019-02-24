package com.yiyao.app.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.yiyao.app.model.Project;

public interface ProjectRepository extends ElasticsearchRepository<Project, Long>{

}
