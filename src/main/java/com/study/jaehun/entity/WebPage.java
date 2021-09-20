package com.study.jaehun.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class WebPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    private String title;

    private String url;
    @Lob
    private String description;

    private Integer mcnt;
}
