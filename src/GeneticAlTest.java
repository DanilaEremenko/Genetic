import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class GeneticAlTest {
    private List<Item> items;
    private GeneticAl geneticAl;

    @Before
    public void temps() {
        items = new ArrayList<>();
        items.add(new Item(8, 10));
        items.add(new Item(5, 12));
        items.add(new Item(6, 8));
        items.add(new Item(10, 15));
        items.add(new Item(4, 2));
        geneticAl = new GeneticAl(30, items,20);
    }

    @org.junit.Test
    public void fillKnapsackGenetic() throws Exception {
        for (int i = 0; i < 20; i++) {
            System.out.println(geneticAl.fillKnapsackGenetic());

        }

    }

}