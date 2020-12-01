package com.diegogarrido.imagemanager.util;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;


public class ImageManager {

    @Value("${spring.application.image-folder}")
    private static String filePath = "./images/";

    public static String saveImage(MultipartFile image, String name) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.mkdir();
        }
        BufferedImage src = ImageIO.read(new ByteArrayInputStream(image.getBytes()));
        File destination = new File(filePath + name);
        ImageIO.write(src, name.split("\\.")[1], destination);
        src.flush();
        return filePath + name;
    }

    public static HttpServletResponse getImage(HttpServletResponse response, String fileName) throws IOException {
        InputStream is = new BufferedInputStream(new FileInputStream(filePath + fileName));
        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
        is.close();
        return response;
    }

    public static boolean deleteImage(String name) {
        File file = new File(filePath + name);
        return file.delete();
    }

    public static boolean imageExists(String name){
        File file = new File(filePath + name);
        return file.exists();
    }
}
