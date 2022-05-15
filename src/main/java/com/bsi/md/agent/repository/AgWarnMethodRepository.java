package com.bsi.md.agent.repository;

import com.bsi.md.agent.entity.AgWarnMethod;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * @author fish
 */
@Component
public interface AgWarnMethodRepository extends JpaRepository<AgWarnMethod, String> {
    @Delete("delete from md_agent_warn_method where id = ?1")
    void deleteById(String id);
}
