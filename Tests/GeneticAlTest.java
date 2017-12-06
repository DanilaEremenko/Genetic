import Genetic.Algs;
import Genetic.Fill;
import Genetic.GeneticAl;
import Genetic.Item;
import org.junit.Before;

import static kotlin.text.Typography.times;
import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Genetic.Algs.*;

public class GeneticAlTest {
    Random random=new Random();
    private List<Item> items;
    private GeneticAl geneticAl;
    private Algs algs = new Algs();
    private int itemsSize=100;

    @Before
    public void temps() {
        items = new ArrayList<>();
        for (int i = 0; i <itemsSize ; i++)
        items.add(new Item(1+random.nextInt(50),1+random.nextInt(50)));

        geneticAl = new GeneticAl(30, items, 20);

    }

    @org.junit.Test
    public void fillKnapsackGenetic() throws Exception {
        Fill fillGenetic=new Fill();
        Fill fillDynamic=new Fill();
        Fill fillGreedy=new Fill();
        int betterOrEqualsDynamic=0;
        int betterOrEqualsGreedy=0;
        int reload=100;
        for (int i = 0; i < reload; i++) {
            fillGenetic = GeneticAl.fillKnapsackGenetic(30, items, 10);
            fillDynamic = algs.fillKnapsackDynamic(30, items, new HashMap<>());
            fillGreedy = algs.fillKnapsackGreedy(30, items);
            if(fillGenetic.getCost()>=fillDynamic.getCost())
                betterOrEqualsDynamic++;
            if(fillGenetic.getCost()>fillGreedy.getCost())
                betterOrEqualsGreedy++;
        }
        System.out.println(betterOrEqualsDynamic);
        System.out.println(betterOrEqualsGreedy);
        System.out.println(fillDynamic.getCost());
        System.out.println(fillGreedy.getCost());
        assertEquals(true,betterOrEqualsDynamic>90);
        assertEquals(true,betterOrEqualsGreedy==100);
    }

}