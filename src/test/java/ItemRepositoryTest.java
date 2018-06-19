import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import application.Main;
import application.warehouse.repository.ItemsRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Main.class)
public class ItemRepositoryTest {

	private @Autowired ItemsRepository repository;

	@Test
	public void initTableTest() {
		repository.createItemsTable(true);
		assertEquals(0, (long) repository.getNumItems());
	}

	@Test
	public void createItemTest() {
		repository.createItemsTable(true);
		repository.addItem(1, "demo", 10);
		assertEquals(1, (long) repository.getNumItems());
	}

	@Test
	public void decrementItemTest() {
		repository.createItemsTable(true);
		repository.addItem(1, "demo", 10);
		repository.decrement(1, 1);
		assertEquals(9, (long) repository.getStock(1));
	}

}
