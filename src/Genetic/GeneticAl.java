package Genetic;

import java.util.*;

public class GeneticAl {
    Random random = new Random();
    private int mutantsDigit = 10;//сколько мутантов появляется в новом поколении
    private int generationDigit = 10;//сколько поколений
    private int firstGenerationDigit = 30;//сколько особей в первом поколении
    private int survivorsDigit = 5;//сколько выживает в каждом поколении после отбора
    private int maxLoad;
    private Item[] items;


    public GeneticAl(int load, List<Item> items, int generationDigit) {
        this.generationDigit = generationDigit;
        this.items = new Item[items.size()];
        this.items = items.toArray(this.items);
        maxLoad = load;
    }


    public static Fill fillKnapsackGenetic(int load, List<Item> items, int generationDigit) {

        GeneticAl geneticAl = new GeneticAl(load, items, generationDigit);
        return geneticAl.fillKnapsackGenetic().convertToFill();

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
        int fitness;
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
                    gens[i] = (byte) random.nextInt(2);
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

        Fill convertToFill() {
            Set<Item> takenItems = new HashSet<>();
            for (int i = 0; i < items.length; i++)
                if (gens[i] == 1)
                    takenItems.add(items[i]);

            return new Fill(fitness, takenItems);
        }

        @Override
        public String toString() {
            String string = "";
            string += "Genetic.Fill(cost=" + fitness + ", items=[)";
            for (int i = 0; i < gens.length; i++)
                if (gens[i] == 1)
                    string += "Genetic.Item(cost=" + items[i].getCost() + ", weight=" + items[i].getWeight() + "),  ";
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


        private void evolute() {
            individuals = selection();
            individuals = crossing();
            for (int i = 0; i < mutantsDigit; i++)
                individuals.add(new Individual());
            individuals.add(reverseGens(bestIndividualInHistory));
            searchBestIndividuals();
        }


        private void searchBestIndividuals() {
            for (Individual individual : individuals)
                if (individual.fitness > bestIndividualInHistory.fitness && individual.load < maxLoad)
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

        //Мутации
        private Individual reverseGens(Individual individual) {
            byte[] mutantsGens = new byte[items.length];
            int fitness = 0;
            int weight = 0;
            for (int i = 0; i < items.length; i++)
                if (individual.gens[i] == 1)
                    mutantsGens[i] = 0;
                else {
                    mutantsGens[i] = 1;
                    fitness += items[i].getCost();
                    weight += items[i].getWeight();
                }


            return new Individual(mutantsGens, weight, fitness);

        }


    }

}
