package com.leyou.service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * create by: cdy
 * description:$
 * create time: $ $
 * <p>
 * $
 *
 * @return $
 */
@Service
public class UploadService {
     private  static  final List<String> CONTENT_TYPE= Arrays.asList("image/jpeg","image/gif","image/png");
     private  static  final Logger LOGGER= LoggerFactory.getLogger(UploadService.class);
     @Autowired
     private FastFileStorageClient storageClient;
    public String upload(MultipartFile file) {
           String originalName=file.getOriginalFilename();
        String contentType = file.getContentType();
        if(!CONTENT_TYPE.contains(contentType)){
            LOGGER.info("文件类型不合法:{}",originalName);
            return  null;
        }
        try {
            BufferedImage read = ImageIO.read(file.getInputStream());
            if (read==null){
                LOGGER.info("文件内容不合法:{}",originalName);
                return  null;
            }
           //file.transferTo(new File("F:\\gitwork\\uploadFile\\"+originalName));
            String ext = StringUtils.substringAfterLast(originalName, ".");
            StorePath storePath=this.storageClient.uploadFile(file.getInputStream(),file.getSize(),ext,null);
            return  "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
