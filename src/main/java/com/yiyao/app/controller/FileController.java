package com.yiyao.app.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yiyao.app.common.R;



@CrossOrigin(origins = "*", maxAge = 3600, allowCredentials = "true")
@Controller
public class FileController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
	
	@Value("${files.path}")
	private String fileRootPath;
	
	
	/**
     * 实现文件上传
     * */
    @RequestMapping(value="file/fileUpload/{oldFile}",method = RequestMethod.POST)
    @ResponseBody 
    public R fileUpload(@PathVariable String oldFile,MultipartFile file){
    	
        if(file==null || file.isEmpty()){
            return R.error();
        }
        String fileName = file.getOriginalFilename();
        UUID uuid = UUID.randomUUID();
        String path = this.fileRootPath ;
        File dest = new File(path + File.separator + uuid + "_" + fileName);
        System.out.println("ssssssss" + path + File.separator + uuid + "_" + fileName);
        if(!dest.getParentFile().exists()){ //判断文件父目录是否存在
            dest.getParentFile().mkdir();
        }
        try {
        	System.out.println(file.getSize());
        	System.out.println("开始保存文件");
            file.transferTo(dest); //保存文件
            // 删除老文件
            File tbdFile = new File(path + File.separator + oldFile);
            if(tbdFile.exists()) {
            	tbdFile.delete();
            	System.out.println("老文件删除成功");
            }
            return R.ok().put("img", "/report/fileDownload?filename="+uuid + "_" + fileName).put("size", Math.round(file.getSize()/1000)).put("filename", fileName).put("uuid", uuid);
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return R.error();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return R.error();
        }
    }
    
    /**
     * 实现文件下载
     * */
    @RequestMapping(value="file/fileDownload")
    public void fileDownload(@RequestParam("filename") String filename,HttpServletRequest request, HttpServletResponse response){
    	
    	FileInputStream fis = null;  
        OutputStream os = null;  
        try {  
        	System.out.println(filename);
            fis = new FileInputStream(this.fileRootPath + File.separator + filename);  
            os = response.getOutputStream();  
            int count = 0;  
            byte[] buffer = new byte[1024 * 8];  
            while ((count = fis.read(buffer)) != -1) {  
                os.write(buffer, 0, count);  
                os.flush();  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                fis.close();  
                os.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
    }
	
}
