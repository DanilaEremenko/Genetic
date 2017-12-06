import Genetic.Algs;
import Genetic.Fill;
import Genetic.GeneticAl;
import Genetic.Item;
import org.junit.Before;

import static org.junit.Assert.*;

import java.util.*;


public class GeneticAlTest {
    Random random = new Random();
    private List<Item> items;
    private Algs algs = new Algs();
    private GeneticAl geneticAl = new GeneticAl(10, 10, 1000, 20);
    private int itemsSize = 200;
    int maxLoad = 1000;

    @Before
    public void temps() {
        items = new ArrayList<>();
        for (int i = 0; i < itemsSize; i++)
            items.add(new Item(1 + random.nextInt(10000), 1 + random.nextInt(1000                                       )));



    }

    @org.junit.Test
    public void fillKnapsackGenetic() throws Exception {
        int betterOrEqualsDynamic = 0;
        int betterOrEqualsGreedy = 0;
        int reload = 100;

        long startTime = System.currentTimeMillis();
        Fill fillGenetic = geneticAl.fillKnapsackGenetic(maxLoad, items);
        long endTime = System.currentTimeMillis();
        System.out.println("Время исполнения генетиеского: " + (endTime - startTime) + "мс");

        startTime = System.currentTimeMillis();
        Fill fillDynamic = algs.fillKnapsackDynamic(maxLoad, items, new HashMap<>());
        endTime = System.currentTimeMillis();
        System.out.println("Время исполнения динамического: " + (endTime - startTime) + "мс");

        startTime = System.currentTimeMillis();
        Fill fillGreedy = algs.fillKnapsackGreedy(maxLoad, items);
        endTime = System.currentTimeMillis();
        System.out.println("Время исполнения жадного: " + (endTime - startTime) + "мс");


        long geneticCost = 0;
        long dynamicCost = 0;
        long greedyCost = 0;
        for (int i = 0; i < reload; i++) {
            fillGenetic = geneticAl.fillKnapsackGenetic(maxLoad, items);
            geneticCost += fillGenetic.getCost();
            fillDynamic = algs.fillKnapsackDynamic(maxLoad, items, new HashMap<>());
            dynamicCost += fillDynamic.getCost();
            fillGreedy = algs.fillKnapsackGreedy(maxLoad, items);
            greedyCost += fillGreedy.getCost();
            if (fillGenetic.getCost() >= fillDynamic.getCost())
                betterOrEqualsDynamic++;
            if (fillGenetic.getCost() >= fillGreedy.getCost())
                betterOrEqualsGreedy++;
        }
        System.out.println(betterOrEqualsDynamic);
        System.out.println(betterOrEqualsGreedy);
        System.out.println("Последний динамический = " + fillDynamic.getCost());
        System.out.println("Средняя ценность динамического = " + dynamicCost / reload);
        System.out.println("Последний жадный = " + fillGreedy.getCost());
        System.out.println("Средняя ценность жадный = " + greedyCost / reload);
        System.out.println("Последний генетический = " + fillGenetic.getCost());
        System.out.println("Средняя ценность генетический = " + geneticCost / reload);
        assertEquals(true, betterOrEqualsDynamic > 90);
        assertEquals(true, betterOrEqualsGreedy == 100);
    }

}