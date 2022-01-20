package com.guli.teacher.entity.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class OneChapter implements Serializable {

    private String id;

    private String title;

    private List<TwoVideo> children = new ArrayList<>();
}
