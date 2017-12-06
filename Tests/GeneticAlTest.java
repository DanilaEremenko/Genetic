import Genetic.Algs;
import Genetic.Fill;
import Genetic.GeneticAl;
import Genetic.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.*;


public class GeneticAlTest {
    Random random = new Random();
    private List<Item> items = new ArrayList<>();
    private Algs algs = new Algs();
    private GeneticAl geneticAl = new GeneticAl(10, 10, 100, 10);
    private int smallItemSize = 10;
    private int normalItemSize = 100;
    private int bigItemSize = 100000;
    int maxLoad = 1000;

    @Before
    public void terms() throws Exception {
        items = new ArrayList<>();
        for (int i = 0; i < bigItemSize; i++)
            items.add(new Item(1 + random.nextInt(10000), 300 + random.nextInt(600)));


    }


    @Test
    public void fillKnapsackGenetic() throws Exception {
        Fill fillGenetic = geneticAl.fillKnapsackGenetic(maxLoad, items);
        System.out.println("Генетический набрал= " + fillGenetic.getCost());
        int weight = 0;
        for (Item item : fillGenetic.getItems())
            weight += item.getWeight();
        System.out.println("Вес" + weight);
    }

    @Test
    public void fillKnapsackGreedy() throws Exception {
        try {
        Fill fillGreedy = algs.fillKnapsackGreedy(maxLoad, items);
        System.out.println("Жадный набрал = " + fillGreedy.getCost());
        int weight = 0;
        for (Item item : fillGreedy.getItems())
            weight += item.getWeight();
        System.out.println("Вес" + weight);
        } catch (StackOverflowError e) {
            System.out.println("Жадный выбыл");
        }

    }

    @Test
    public void fillKnapsackDynamic() throws Exception {
        try {
            Fill fillDynamic = algs.fillKnapsackDynamic(maxLoad, items, new HashMap<>());
            System.out.println("Динамический набрал= " + fillDynamic.getCost());
        } catch (StackOverflowError e) {
            System.out.println("Динамический выбыл");
        }
    }


}