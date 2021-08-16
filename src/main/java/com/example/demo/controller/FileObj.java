package com.example.demo.controller;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileObj {

    private String originName;
    private String savedName;
    private String path;
    private String contentTpye;
}
