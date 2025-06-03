package genetics;

import SAT.Statement;

public class LocalSearch {
    public static void greedyImprove(Statement statement, Chromosome chrom) {
        boolean improved = true;
        int varCount = chrom.getGeneCount();

        while (improved) {
            improved = false;
            double currentScore = new Fitness(statement, chrom).getFitness();

            Chromosome bestNeighbor = null;
            double bestScore = currentScore;

            for (int var = 1; var <= varCount; var++) {
                Chromosome trial = new Chromosome(chrom);
                trial.flipGene(var);

                double trialScore = new Fitness(statement, trial).getFitness();
                if (trialScore > bestScore) {
                    bestScore = trialScore;
                    bestNeighbor = trial;
                }
            }

            if (bestNeighbor != null) {
                chrom.replaceWith(bestNeighbor);
                improved = true;
            }
        }
    }
}
