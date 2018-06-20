package application.warehouse.repository;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.cql.CqlTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ItemsRepository {

    Logger logger = LoggerFactory.getLogger(ItemsRepository.class);

    private CqlTemplate cqlOperations;

    private final static String ITEMS_TABLE = "ITEMS";
    private final static String ITEMS_COUNT_TABLE = "ITEMS_COUNT";

    public ItemsRepository(@Autowired CqlTemplate cqlOperations) {
        this.cqlOperations = cqlOperations;
    }

    public void createItemsTable(boolean drop) {
        if (drop) {
            cqlOperations.execute("DROP TABLE IF EXISTS " + ITEMS_TABLE);
            cqlOperations.execute("DROP TABLE IF EXISTS " + ITEMS_COUNT_TABLE);

        }
        cqlOperations.execute("CREATE TABLE " + ITEMS_TABLE + " (id int primary key, name text)");
        cqlOperations.execute("CREATE TABLE " + ITEMS_COUNT_TABLE + " (id int primary key, stock counter)");
    }

    public Long getNumItems() {
        Map<String, Object> map = cqlOperations.queryForMap("SELECT COUNT(*) FROM " + ITEMS_TABLE);
        return (Long) map.get("count");
    }

    public int addItem(int uuid, String itemName, int initVal) {
        cqlOperations.execute("INSERT INTO " + ITEMS_TABLE + " (id, name) " + "VALUES (?, ?)", uuid, itemName);
        cqlOperations
                .execute("UPDATE " + ITEMS_COUNT_TABLE + " SET stock = stock + " + initVal + " where id = " + uuid);
        return uuid;
    }

    public void decrement(int uuid, int amount) {
        // logger.info(String.format("DECREMENTING COUNTER WITH ID: %s", uuid));

        // Conditions not supported on counters
        cqlOperations.execute("UPDATE " + ITEMS_COUNT_TABLE + " SET stock = stock - " + amount + " WHERE id = " + uuid);
    }

    public void increment(int uuid, int amount) {
        // logger.info(String.format("DECREMENTING COUNTER WITH ID: %s", uuid));

        // Conditions not supported on counters
        cqlOperations.execute("UPDATE " + ITEMS_COUNT_TABLE + " SET stock = stock + " + amount + " WHERE id = " + uuid);
    }

    public Long getStock(int i) {
        Map<String, Object> map = cqlOperations
                .queryForMap("SELECT stock FROM " + ITEMS_COUNT_TABLE + " WHERE id = " + i);
        return (Long) map.get("stock");
    }

    public List<Map<String, Object>> getAllStock() {
        return cqlOperations.queryForList("SELECT id, stock FROM " + ITEMS_COUNT_TABLE);

    }

}
