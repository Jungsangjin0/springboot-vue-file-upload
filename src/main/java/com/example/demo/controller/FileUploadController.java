package com.example.demo.controller;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.Response;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@Slf4j
@CrossOrigin(exposedHeaders = {"Content-Disposition"}, origins = "*")
public class FileUploadController {

    @Value("${file.dir}")
    private String path;

    private final Map<Integer, FileObj> map;

    private int count = 0;

    public FileUploadController() {
        map = new HashMap<>();
    }

    @PostMapping("/upload")
    public void upload(HttpServletRequest request, @RequestParam(required = false, value="files") List<MultipartFile> files, @RequestParam String name) throws IOException {
        log.info("files :  {}", files);
        log.info("name:  {}", name);
        System.out.println("path = " + path);
        System.out.println("======================================");
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("files.get(0).getContentType() = " + files.get(0).getContentType());
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
            obj.setOriginName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf(".")));
            obj.setSavedName(saveName);
            obj.setContentType(file.getContentType());
            obj.setExt(ext);
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

        // file 정보를 이용해서 실제 파일을 불러온다
        File target = new File(path + map.get(id).getSavedName());
        byte[] data = FileUtils.readFileToByteArray(target);

        //wrapping
        ByteArrayResource resource = new ByteArrayResource(data);
        //응답개체 생성
        ResponseEntity<ByteArrayResource> entity =
                ResponseEntity.ok()
                        .header("Content-Type", map.get(id).getContentType())
                        //기본적인 a태그로 저장할떄 filename은 확장자가 포함되어 있어야한다
                        // filename="파일이름.확장자" (o)
                        // filename="파일이름" (x)
                        .header("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(map.get(id).getOriginName() + map.get(id).getExt(), "UTF-8")+"\"")
                .body(resource);
        return entity;

    }

    @GetMapping("/v2/download/{id}")
    public ResponseEntity<Resource> downloadTest(@PathVariable int id) {

        return null;
    }

    @GetMapping("/getTest")
    public String test(HttpServletRequest request) {
        return "성공";
    }

    @PostMapping("/postTest")
    public String test2(HttpServletRequest request, @RequestBody A a) {
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("a = " + a);
        System.out.println("a.getTitle() = " + a.getTitle());
        System.out.println("a.getContent() = " + a.getContent());
        return "성공";
    }

    @PatchMapping("/patchTest")
    public String test3(HttpServletRequest request, @RequestBody A a) {
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("a = " + a);
        System.out.println("a.getTitle() = " + a.getTitle());
        System.out.println("a.getContent() = " + a.getContent());

        return null;
    }

    @DeleteMapping("/deleteTest")
    public String test4(HttpServletRequest request, @RequestBody A a, @ModelAttribute A b) {

        System.out.println("request = " + request.getContentType());
        System.out.println("a.getContent() = " + a.getContent());
        System.out.println("a.getTitle() = " + a.getTitle());


        System.out.println("b.getTitle() = " + b.getTitle());
        System.out.println("b.getContent() = " + b.getContent());
        return null;
    }
 }
