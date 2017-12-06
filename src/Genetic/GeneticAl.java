package Genetic;

import java.util.*;

public class GeneticAl {
    Random random = new Random();
    private int mutantsDigit = 10;//сколько мутантов появляется в новом поколении
    private int generationDigit = 10;//сколько поколений
    private int firstGenerationDigit = 50;//сколько особей в первом поколении
    private int survivorsDigit = 10;//сколько выживает в каждом поколении после отбора
    private int maxLoad;
    private Item[] items;

    public GeneticAl() {
    }

    public GeneticAl(int mutantsDigit, int generationDigit, int firstGenerationDigit, int survivorsDigit) {
        this.mutantsDigit = mutantsDigit;
        this.generationDigit = generationDigit;
        this.firstGenerationDigit = firstGenerationDigit;
        this.survivorsDigit = survivorsDigit;
    }

    public void set(int mutantsDigit, int generationDigit, int firstGenerationDigit, int survivorsDigit) {
        this.mutantsDigit = mutantsDigit;
        this.generationDigit = generationDigit;
        this.firstGenerationDigit = firstGenerationDigit;
        this.survivorsDigit = survivorsDigit;
    }


    public Fill fillKnapsackGenetic(int load, List<Item> items) {
        this.items = new Item[items.size()];
        this.items = items.toArray(this.items);
        maxLoad = load;
        return fillKnapsackGenetic().convertToFill();

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
    public class Individual {
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
                if (gens[i] == opponent.gens[i]) {
                    gensOfChild[i] = gens[i];
                    if (gens[i] == 1) {
                        load += items[i].getWeight();
                        fitness += items[i].getCost();
                    }
                } else {
                    gensOfChild[i] = (byte) random.nextInt(2);
                    if (gensOfChild[i] == 1) {
                        load += items[i].getWeight();
                        fitness += items[i].getCost();
                    }
                }

            return new Individual(gensOfChild, load, fitness);

        }

        //Мутации
        private Individual mutantReverse() {
            byte[] gensMutant = new byte[items.length];
            int fitness = 0;
            int load = 0;
            for (int i = 0; i < items.length; i++) {
                if (gens[i] == 1)
                    gensMutant[i] = 0;
                else {
                    gensMutant[i] = 1;
                    load += items[i].getWeight();
                    fitness += items[i].getCost();
                }

            }
            return new Individual(gensMutant, load, fitness);
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
            return "" + fitness;
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
            individuals.addAll(mutation());
            searchBestIndividuals();
        }


        private void searchBestIndividuals() {
            for (Individual individual : individuals)
                if (individual.fitness > bestIndividualInHistory.fitness && individual.load < maxLoad)
                    bestIndividualInHistory = individual;
        }

        //Попробовать удалять неотобранные
        private List<Individual> selection() {
            List<Individual> survivorsIndividuals = new ArrayList<>(survivorsDigit);
            for (int i = 0; i < survivorsDigit; i++) {
                int removeIndex = 0;
                Individual bestInGeneration = new Individual(individuals.get(0).gens, individuals.get(0).load, individuals.get(0).fitness);
                for (int j = 0; j < individuals.size(); j++) {
                    if (individuals.get(j).fitness > bestInGeneration.fitness) {
                        bestInGeneration = new Individual(individuals.get(j).gens, individuals.get(j).load, individuals.get(j).fitness);
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
        private List<Individual> mutation() {
            List<Individual> mutatnts = new ArrayList<>();
            for (Individual individual : individuals)
                mutatnts.add(individual.mutantReverse());

            return mutatnts;

        }


    }

}
