package com.nd.vo;

public class NDFile {

    private Integer id;
    private String originalFilename; // 原始名称
    private String fileName;
    private Integer status; // 状态：0未审核、1已审核
    private String creteTime;
    private String creteName;
    private Integer creteId;
    private String path;

    public String getCreteName() {
        return creteName;
    }

    public void setCreteName(String creteName) {
        this.creteName = creteName;
    }

    public Integer getCreteId() {
        return creteId;
    }

    public void setCreteId(Integer creteId) {
        this.creteId = creteId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getCreteTime() {
        return creteTime;
    }

    public void setCreteTime(String creteTime) {
        this.creteTime = creteTime;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}
