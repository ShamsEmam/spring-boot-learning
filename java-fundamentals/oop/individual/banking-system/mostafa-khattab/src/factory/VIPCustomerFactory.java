
package factory;

import model.Customer;
import model.CustomerServicePolicy;
import model.VIPCustomerPolicy;

public class VIPCustomerFactory implements AbstractCustomerFactory {

    @Override
    public Customer createCustomer(String customerId, String name) {
        return new Customer(customerId, name);
    }

    @Override
    public CustomerServicePolicy createServicePolicy() {
        return new VIPCustomerPolicy();
    }
}
