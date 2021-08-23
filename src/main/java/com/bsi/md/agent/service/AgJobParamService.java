package com.bsi.md.agent.service;

import com.bsi.framework.core.service.FwService;
import com.bsi.md.agent.entity.AgApiProxy;
import com.bsi.md.agent.entity.AgJobParam;
import com.bsi.md.agent.repository.AgJobParamRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
@Slf4j
public class AgJobParamService extends FwService {
    @Autowired
    private AgJobParamRepository agJobParamRepository;

    /**
     * 根据id查询任务参数
     * @param jobId
     * @return
     */
    public AgJobParam getByJobId(Integer jobId){
        AgJobParam example = new AgJobParam();
        example.setJobId(jobId);
        return agJobParamRepository.findOne(Example.of(example)).orElse(null);
    }

    /**
     * 保存任务参数
     * @param param
     */
    public void save(AgJobParam param){
        agJobParamRepository.save(param);
    }
}
