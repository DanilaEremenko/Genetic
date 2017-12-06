package Genetic;

import java.util.*;

public class GeneticAl {
    Random random = new Random();
    private int randomIndividDigit = 10;//сколько мутантов появляется в новом поколении
    private int generationDigit = 10;//сколько поколений
    private int firstGenerationDigit = 50;//сколько особей в первом поколении
    private int survivorsDigit = 10;//сколько выживает в каждом поколении после отбора
    private int maxLoad;
    private Item[] items;

    public GeneticAl() {
    }

    public GeneticAl(int randomIndividDigit, int generationDigit, int firstGenerationDigit, int survivorsDigit) {
        this.randomIndividDigit = randomIndividDigit;
        this.generationDigit = generationDigit;
        this.firstGenerationDigit = firstGenerationDigit;
        this.survivorsDigit = survivorsDigit;
    }

    public void set(int mutantsDigit, int generationDigit, int firstGenerationDigit, int survivorsDigit) {
        this.randomIndividDigit = mutantsDigit;
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
            int goFromStart = random.nextInt(3);
            //1-с нуля
            //2-c конца
            //3-с центра

            if (goFromStart == 1)
                for (int i = 0; i < gens.length; i++) {
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
            else if (goFromStart == 2)
                for (int i = gens.length - 1; i >= 0; i--) {
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
            else if (goFromStart == 3)
                for (int i = 0; i < gens.length / 2 - 1; i++) {
                    gens[gens.length / 2 + i] = (byte) random.nextInt(2);
                    gens[gens.length / 2 - i] = (byte) random.nextInt(2);
                    if (gens.length / 2 + i == 1) {
                        load += items[gens.length / 2 + i].getWeight();
                        fitness += items[gens.length / 2 + i].getCost();
                    }
                    if (gens.length / 2 - i == 1) {
                        load += items[gens.length / 2 - i].getWeight();
                        fitness += items[gens.length / 2 - i].getCost();

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

        private Individual mutantGensToLeft() {
            byte[] gensMutant = new byte[items.length];
            int fitness = 0;
            int load = 0;
            for (int i = 1; i < items.length; i++) {
                gensMutant[i] = gens[i - 1];

            }
            gensMutant[0] = gens[gens.length - 1];
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
            return "f: " + fitness + " w: " + load;
        }
    }

    //Поколение
    private class Generation {

        List<Individual> individuals;
        Individual bestIndividualInHistory;

        Generation() {
            individuals = new ArrayList<>();
            //Первое поколение
            for (int i = 0; i < firstGenerationDigit; i++)
                individuals.add(new Individual());

            byte[] gens = new byte[items.length];
            for (int i = 0; i < gens.length; i++) {
                gens[i] = 0;
            }
            bestIndividualInHistory = new Individual(gens, 0, 0);

        }


        private void evolute() {
            for (int i = 0; i < randomIndividDigit; i++)
                individuals.add(new Individual());
            individuals = selection();
            individuals.addAll(mutants());
            individuals = selection();
            individuals = crossing();
            searchBestIndividuals();
        }


        private void searchBestIndividuals() {
            for (Individual individual : individuals)
                if (individual.fitness > bestIndividualInHistory.fitness && individual.load < maxLoad)
                    bestIndividualInHistory = individual;
        }


        private List<Individual> selection() {
            List<Individual> survivorsIndividuals = new ArrayList<>(survivorsDigit);
            for (int i = 0; i < survivorsDigit; i++) {
                int removeIndex = 0;
                Individual bestInGeneration = new Individual(new byte[items.length], 0, 0);
                for (int j = 0; j < individuals.size(); j++) {
                    if (individuals.get(j).fitness > bestInGeneration.fitness && individuals.get(j).load <= maxLoad) {
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
        private List<Individual> mutants() {
            List<Individual> mutants = new ArrayList<>();
            for (Individual individual : individuals) {
                mutants.add(individual.mutantReverse());
                mutants.add(individual.mutantGensToLeft());
            }


            return mutants;

        }


    }

}
