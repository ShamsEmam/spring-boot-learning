import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryCustomerRepository implements CustomerRepository {
    private final Map<String, Customer> customers = new HashMap<>();

    @Override
    public Customer findById(String customerId) {
        return customers.get(customerId);
    }

    @Override
    public boolean save(Customer customer) {
        if (customer == null || customers.containsKey(customer.getCustomerId())) {
            return false;
        }
        customers.put(customer.getCustomerId(), customer);
        return true;
    }

    @Override
    public boolean exists(String customerId) {
        return customers.containsKey(customerId);
    }

    @Override
    public List<Customer> findAll() {
        return new ArrayList<>(customers.values());
    }
}