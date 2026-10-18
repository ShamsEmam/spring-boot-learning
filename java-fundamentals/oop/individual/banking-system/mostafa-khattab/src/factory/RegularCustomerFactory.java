
package factory;

import model.Customer;
import model.CustomerServicePolicy;
import model.RegularCustomerPolicy;

public class RegularCustomerFactory implements AbstractCustomerFactory {

    @Override
    public Customer createCustomer(String customerId, String name) {
        return new Customer(customerId, name);
    }

    @Override
    public CustomerServicePolicy createServicePolicy() {
        return new RegularCustomerPolicy();
    }
}
