package genetics;

public class Mutation {

    public static void mutate(Chromosome chromosome, double probability) {
        int geneCount = chromosome.getGeneCount();
        for (int i = 1; i <= geneCount; i++) {
            if (Math.random() < probability) {
                chromosome.flipGene(i);
            }
        }
    }
}
