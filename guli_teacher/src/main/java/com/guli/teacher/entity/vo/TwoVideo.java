package com.guli.teacher.entity.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TwoVideo implements Serializable {

    private String id;

    private String title;

    private Integer isFree;

    private String videoSourceId;

    private String videoOriginalName;

}
