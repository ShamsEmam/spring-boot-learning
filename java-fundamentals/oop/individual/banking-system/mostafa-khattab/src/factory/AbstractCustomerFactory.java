
package factory;

import model.Customer;
import model.CustomerServicePolicy;

public interface AbstractCustomerFactory {

    Customer createCustomer(String customerId, String name);

    CustomerServicePolicy createServicePolicy();
}
