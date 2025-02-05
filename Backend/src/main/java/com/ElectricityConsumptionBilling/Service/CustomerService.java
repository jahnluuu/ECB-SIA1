package com.ElectricityConsumptionBilling.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ElectricityConsumptionBilling.DTO.Login;
import com.ElectricityConsumptionBilling.DTO.Signup;
import com.ElectricityConsumptionBilling.Entity.CustomerEntity;
import com.ElectricityConsumptionBilling.Repository.CustomerRepository;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository crepo;

    public CustomerService(){
        super();
    }

    public CustomerEntity postCustomerAccount(CustomerEntity customer){
        if(customer.getRole() != null){
            customer.setRole("CUSTOMER");
        }
        return crepo.save(customer);
    }

    public List<CustomerEntity> getAllCustomers(){
        return crepo.findAll();
    }

    public CustomerEntity updateProfile(int id, CustomerEntity updatedProfile) {
        CustomerEntity customer = crepo.findbyCustomerId(id);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found");
        }

        customer.setFname(updatedProfile.getFname());
        customer.setLname(updatedProfile.getLname());
        customer.setAddress(updatedProfile.getAddress());
        customer.setEmail(updatedProfile.getEmail());
        customer.setPassword(updatedProfile.getPassword());
        customer.setPhoneNumber(updatedProfile.getPhoneNumber());

        if (updatedProfile.getImage() != null) {
            customer.setImage(updatedProfile.getImage());
        }

        return crepo.save(customer);
    }

    public String deleteCustomer(int id){
        Optional<CustomerEntity> customerOptional = crepo.findById(id);
        if (customerOptional.isPresent()){
            CustomerEntity customer = customerOptional.get();
            crepo.delete(customer);
            return "Customer record deleted successfully";
        } else{
            return id + " Not Found";
        }
    }

    public CustomerEntity registerCustomer(Signup signupRequest) {
        CustomerEntity customer = new CustomerEntity();
        customer.setFname(signupRequest.getFname());
        customer.setLname(signupRequest.getLname());
        customer.setAddress(signupRequest.getAddress());
        customer.setEmail(signupRequest.getEmail());
        customer.setPassword(signupRequest.getPassword());
        customer.setPhoneNumber(signupRequest.getPhoneNumber());
        return crepo.save(customer);
    }

    public boolean loginCustomer(Login loginRequest) {
        CustomerEntity customer = crepo.findByEmail(loginRequest.getEmail());
        return customer != null && loginRequest.getPassword().equals(customer.getPassword());
    }

    public CustomerEntity findByEmail(String email) {
        return crepo.findByEmail(email);
    }

    public CustomerEntity findById(int customerId) {
        return crepo.findById(customerId).orElse(null);
    }

    public CustomerEntity save(CustomerEntity customer){
        return crepo.save(customer);
    }

}
