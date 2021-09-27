package com.study.jaehun.entity;

import lombok.*;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

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
    @Field
    private String title;
    @Column(unique = true)
    private String url;
    @Lob
    @Field
    private String description;
    private String price;
    private Integer mcnt;
}
