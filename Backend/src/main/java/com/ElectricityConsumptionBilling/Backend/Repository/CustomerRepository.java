package com.ElectricityConsumptionBilling.Backend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ElectricityConsumptionBilling.Backend.Entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    public CustomerEntity findByCustomerId(Integer Id);
    public CustomerEntity findByEmail(String email);
}
