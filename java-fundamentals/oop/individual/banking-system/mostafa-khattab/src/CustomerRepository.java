import java.util.List;

public interface CustomerRepository {
    Customer findById(String customerId);
    boolean save(Customer customer);
    boolean exists(String customerId);
    List<Customer> findAll();
}