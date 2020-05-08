package com.zrmn.model.services;

import com.zrmn.model.entities.Product;
import com.zrmn.model.entities.StockItem;
import com.zrmn.model.entities.Warehouse;
import com.zrmn.model.exceptions.BadRequestException;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.repositories.StockItemsRepository;
import com.zrmn.model.repositories.WarehousesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
public class StockServiceImpl implements StockService
{
    @Autowired
    private StockItemsRepository stockItemsRepository;

    @Autowired
    private WarehousesRepository warehousesRepository;

    @Override
    public List<StockItem> getAll()
    {
        return stockItemsRepository.findAll();
    }

    @Override
    public List<StockItem> getAll(Warehouse warehouse)
    {
        return warehouse.getStockItems();
    }

    @Override
    public StockItem get(Long id) throws NotFoundException
    {
        return stockItemsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such stock item"));
    }

    @Override
    public StockItem get(Warehouse warehouse, Long id) throws NotFoundException
    {
        return warehouse.getStockItems().stream()
                .filter(stockItem -> stockItem.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Warehouse has no such stock item"));
    }

    @Override
    public void save(Warehouse warehouse, StockItem stockItem)
    {
        stockItemsRepository.save(stockItem);
        warehouse.getStockItems().add(stockItem);
        warehousesRepository.save(warehouse);
    }

    @Override
    public void delete(Long id) throws NotFoundException
    {
        stockItemsRepository.delete(get(id));
    }

    @Override
    public void deleteAll()
    {
        stockItemsRepository.deleteAll();
    }

    @Override
    public void deleteAll(Warehouse warehouse)
    {
        warehouse.getStockItems().forEach(stockItem -> stockItemsRepository.delete(stockItem));
        warehouse.getStockItems().clear();
    }

    @Override
    public void update(StockItem stockItem) throws NotFoundException
    {
        get(stockItem.getId());
        stockItemsRepository.save(stockItem);
    }

    @Override
    public StockItem addToStock(Warehouse warehouse, Product product, int quantity)
    {
        Optional<StockItem> stockItemCandidate = warehouse.getStockItems().stream()
                .filter(stockItem -> stockItem.getProduct().getId().equals(product.getId()))
                .findFirst();

        StockItem stockItem;

        if(stockItemCandidate.isPresent())
        {
            stockItem = stockItemCandidate.get();
            stockItem.setQuantity(stockItem.getQuantity() + quantity);
        }
        else
        {
            stockItem = new StockItem(null, product, quantity);
            warehouse.getStockItems().add(stockItem);
        }

        save(warehouse, stockItem);

        return stockItem;
    }

    @Override
    public void writeOff(List<Warehouse> writeOffMap) throws NotFoundException
    {
        boolean availability = writeOffMap.stream()
                .noneMatch(warehouse -> warehouse.getId() == null);

        if(!availability)
        {
            throw new BadRequestException("Some products not available");
        }

        writeOffMap.forEach(warehouse -> {
            warehouse.getStockItems().forEach(stockItemForWriteOff -> {
                StockItem stockItem = stockItemsRepository.findById(stockItemForWriteOff.getId())
                        .orElseThrow(() -> new NotFoundException("No such stock item"));

                stockItem.setQuantity(stockItem.getQuantity() - stockItemForWriteOff.getQuantity());
                update(stockItem);
            });
        });
    }

    @Override
    public int getAvailability(Product product)
    {
        List<StockItem> stockItems = stockItemsRepository.findAllByProduct(product);

        return stockItems.stream()
                .map(StockItem::getQuantity)
                .reduce(0, Integer::sum);
    }
}
