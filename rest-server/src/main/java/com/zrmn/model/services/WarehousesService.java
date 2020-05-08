package com.zrmn.model.services;

import com.zrmn.model.entities.Order;
import com.zrmn.model.entities.Warehouse;
import com.zrmn.model.exceptions.NotFoundException;

import java.util.List;

public interface WarehousesService
{
    List<Warehouse> getAll();

    Warehouse get(Long id) throws NotFoundException;

    void save(Warehouse warehouse);

    void delete(Long id) throws NotFoundException;

    void deleteAll();

    void update(Warehouse warehouse) throws NotFoundException;

    List<Warehouse> collect(Order order);
}
