package com.study.jaehun.entity;

import lombok.*;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;


import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Indexed
@Entity
public class WebPage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;
    @FullTextField(analyzer = "english")
    private String title;
    @Column(unique = true)
    private String url;
    @Lob
    @FullTextField(analyzer = "english")
    private String description;
    private String price;
    private Integer mcnt;
}
