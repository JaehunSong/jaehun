package com.study.jaehun.seedsite;

import lombok.*;
import org.jsoup.nodes.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tresbien {
    private String title;
    private String price;
    private String img;
    private String description;
    private String detail;
    private String brand;


    public Tresbien(Document doc) {
        title = doc.title();
        if (doc.getElementsByTag("span").hasClass("price")){
            price = doc.getElementsByTag("span").select(".price").get(0).text();
        }else price = null;
        if (doc.getElementsByTag("img").hasClass("pdp__media-grid-item-image")){
            img = doc.getElementsByTag("img").select(".pdp__media-grid-item-image").attr("src");
        }else img = null;
        if (doc.getElementsByTag("div").hasClass("read-more__content")){
            description = doc.getElementsByTag("div").select(".read-more__content").text();
        }else description = null;
        if (doc.getElementsByTag("div").hasClass("value")){
            detail = doc.getElementsByTag("div").select(".value").text();
        }else detail = null;
        if (doc.getElementsByTag("div").hasClass("pdp__more-from-brand")){
            brand = doc.getElementsByTag("div").select("div.pdp__more-from-brand a").text();
        }else brand = null;
    }
}
