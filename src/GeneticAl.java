import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeneticAl {
    static private Random random = new Random();
    static private int generationDigit = 5;//сколько поколений
    static private int firstGenerationDigit = 30;//сколько особей в первом поколении
    static private int survivorsDigit = 5;//сколько выживает в каждом поколении после отбора
    private int maxLoad;
    private Item[] items;


    public GeneticAl(int load, List<Item> items) {
        this.items = (Item[]) items.toArray();
        maxLoad = load;
    }

    public Individual fillKnapsackGenetic() {


        Generation generation = new Generation();
        //Эволюционируем заданное колличество раз
        //Каждый раз выбираем из поколения лучший экземпляр
        //На случай, если последнее поколение может не оказаться лучшим
        for (int i = 0; i < generationDigit; i++)
            generation.evolute();


        return generation.bestIndividualInHistory;
    }


    //Особь
    private class Individual {
        byte[] gens;
        int fitness;//!!!Научиться считать
        int load;

        Individual() {
            load = 0;
            fitness = 0;
            gens = new byte[items.length];
            for (int i = 0; i < gens.length; i++)
                //Если предмет нельзя взять, гарантированно не берем
                if (load + items[i].getWeight() > maxLoad)
                    gens[i] = 0;
                else {
                    gens[i] = (byte) random.nextInt(1);
                    if (gens[i] == 1) {
                        load += items[i].getWeight();
                        fitness += items[i].getCost();
                    }
                }


        }

        Individual(byte[] gens, int load, int fitness) {
            this.gens = gens;
            this.load = load;
            this.fitness = fitness;

        }


        //Скрещивание особей
        private Individual cross(Individual opponent) {
            byte[] gensOfChild = new byte[gens.length];
            int load = 0;
            int fitness = 0;
            for (int i = 0; i < gens.length; i++)
                if (gens[i] == opponent.gens[i])
                    gensOfChild[i] = gens[i];
                else if (load + items[i].getWeight() > maxLoad)
                    gensOfChild[i] = 0;
                else {
                    gensOfChild[i] = (byte) random.nextInt(i);
                    load += items[i].getWeight();
                    fitness += items[i].getCost();
                }

            return new Individual(gensOfChild, load, fitness);

        }

        @Override
        public String toString() {
            String string = "";
            string += "Fill(cost=" + fitness + ", items=[)";
            for (int i = 0; i < gens.length; i++)
                if (gens[i] == 1)
                    string += "Item(cost=" + items[i].getCost() + ", weight=" + items[i].getWeight() + "),  ";
            string += "])";
            return string;
        }
    }

    //Поколение
    private class Generation {

        List<Individual> individuals;
        Individual bestIndividualInHistory;

        Generation() {
            individuals = new ArrayList<>();
            for (int i = 0; i < firstGenerationDigit; i++)
                individuals.add(new Individual());

            bestIndividualInHistory = individuals.get(0);

        }


        //Скрещивание особей
        private void evolute() {
            individuals = selection();
            individuals = crossing();
            searchBestIndividuals();
        }


        private void searchBestIndividuals() {
            for (Individual individual : individuals)
                if (individual.fitness > bestIndividualInHistory.fitness)
                    bestIndividualInHistory = individual;
        }

        private List<Individual> selection() {
            List<Individual> survivorsIndividuals = new ArrayList<>(survivorsDigit);
            int removeIndex = 0;
            for (int i = 0; i < survivorsIndividuals.size(); i++) {
                Individual bestInGeneration = individuals.get(0);
                for (int j = 0; j < individuals.size(); j++) {
                    if (individuals.get(j).fitness > bestInGeneration.fitness) {
                        bestInGeneration = individuals.get(j);
                        removeIndex = j;

                    }


                }
                survivorsIndividuals.add(bestInGeneration);
                individuals.remove(removeIndex);

            }

            return survivorsIndividuals;
        }

        private List<Individual> crossing() {
            List<Individual> nextGeneration = new ArrayList();
            for (int i = 0; i < individuals.size(); i++)
                for (int j = i + 1; j < individuals.size(); j++)
                    nextGeneration.add(individuals.get(i).cross(individuals.get(j)));


            return nextGeneration;
        }


    }

    public static void main(String[] args) {
        List<Item> items = new ArrayList<>();
        items.add(new Item(8, 10));
        items.add(new Item(5, 12));
        items.add(new Item(6, 8));
        items.add(new Item(10, 15));
        items.add(new Item(4, 2));
        GeneticAl geneticAl = new GeneticAl(30,items);
        System.out.println(geneticAl.fillKnapsackGenetic().toString());
    }
}
