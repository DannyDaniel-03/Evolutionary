import Parser.CNFParser;
import SAT.Statement;
import genetics.*;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String filePath = "data/2bitcomp_5.cnf";
        CNFParser parser = new CNFParser();
        parser.parse(filePath);

        Statement statement = parser.getStatement();
        int variableCount = parser.getVariableCount();

        Simulation simulation = new Simulation(variableCount, statement);
        simulation.simulate();
        System.out.println("Done.");
    }
}