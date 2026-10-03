package com.learning.banking.repository.inmemory;

import com.learning.banking.model.Customer;
import com.learning.banking.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryCustomerRepositoryTest {

    private CustomerRepository customerRepository;
    private Customer customer;

    @BeforeEach
    void setUp() {

        customerRepository =
                new InMemoryCustomerRepository();

        customer = new Customer(
                "CUST001",
                "Shams",
                "shams@gmail.com",
                "123456"
        );
    }

    @Test
    void saveShouldSaveCustomerSuccessfully() {

        customerRepository.save(customer);

        Customer foundCustomer =
                customerRepository.findById("CUST001");

        assertEquals(customer, foundCustomer);
    }

    @Test
    void saveShouldRejectNullCustomer() {

        assertThrows(
                IllegalArgumentException.class,
                () -> customerRepository.save(null)
        );
    }

    @Test
    void saveShouldRejectDuplicateCustomerId() {

        Customer duplicateCustomer =
                new Customer(
                        "CUST001",
                        "Ahmed",
                        "ahmed@gmail.com",
                        "123456"
                );

        customerRepository.save(customer);

        assertThrows(
                IllegalArgumentException.class,
                () -> customerRepository.save(
                        duplicateCustomer
                )
        );
    }

    @Test
    void findByIdShouldReturnExistingCustomer() {

        customerRepository.save(customer);

        Customer foundCustomer =
                customerRepository.findById("CUST001");

        assertNotNull(foundCustomer);
        assertEquals(customer, foundCustomer);
    }

    @Test
    void findByIdShouldReturnNullWhenCustomerDoesNotExist() {

        Customer foundCustomer =
                customerRepository.findById("CUST999");

        assertNull(foundCustomer);
    }

    @Test
    void findByIdShouldRejectNullId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> customerRepository.findById(null)
        );
    }

    @Test
    void findByIdShouldRejectBlankId() {

        assertThrows(
                IllegalArgumentException.class,
                () -> customerRepository.findById("   ")
        );
    }

    @Test
    void existsByIdShouldReturnTrueWhenCustomerExists() {

        customerRepository.save(customer);

        assertTrue(
                customerRepository.existsById("CUST001")
        );
    }

    @Test
    void existsByIdShouldReturnFalseWhenCustomerDoesNotExist() {

        assertFalse(
                customerRepository.existsById("CUST999")
        );
    }

    @Test
    void findAllShouldReturnAllCustomers() {

        Customer secondCustomer =
                new Customer(
                        "CUST002",
                        "Ahmed",
                        "ahmed@gmail.com",
                        "123456"
                );

        customerRepository.save(customer);
        customerRepository.save(secondCustomer);

        List<Customer> customers =
                customerRepository.findAll();

        assertEquals(2, customers.size());
        assertTrue(customers.contains(customer));
        assertTrue(customers.contains(secondCustomer));
    }

    @Test
    void findAllShouldReturnEmptyListWhenNoCustomersExist() {

        List<Customer> customers =
                customerRepository.findAll();

        assertNotNull(customers);
        assertTrue(customers.isEmpty());
    }

    @Test
    void findAllShouldReturnReadOnlyList() {

        customerRepository.save(customer);

        List<Customer> customers =
                customerRepository.findAll();

        assertThrows(
                UnsupportedOperationException.class,
                () -> customers.clear()
        );

        assertEquals(
                1,
                customerRepository.findAll().size()
        );
    }
}