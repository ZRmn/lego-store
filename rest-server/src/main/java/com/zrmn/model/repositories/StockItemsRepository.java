package com.zrmn.model.repositories;

import com.zrmn.model.entities.Product;
import com.zrmn.model.entities.StockItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemsRepository extends JpaRepository<StockItem, Long>
{
    List<StockItem> findAllByProduct(Product product);
}
