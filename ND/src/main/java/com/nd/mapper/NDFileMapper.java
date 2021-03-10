package com.nd.mapper;

import com.nd.vo.NDFile;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface NDFileMapper {
    
    void save(NDFile ndFile);

    NDFile getById(Integer id);

    void delete(Integer id);

    NDFile getByFileName(String fileName);

    void approve(Integer id);

    void updatePath(NDFile ndFile);

    List<NDFile> findUnApproveImgByPage(int i, Integer pageSize);

}
