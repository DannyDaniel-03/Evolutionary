package genetics;

import java.util.concurrent.ThreadLocalRandom;

public class Chromosome {
    private final byte[] genes;
    private final int geneCount;

    private void checkIndex(int index) {
        if (index < 1 || index > geneCount) {
            throw new IndexOutOfBoundsException("Gene index " + index + " is out of bounds (1-" + (geneCount) + ")");
        }
    }

    public Chromosome(int geneCount, boolean random) {
        int byteCount = (geneCount + 7) / 8;//8 bits per byte
        this.genes = new byte[byteCount];
        this.geneCount = geneCount;

        if (random)
            for (int i = 1; i <= geneCount; i++) {
                setGene(i, ThreadLocalRandom.current().nextBoolean());
            }
        else
            for (int i = 0; i < byteCount; i++) {
                genes[i] = 0;
            }
    }

    public boolean getGene(int index) {
        checkIndex(index--);
        return (genes[index / 8] & (1 << (index % 8))) != 0;
    }

    public void setGene(int index, boolean value) {
        checkIndex(index--);
        if (value) {
            genes[index / 8] |= (byte) (1 << (index % 8));//sets the bit
        } else {
            genes[index / 8] &= (byte) ~(1 << (index % 8));//clears the bit
        }
    }

    public void flipGene(int index) {
        checkIndex(index--);
        genes[index / 8] ^= (byte) (1 << (index % 8));
    }

    public int getGeneCount() {
        return geneCount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(geneCount);
        for (int i = 1; i <= geneCount; i++) {
            sb.append(getGene(i) ? '1' : '0');
        }
        return sb.toString();
    }
}
