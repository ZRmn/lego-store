package com.zrmn.model.services;

import com.zrmn.model.entities.Order;
import com.zrmn.model.entities.OrderItem;
import com.zrmn.model.entities.StockItem;
import com.zrmn.model.entities.Warehouse;
import com.zrmn.model.exceptions.NotFoundException;
import com.zrmn.model.repositories.StockItemsRepository;
import com.zrmn.model.repositories.WarehousesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class WarehousesServiceImpl implements WarehousesService
{
    @Autowired
    private WarehousesRepository warehousesRepository;

    @Autowired
    private StockItemsRepository stockItemsRepository;

    @Override
    public List<Warehouse> getAll()
    {
        return warehousesRepository.findAll();
    }

    @Override
    public Warehouse get(Long id) throws NotFoundException
    {
        return warehousesRepository.findById(id).orElseThrow(() -> new NotFoundException("No such warehouse"));
    }

    @Override
    public void save(Warehouse warehouse)
    {
        warehousesRepository.save(warehouse);
    }

    @Override
    public void delete(Long id) throws NotFoundException
    {
        warehousesRepository.delete(get(id));
    }

    @Override
    public void deleteAll()
    {
        warehousesRepository.deleteAll();
    }

    @Override
    public void update(Warehouse warehouse) throws NotFoundException
    {
        get(warehouse.getId());
        warehousesRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> collect(Order order)
    {
        List<Warehouse> warehouses = warehousesRepository.findAll();
        warehouses.forEach(warehouse ->
        {
            warehouse.getStockItems().removeIf(
                    stockItem -> stockItem.getQuantity() < 1 || order.getOrderItems().stream().noneMatch(
                            orderItem -> orderItem.getProduct().getId().equals(stockItem.getProduct().getId())));
        });

        Map<Long, Warehouse> collectMap = new HashMap<>();

        for(OrderItem orderItem : order.getOrderItems())
        {
            int quantity = orderItem.getQuantity();

            for (Warehouse warehouse : warehouses)
            {
                if (quantity < 1)
                {
                    break;
                }

                Optional<StockItem> stockItemCandidate = warehouse.getStockItems().stream()
                        .filter(item -> item.getProduct().getId().equals(orderItem.getProduct().getId()))
                        .findFirst();

                if(!stockItemCandidate.isPresent())
                {
                    break;
                }

                StockItem stockItem = stockItemCandidate.get();

                if (quantity - stockItem.getQuantity() < 0)
                {
                    stockItem.setQuantity(quantity);
                }

                Warehouse mappedWarehouse = collectMap.get(warehouse.getId());

                if (mappedWarehouse == null)
                {
                    mappedWarehouse =  Warehouse.builder()
                                    .id(warehouse.getId())
                                    .address(warehouse.getAddress())
                                    .stockItems(new ArrayList<>())
                                    .build();

                    collectMap.put(warehouse.getId(), mappedWarehouse);
                }

                mappedWarehouse.getStockItems().add(stockItem);
                quantity -= stockItem.getQuantity();
            }

            if(quantity > 0)
            {
                Warehouse outOfStockWarehouse = collectMap.get(null);

                if (outOfStockWarehouse == null)
                {
                    outOfStockWarehouse =  Warehouse.builder()
                            .id(null)
                            .address("Out of stock")
                            .stockItems(new ArrayList<>())
                            .build();

                    collectMap.put(null, outOfStockWarehouse);
                }

                StockItem outOfStock = new StockItem(null, orderItem.getProduct(), quantity);
                outOfStockWarehouse.getStockItems().add(outOfStock);
            }
        }

        return new ArrayList<>(collectMap.values());
    }
}
