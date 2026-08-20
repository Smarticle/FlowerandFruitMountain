package com.cetc36.demo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * File请求
 *
 * @author liuyang
 */
@Getter
@Setter
@ToString
public class FileRequest {
    private Long id;
    private String name;
    private String type;
}