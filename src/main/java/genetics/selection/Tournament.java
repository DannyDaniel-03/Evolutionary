package genetics.selection;

import SAT.Statement;
import genetics.Chromosome;
import genetics.Fitness;
import genetics.LocalSearch;
import genetics.Population;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;
import java.util.Comparator;

public class Tournament extends Selection {
    //private final Statement statement;

    private static final int TOURNAMENT_SIZE = 3;
    private static final int WINNER_COUNT = 2;

    public Tournament(Population current_population, Fitness[] fitness, Statement statement) {
        super(current_population, fitness);
        //this.statement = statement;
    }

    @Override
    public Population getNextPopulation() {
        int popSize = fitness.length;
        int geneCount = currentPopulation.at(1).getGeneCount();
        Population newPopulation = new Population(popSize, geneCount, false);
        int ELITE_COUNT = 5;

        Integer[] indices = new Integer[popSize];
        for (int i = 0; i < popSize; i++) {
            indices[i] = i;
        }

        Arrays.sort(indices, Comparator.<Integer>comparingInt(i -> fitness[i].getFitness()).reversed());

        for (int e = 0; e < ELITE_COUNT; e++) {
            int eliteIdx = indices[e] + 1;
            /*Chromosome elite = currentPopulation.at(eliteIdx);
            LocalSearch.greedyImprove(statement, elite);*/
            newPopulation.setChromosome(e + 1, currentPopulation.at(eliteIdx));
        }

        int filled = ELITE_COUNT + 1;
        while (filled <= popSize) {
            List<Integer> participants = new ArrayList<>(TOURNAMENT_SIZE);
            for (int k = 0; k < TOURNAMENT_SIZE; k++) {
                int idx;
                do {
                    idx = ThreadLocalRandom.current().nextInt(1, popSize + 1);
                } while (participants.contains(idx));
                participants.add(idx);
            }

            participants.sort((a, b) -> Integer.compare(
                    fitness[b - 1].getFitness(),
                    fitness[a - 1].getFitness()));

            for (int w = 0; w < WINNER_COUNT && filled <= popSize; w++) {
                int winnerIdx = participants.get(w);
                Chromosome winner = currentPopulation.at(winnerIdx);
                newPopulation.setChromosome(filled, winner);
                filled++;
            }
        }


        return newPopulation;
    }
}
