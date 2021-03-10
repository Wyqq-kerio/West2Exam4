package com.nd.service;

import com.nd.mapper.NDFileMapper;
import com.nd.vo.NDFile;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NDFileService {

    @Resource
    private NDFileMapper ndFileMapper;

    public void save(NDFile ndFile) {
        ndFileMapper.save(ndFile);
    }

    public NDFile getById(Integer id) {
        return ndFileMapper.getById(id);
    }

    public void delete(Integer id) {
        ndFileMapper.delete(id);
    }

    public NDFile getByFileName(String fileName) {
        return ndFileMapper.getByFileName(fileName);
    }

    public void approve(Integer id) {
        ndFileMapper.approve(id);
    }

    public void updatePath(NDFile ndFile) {
        ndFileMapper.updatePath(ndFile);
    }

    public List<NDFile> findUnApproveImgByPage(Integer pageNum, Integer pageSize) {
        return ndFileMapper.findUnApproveImgByPage((pageNum - 1) * pageSize, pageSize);
    }

}
