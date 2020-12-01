package com.diegogarrido.imagemanager.controller;

import com.diegogarrido.imagemanager.util.ImageManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/image")
public class ImageController {

    @GetMapping()
    public void getImage(@RequestParam String name, HttpServletResponse response) {
        try {
            response = ImageManager.getImage(response, name);
        } catch (IOException ex) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading image");
        }
    }

    @PostMapping()
    public void postImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Empty file");
        }
        if(file.getOriginalFilename().split("\\.").length != 2){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid file name");
        }
        try {
            ImageManager.saveImage(file, file.getOriginalFilename().replace(" ",""));
        } catch (Exception ex) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving image");
        }
    }

    @DeleteMapping()
    public void deleteImage(@RequestParam String name){
        if(!ImageManager.imageExists(name)){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Image does not exists");
        }
        if(!ImageManager.deleteImage(name)){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting image");
        }
    }

}
