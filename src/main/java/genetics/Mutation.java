package genetics;

public class Mutation {
    private static double PROBABILITY = 0.01;

    public static void mutate(Chromosome chromosome) {
        int geneCount = chromosome.getGeneCount();
        for (int i = 1; i <= geneCount; i++) {
            if (Math.random() < PROBABILITY) {
                chromosome.flipGene(i);
            }
        }
    }
}
