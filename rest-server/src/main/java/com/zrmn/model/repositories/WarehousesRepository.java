package com.zrmn.model.repositories;

import com.zrmn.model.entities.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehousesRepository extends JpaRepository<Warehouse, Long>
{
}
