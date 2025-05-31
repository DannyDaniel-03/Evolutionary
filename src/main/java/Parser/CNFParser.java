package Parser;

import SAT.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CNFParser {
    private int variableCount;
    private int clauseCount;
    private Statement statement;

    public void parse(String filePath) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            ArrayList<Clause> clauses = new ArrayList<>();

            // Step 1–3: Skip comments, find problem line
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("c")) continue;

                if (line.startsWith("p cnf")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length != 4) {
                        throw new IllegalArgumentException("Invalid problem line format: " + line);
                    }
                    variableCount = Integer.parseInt(parts[2]);
                    clauseCount = Integer.parseInt(parts[3]);
                    break;
                } else {
                    throw new IllegalArgumentException("Expected problem line starting with 'p cnf', found: " + line);
                }
            }

            if (variableCount == 0 || clauseCount == 0) {
                throw new IllegalStateException("CNF header not found or incomplete.");
            }

            // Step 4–6: Parse clauses
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("c")) continue;

                String[] tokens = line.split("\\s+");
                ArrayList<Integer> literalsList = new ArrayList<>();

                for (String token : tokens) {
                    int literal = Integer.parseInt(token);
                    if (literal == 0) break;
                    literalsList.add(literal);
                }

                if (!literalsList.isEmpty()) {
                    clauses.add(new Clause(literalsList));
                }
            }

            statement = new Statement(clauses);
        }
    }

    public int getVariableCount() {
        return variableCount;
    }

    public int getClauseCount() {
        return clauseCount;
    }

    public Statement getStatement() {
        return statement;
    }
}