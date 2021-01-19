package com.cmm.springboottest;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class fileUploadController {
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile fileData, HttpServletRequest req)
            throws IllegalStateException, IOException {
        if (fileData.isEmpty()) {
            return "failed";
        }
        String path = req.getServletContext().getRealPath("/WEB-INF/file");
        String fileName = fileData.getOriginalFilename();
        File file = new File(path, fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            System.out.println("创建目录" + file);
        }
        fileData.transferTo(file);
        return "success";
    }

    @PostMapping("/upload2")
    public String  upload2(HttpServletRequest request) throws IllegalStateException, IOException
    {
        ServletContext sevletContext=request.getSession().getServletContext();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）
        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(sevletContext);
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request)){
            //将request变成multiRequest
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();
            while(iter.hasNext()){//遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null){
                    String path="G:/springUpload"+file.getOriginalFilename();
                    file.transferTo(new File(path));//上传
                }
            }
        }

        return "success";
    }


    @PostMapping("/upload3")
    public Map<String, Object>  upload3(MultipartFile myfiles, HttpServletRequest req)
            throws IllegalStateException, IOException{
        Map<String, Object> resMap = new HashMap<>();
        String path = req.getContextPath();
        String realPath ="g:/files/";
        String originalFilename=myfiles.getOriginalFilename();
        File fileObj=new File(realPath, originalFilename);
        FileUtils.copyInputStreamToFile(myfiles.getInputStream(), fileObj);
        return resMap;//失败？？
    }

    @PostMapping("/upload4")
    public String  upload4(@RequestParam("file") CommonsMultipartFile file)
            throws IOException {
        String fileName=file.getOriginalFilename();
        String filePath="g:/"+fileName;
        OutputStream os=new FileOutputStream(filePath);
        InputStream is=file.getInputStream();
        byte[] bytex = new byte[1024];
        while(is.read(bytex)!=-1){
            os.write(bytex);
        }
        os.flush();
        os.close();
        is.close();
        return "success";//失败？？
    }







}
