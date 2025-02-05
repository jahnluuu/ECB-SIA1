package com.ElectricityConsumptionBilling.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ElectricityConsumptionBilling.Entity.CustomerEntity;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Integer> {
    public CustomerEntity findbyCustomerId(Integer Id);
    public CustomerEntity findByEmail(String email);
}
