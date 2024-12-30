package com.lyq.apiProject.esdao;

import com.lyq.apiProject.model.dto.post.PostEsDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * 帖子 ES 操作
 *
 * @author <a href="https://github.com/lilyq">程序员鱼皮</a>
 * @from <a href="https://lyq.icu">编程导航知识星球</a>
 */
public interface PostEsDao extends ElasticsearchRepository<PostEsDTO, Long> {

    List<PostEsDTO> findByUserId(Long userId);
}