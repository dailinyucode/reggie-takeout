package com.DLY.controller;

import com.DLY.common.R;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.UUID;

@RestController
@RequestMapping("/common")
@Slf4j
public class CommonController {

    @Value("${reggie.path}")
    private String bashPath;

    /**
     * multipartFile是一个临时接收的文件如果没有后续操作文件将会被系统删除
     * @param multipartFile
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public R upload(
       @RequestParam("file") MultipartFile multipartFile) throws IOException {
//        log.info("文件的长度+++{}",multipartFile.getBytes().length);
//        multipartFile.getOriginalFilename()  获取原始的文件名
        String originalFilename = multipartFile.getOriginalFilename();

//        使用uuid重写生成文件名，防止文件名重复  拼接后缀
//        originalFilename.substring(originalFilename.lastIndexOf("."));  获取后缀
        String newFileName=UUID.randomUUID().toString()+"."+originalFilename.split("\\.")[1];
//        log.info(bashPath+newFileName);
        multipartFile.transferTo(new File(bashPath+newFileName));

        //返回文件名称
        return R.success(newFileName);
    }

    @SneakyThrows
    @GetMapping("/download")
    public R download(HttpServletResponse response,
                      @RequestParam("name") String filename
    ) throws FileNotFoundException {
        String path=bashPath+filename;
        File file = new File(path);
        if(!file.exists()){
            return R.error("没有该文件");
        };
        //通果ImageIO工具类进行操作
//        BufferedImage read = ImageIO.read(file);
//        ImageIO.write(read,"JPGE",new FileOutputStream(new File("D://")));

        FileInputStream inputStream = new FileInputStream(file);
        ServletOutputStream outputStream = response.getOutputStream();

        //读取文件流
        int len=0;
        byte[] buffer = new byte[1024 * 4];
        while ((len=inputStream.read(buffer))!=-1){
            outputStream.write(buffer,0,len);
        }
        outputStream.flush();

        inputStream.close();
        outputStream.close();
        return R.success(null);
    }
}
