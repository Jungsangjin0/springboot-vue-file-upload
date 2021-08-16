package com.example.demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@Slf4j
public class FileUploadController {

    @Value("${file.dir}")
    private String path;

    private final Map<Integer, FileObj> map;

    private int count = 1;

    public FileUploadController() {
        map = new HashMap<>();
    }

    @PostMapping("/upload")
    public void upload(@RequestParam(required = false, value="files") List<MultipartFile> files, @RequestParam String name) throws IOException {
        log.info("files :  {}", files);
        log.info("name:  {}", name);
        System.out.println("path = " + path);

        String fullpath = null;


        for (MultipartFile file : files) {
            System.out.println("file.getContentType() = " + file.getContentType());
            System.out.println("file.getOriginalFilename() = " + file.getOriginalFilename());
            System.out.println("file.getSize() = " + file.getSize());
            System.out.println("file.getContentType() = " + file.getContentType());


            String randomName = UUID.randomUUID().toString().replace("-", "");
            String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String saveName = randomName + ext;
            
            FileObj obj = new FileObj();
            System.out.println("file.getOriginalFilename().substring(0, file.getOriginalFilename().las) = " + file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            obj.setOriginName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            obj.setSavedName(saveName);
            obj.setContentTpye(file.getContentType());
            map.put(count, obj);
            count++;

            fullpath = path + saveName;
            System.out.println("fullpath = " + fullpath);
            obj.setPath(fullpath);
            File mkdir = new File(fullpath);
            System.out.println("mkdir = " + mkdir);

            if(!mkdir.exists()) {
                mkdir.mkdirs();
            }

            try {
                file.transferTo(mkdir);
            } catch (IOException e) {
                new File(fullpath).delete();
                throw new IOException("파일 저장에 실패하였습니다");
            }
            System.out.println("map.get(id).getSavedName() = " + map.get(1).getSavedName());
            System.out.println("map.get(id).getOriginName() = " + map.get(1).getOriginName());
            System.out.println("map.get(0).getContentTpye() = " + map.get(1).getContentTpye());
            System.out.println("map.get(id).getPath() = " + map.get(1).getPath());
            System.out.println("count = " + count);
        }

    }

    @GetMapping("/file/list")
    public Map list() {
        log.info("map : {}", map);
        for (int s : map.keySet()) {
            System.out.println("map.get(s) = " + map.get(s));
        }
        return map;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> download(@PathVariable int id) throws IOException {
        System.out.println("====download======");
        System.out.println("id = " + id);
        System.out.println(map.get(id));
        System.out.println("map.get(1) = " + map.get(1));
        // file 정보를 이용해서 실제 파일을 불러온다
        System.out.println("map.get(id).getSavedName() = " + map.get(id).getSavedName());
        System.out.println("map.get(id).getOriginName() = " + map.get(id).getOriginName());
        System.out.println("map.get(0).getContentTpye() = " + map.get(id).getContentTpye());
        System.out.println("map.get(id).getPath() = " + map.get(id).getPath());
        File target = new File(path + map.get(id).getSavedName());
        byte[] data = FileUtils.readFileToByteArray(target);

        //wrapping
        ByteArrayResource resource = new ByteArrayResource(data);
        //응답개체 생성
        ResponseEntity<ByteArrayResource> entity =
                ResponseEntity.ok()
                        .header("Content-Type", map.get(id).getContentTpye())
                        .header("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(map.get(id).getOriginName(), "UTF-8")+"\"")
                .body(resource);
        return entity;
    }
}
