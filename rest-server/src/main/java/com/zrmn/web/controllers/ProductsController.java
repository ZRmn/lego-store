package com.zrmn.web.controllers;

import com.zrmn.model.entities.Product;
import com.zrmn.model.services.ProductsService;
import com.zrmn.model.services.StockService;
import com.zrmn.model.transfer.ProductDto;
import com.zrmn.utils.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/products")
public class ProductsController
{
    @Autowired
    private ProductsService productsService;

    @Autowired
    private StockService stockService;

    private Comparator<Product> getComparator(String order, String sortField)
    {
        Comparator<Product> comparator;

        switch(sortField)
        {
            case "price":
                comparator = Comparator.comparing(Product::getPrice);
                break;
            case "releaseDate":
                comparator = Comparator.comparing(Product::getReleaseDate);
                break;
            case "piecePrice":
                comparator = Comparator.comparing(product -> product.getPrice().divide(
                        BigDecimal.valueOf(product.getPieces()), RoundingMode.HALF_UP));
                break;
            default:
                comparator = Comparator.comparing(Product::getTitle);
        }

        if(order.equals("desc"))
        {
            comparator = comparator.reversed();
        }

        return comparator;
    }

    private Predicate<Product> parsePredicate(String predicateName, String param)
    {
        switch (predicateName)
        {
            case "category":
            {
                return product -> product.getCategory().equals(param);
            }
            case "priceFrom":
            {
                BigDecimal price = new BigDecimal(param);
                return product -> product.getPrice().compareTo(price) >= 0;
            }
            case "priceTo":
            {
                BigDecimal price = new BigDecimal(param);
                return product -> product.getPrice().compareTo(price) <= 0;
            }
            case "piecesFrom":
            {
                Integer pieces = Integer.valueOf(param);
                return product -> product.getPieces() >= pieces;
            }
            case "piecesTo":
            {
                int pieces = Integer.parseInt(param);
                return product -> product.getPieces() <= pieces;
            }
            case "yearFrom":
            {
                int year = Integer.parseInt(param);
                return product -> product.getReleaseDate().getYear() >= year;
            }
            case "yearTo":
            {
                int year = Integer.parseInt(param);
                return product -> product.getReleaseDate().getYear() <= year;
            }
        }

        return null;
    }

    private void addPredicateIfNotNull(List<Predicate<Product>> predicates, String predicateName, String param)
    {
        if(param != null)
        {
            predicates.add(parsePredicate(predicateName, param));
        }
    }

    @GetMapping
    public ResponseEntity getProducts(@RequestParam Map<String, String> params)
    {
        String query = params.get("query");
        String order = params.get("order");
        String sortField = params.get("sortField");

        String category = params.get("category");
        String priceFrom = params.get("priceFrom");
        String priceTo = params.get("priceTo");
        String piecesFrom = params.get("piecesFrom");
        String piecesTo = params.get("piecesTo");
        String yearFrom = params.get("yearFrom");
        String yearTo = params.get("yearTo");

        List<Predicate<Product>> predicates = new ArrayList<>();
        addPredicateIfNotNull(predicates, "category", category);
        addPredicateIfNotNull(predicates, "priceFrom", priceFrom);
        addPredicateIfNotNull(predicates, "priceTo", priceTo);
        addPredicateIfNotNull(predicates, "piecesFrom", piecesFrom);
        addPredicateIfNotNull(predicates, "piecesTo", piecesTo);
        addPredicateIfNotNull(predicates, "yearFrom", yearFrom);
        addPredicateIfNotNull(predicates, "yearTo", yearTo);

        SearchEngine.Searcher<Product> searcher = productsService.searcher();

        if(query != null)
        {
            if(!predicates.isEmpty())
            {
                searcher.filterAndFind(query, predicates);
            }
            else
            {
                searcher.find(query);
            }
        }

        if(!predicates.isEmpty())
        {
            searcher.filter(predicates);
        }

        if(order != null && sortField != null)
        {
            searcher.sort(getComparator(order, sortField));
        }

        List<ProductDto> products = searcher.getSearchResults().stream()
                .map(ProductDto::from)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProduct(@PathVariable Long id)
    {
        Product product = productsService.get(id);
        int availability = stockService.getAvailability(product);

        Map<String, Object> map = new HashMap<>();
        map.put("product", product);
        map.put("availability", availability);

        return map;
    }
}
