package com.zrmn.model.services;

import com.zrmn.model.entities.Product;
import com.zrmn.model.entities.StockItem;
import com.zrmn.model.entities.Warehouse;
import com.zrmn.model.exceptions.BadRequestException;
import com.zrmn.model.exceptions.NotFoundException;

import java.util.List;

public interface StockService
{
    List<StockItem> getAll();

    List<StockItem> getAll(Warehouse warehouse);

    StockItem get(Long id) throws NotFoundException;

    StockItem get(Warehouse warehouse, Long id) throws NotFoundException;

    void save(Warehouse warehouse, StockItem stockItem);

    void delete(Long id) throws NotFoundException;

    void deleteAll();

    void deleteAll(Warehouse warehouse);

    void update(StockItem stockItem) throws NotFoundException;

    StockItem addToStock(Warehouse warehouse, Product product, int quantity);

    void writeOff(List<Warehouse> writeOffMap) throws NotFoundException, BadRequestException;

    int getAvailability(Product product);
}
