package com.nd.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.nd.service.NDFileService;
import com.nd.utils.AppConstant;
import com.nd.vo.NDFile;
import com.nd.vo.User;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class NDFileController extends BaseController {

    @Resource
    private NDFileService ndFileService;

    /**
     * 下载文件
     *
     * @param id 文件ID
     */
    @RequestMapping("/download")
    public Object download(Integer id, HttpServletResponse resp) throws UnsupportedEncodingException {
        resp.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=UTF-8");
        NDFile ndFile = ndFileService.getById(id);
        if (ndFile == null) {
            return super.fail(4001, "文件不存在！");
        }
        String downloadName = ndFile.getOriginalFilename();
        // 文件所在路径
        File file = new File(AppConstant.ROOT_PATH + ndFile.getPath(), ndFile.getFileName());
        if (file.exists()) {
            resp.setContentType("application/force-download"); // 设置强制下载不打开
            resp.addHeader("Content-Disposition", "attachment;fileName=" + new String(downloadName.getBytes("GB2312"), StandardCharsets.ISO_8859_1)); // 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = resp.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return null;
    }

    /**
     * 上传文件
     *
     * @param path 路径
     */
    @RequestMapping("/upload")
    public Object upload(MultipartFile file, String path, HttpServletRequest req) {
        try {
            User user = (User) req.getSession().getAttribute(AppConstant.CURRENT_USER);
            if (user == null) {
                return super.fail(4003, "请先登录！");
            }
            if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
                return super.fail(4001, "请选择一张图片！");
            }
            String originalFilename = file.getOriginalFilename();
            if (StringUtils.isEmpty(originalFilename)) {
                return "文件名称为空，请重新上传！";
            }
            String fileName = UUID.randomUUID().toString() + originalFilename.substring(originalFilename.lastIndexOf("."));
            File uploadFile = new File(AppConstant.ROOT_PATH + path);
            if (!uploadFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                uploadFile.mkdirs();
            }
            File savedFile = new File(AppConstant.ROOT_PATH + path, fileName);
            FileUtils.copyInputStreamToFile(file.getInputStream(), savedFile);
            NDFile ndFile = new NDFile();
            ndFile.setFileName(fileName);
            ndFile.setOriginalFilename(originalFilename);
            ndFile.setPath(path);
            ndFile.setStatus(0);
            ndFile.setCreteId(user.getId());
            ndFile.setCreteTime(new DateTime().toString("yyyy-MM-dd HH:mm:ss"));
            ndFileService.save(ndFile);
            return super.success("上传成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4002, "上传失败！");
    }

    /**
     * 创建文件夹
     *
     * @param path    路径
     * @param dirName 文件夹名称
     */
    @RequestMapping("/mkdir")
    public Object mkdir(String path, String dirName) {
        try {
            File dir = new File(AppConstant.ROOT_PATH + path + "/" + dirName);
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            return super.success("创建成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "创建文件夹失败");
    }

    /**
     * 移动文件
     *
     * @param srcPath    源目录
     * @param targetPath 目标目录
     * @param fileId     文件ID
     */
    @RequestMapping("move")
    public Object move(String srcPath, String targetPath, Integer fileId) {
        try {
            NDFile ndFile = ndFileService.getById(fileId);
            File srcFile = new File(AppConstant.ROOT_PATH + srcPath, ndFile.getFileName());
            File targetFile = new File(AppConstant.ROOT_PATH + targetPath, ndFile.getFileName());
            ndFile.setPath(targetPath);
            ndFileService.updatePath(ndFile);
            FileUtils.moveFile(srcFile, targetFile);
            return super.success("移动成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "移动失败");
    }

    /**
     * 删除文件
     *
     * @param id 文件ID
     */
    @RequestMapping("/delete")
    public Object delete(Integer id) {
        try {
            NDFile ndFile = ndFileService.getById(id);
            File file = new File(AppConstant.ROOT_PATH + ndFile.getPath(), ndFile.getFileName());
            FileUtils.forceDelete(file);
            ndFileService.delete(id);
            return super.success("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "删除失败！");
    }

    /**
     * 删除文件夹
     *
     * @param path 路径
     */
    @RequestMapping("/deldir")
    public Object deldir(String path) {
        try {
            File file = new File(AppConstant.ROOT_PATH + path);
            FileUtils.deleteDirectory(file);
            return super.success("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "删除失败！");
    }

    /**
     * 列出文件夹下所有文件和文件夹
     *
     * @param path 路径
     */
    @RequestMapping("/list")
    public Object list(String path, Integer pageNum, Integer pageSize) {
        try {
            File file = new File(AppConstant.ROOT_PATH + path);
            File[] files = file.listFiles();
            if (files == null) {
                return super.success("");
            }
            JSONArray data = new JSONArray();
            for (File item : files) {
                String name = item.getName();
                if (!StringUtils.isEmpty(name) && name.startsWith(".")) {
                    continue;
                }
                JSONObject obj = new JSONObject();
                String type = item.isDirectory() ? "dir" : "file";
                obj.put("type", type);
                if ("file".equals(type)) {
                    NDFile ndFile = ndFileService.getByFileName(name);
                    obj.put("name", ndFile.getOriginalFilename());
                    obj.put("status", ndFile.getStatus());
                } else {
                    obj.put("name", name);
                }
                data.add(obj);
            }
            JSONObject result = new JSONObject();
            result.put("total", data.size());

            int fromIndex = (pageNum - 1) * pageSize;
            int toIndex = (pageNum - 1) * pageSize + pageSize;
            if (toIndex >= data.size()) {
                toIndex = data.size();
            }
            List<Object> objects = data.subList(fromIndex, toIndex);
            data.clear();
            data.addAll(objects);
            return super.success(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "获取失败！");
    }

    /**
     * 批量删除
     *
     * @param ids 文件ID列表（逗号隔开）
     */
    @RequestMapping("/batchDel")
    public Object batchDel(String ids) {
        try {
            String[] list = ids.split(",");
            for (String item : list) {
                NDFile ndFile = ndFileService.getById(Integer.parseInt(item));
                if (ndFile == null) {
                    continue;
                }
                File file = new File(AppConstant.ROOT_PATH + ndFile.getPath(), ndFile.getFileName());
                FileUtils.forceDelete(file);
                ndFileService.delete(Integer.parseInt(item));
            }
            return super.success("删除成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "删除失败！");
    }

    /**
     * 审核
     *
     * @param fileId  文件ID
     * @param approve 0通过，1不通过
     */
    @RequestMapping("approve")
    public Object approve(Integer fileId, Integer approve) {
        try {
            if (approve == 1) {
                NDFile ndFile = ndFileService.getById(fileId);
                File file = new File(AppConstant.ROOT_PATH + ndFile.getPath(), ndFile.getFileName());
                FileUtils.forceDelete(file);
                ndFileService.delete(fileId);
            } else if (approve == 0) {
                ndFileService.approve(fileId);
            }
            return super.success("审核成功!");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "审核失败！");
    }

    /**
     * 分页获取未审核图片
     */
    @RequestMapping("/findUnApproveImgByPage")
    public Object findUnApproveImgByPage(Integer pageNum, Integer pageSize) {
        try {
            return super.success(ndFileService.findUnApproveImgByPage(pageNum, pageSize));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.fail(4001, "获取失败！");
    }

}
