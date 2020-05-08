package com.zrmn.model.forms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ProductForm
{
    private String title;
    private String category;
    private String article;
    private String description;
    private Integer pieces;
    private BigDecimal price;
    private LocalDate releaseDate;
    private List<MultipartFile> images;
}
