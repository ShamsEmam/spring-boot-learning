package repository;

import model.Lookup;
import java.util.List;

public interface LookupRepository {
    List<Lookup> findActiveByCategory(String categoryCode);
    boolean softDelete(String categoryCode, String itemCode);
    boolean existsActive(String categoryCode, String itemCode);
}