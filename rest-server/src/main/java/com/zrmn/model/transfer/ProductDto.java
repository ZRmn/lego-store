package com.zrmn.model.transfer;

import com.zrmn.model.entities.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductDto
{
    private Long id;
    private String title;
    private String category;
    private String article;
    private Integer pieces;
    private BigDecimal price;
    private LocalDate releaseDate;
    private String imageUrl;

    public static ProductDto from(Product product)
    {
        String imageUrl = null;

        if(!product.getImageUrls().isEmpty())
        {
            imageUrl = product.getImageUrls().get(0);
        }

        return ProductDto.builder()
                .id(product.getId())
                .title(product.getTitle())
                .article(product.getArticle())
                .category(product.getCategory())
                .pieces(product.getPieces())
                .price(product.getPrice())
                .releaseDate(product.getReleaseDate())
                .imageUrl(imageUrl)
                .build();
    }
}
