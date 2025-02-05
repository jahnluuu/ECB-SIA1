package com.ElectricityConsumptionBilling.Backend.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ElectricityConsumptionBilling.Backend.DTO.Login;
import com.ElectricityConsumptionBilling.Backend.DTO.Signup;
import com.ElectricityConsumptionBilling.Backend.Entity.CustomerEntity;
import com.ElectricityConsumptionBilling.Backend.Service.CustomerService;

@RestController
@RequestMapping("/api/ElectricityConsumptionBilling/customer")
public class CustomerController {
    @Autowired
    CustomerService cserv;

    @GetMapping("/")
    public String index(){
        return "index.html";
    }

    @GetMapping("/print")
    public String print(){
        return "Welcome to Electricity Consumption BIlling";
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Signup signupRequest){
        cserv.registerCustomer(signupRequest);
        return ResponseEntity.ok("Signup Successful.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Login loginRequest){
        boolean isAuthenticated = cserv.loginCustomer(loginRequest);
        if(isAuthenticated) {
            return ResponseEntity.ok("Login Successful.");
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Email or Password.");
    }

    @GetMapping("/profile")
    public ResponseEntity<CustomerEntity> getCustomerProfile(@AuthenticationPrincipal CustomerEntity customerEntity){
        String email = customerEntity.getEmail();

        CustomerEntity customer = cserv.findByEmail(email);
        if (customer != null){
            return ResponseEntity.ok(customer);
        } else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/postcustomeraccount")
    public CustomerEntity postCustomerAccount(@RequestBody CustomerEntity customer){
        return cserv.postCustomerAccount(customer);
    }

    @GetMapping("/getAllCustomers")
    public List<CustomerEntity> getAllCustomers(){
        return cserv.getAllCustomers();
    }

    @PutMapping("/profile/edit/{id}")
    public ResponseEntity<CustomerEntity> editProfile(@PathVariable int id, @RequestBody CustomerEntity updatedProfile){
        CustomerEntity updatedCustomer = cserv.updateProfile(id, updatedProfile);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/deleteCustomerDetails/{id}")
    public String deleteOwners(@PathVariable int id){
        return cserv.deleteCustomer(id);
    }

    @PostMapping("/profile/uploadImage")
    public ResponseEntity<?> uploadCustomerImage (@RequestParam("customerId") int customerId, @RequestParam("image") MultipartFile file){
        try{
            CustomerEntity customer = cserv.findById(customerId);
            if(customer == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer Not Found.");
            }

            customer.setImage(file.getBytes());
            cserv.save(customer);
            return ResponseEntity.ok("Image Upload Successful.");
        } catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Uploading Image: " + e.getMessage());
        }
    }
    
    @GetMapping("/profile/image/{id}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable int customerId) {
        CustomerEntity customer = cserv.findById(customerId);
        if (customer == null || customer.getImage() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok()
                .header("Content-Type", "image/jpeg")
                .body(customer.getImage());
    }

}
