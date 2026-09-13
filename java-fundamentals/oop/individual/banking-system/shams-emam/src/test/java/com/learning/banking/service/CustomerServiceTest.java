package com.learning.banking.service;

import com.learning.banking.model.Customer;
import com.learning.banking.repository.CustomerRepository;
import com.learning.banking.repository.inmemory.InMemoryCustomerRepository;
import com.learning.banking.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceTest {

    private CustomerRepository customerRepository;
    private CustomerService customerService;
    private Customer customer;

    @BeforeEach
    void setUp() {

        customerRepository =
                new InMemoryCustomerRepository();

        customerService =
                new CustomerServiceImpl(customerRepository);

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );
    }

    @Test
    void registerCustomerShouldRegisterCustomerSuccessfully() {

        customerService.registerCustomer(customer);

        Customer foundCustomer =
                customerService.findCustomerById("CUST001");

        assertNotNull(foundCustomer);
        assertEquals(customer, foundCustomer);
    }

    @Test
    void registerCustomerShouldRejectNullCustomer() {

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.registerCustomer(null)
        );
    }

    @Test
    void registerCustomerShouldRejectDuplicateCustomerId() {

        Customer duplicateCustomer =
                new Customer(
                        "CUST001",
                        "Ahmed",
                        "ahmed@gmail.com",
                        "123456"
                );

        customerService.registerCustomer(customer);

        assertThrows(
                IllegalArgumentException.class,
                () -> customerService.registerCustomer(
                        duplicateCustomer
                )
        );
    }

    @Test
    void findCustomerByIdShouldReturnCustomer() {

        customerService.registerCustomer(customer);

        Customer foundCustomer =
                customerService.findCustomerById("CUST001");

        assertEquals(customer, foundCustomer);
    }

    @Test
    void findCustomerByIdShouldReturnNullWhenCustomerDoesNotExist() {

        Customer foundCustomer =
                customerService.findCustomerById("CUST999");

        assertNull(foundCustomer);
    }

    @Test
    void constructorShouldRejectNullRepository() {

        assertThrows(
                IllegalArgumentException.class,
                () -> new CustomerServiceImpl(null)
        );
    }
}