package com.solrstart.javadoc.server;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Iterator;
import java.util.List;

@Controller
@EnableAutoConfiguration
@EnableSolrRepositories("com.solrstart.javadoc.server")
public class AutoCompleteServer {

    @Autowired
    private MatchRepository matchRepository;

    @RequestMapping("/lookup")
    @ResponseBody
    List<Match> home(
            @RequestParam(value="query", required=false, defaultValue="solr") String query
    ){

//        return new Match[]{new Match("1", "package", "class", "method", "The matched package.class.method")};


        Page<Match> results = matchRepository.find(query, new PageRequest(0, 10));
        return results.getContent();
    }


    @Bean
    public SolrServer solrServer() {
        return new HttpSolrServer("http://localhost:8983/solr/JavadocCollection");
    }

    @Bean
    public SolrTemplate solrTemplate(SolrServer server) throws Exception {
        return new SolrTemplate(server);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AutoCompleteServer.class, args);
    }

}