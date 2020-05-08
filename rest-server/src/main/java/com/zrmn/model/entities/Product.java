package com.zrmn.model.entities;

import com.zrmn.utils.SearchEngine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "products")
public class Product implements SearchEngine.Searchable
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String category;
    private String article;
    private String description;
    private Integer pieces;
    private BigDecimal price;
    private LocalDate releaseDate;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "image_urls")
    @Cascade(CascadeType.ALL)
    @Fetch(FetchMode.SELECT)
    @JoinColumn(name = "product_id")
    @Column(name = "image_url")
    private List<String> imageUrls;

    @Override
    public String keyWords()
    {
        return title + "\n"
                + category + "\n"
                + article;
    }
}
