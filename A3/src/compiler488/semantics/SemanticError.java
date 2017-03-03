package compiler488.semantics;

import compiler488.ast.AST;
import compiler488.ast.LOC;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class that holds all errors.
 *
 * Created by Amir on 3/3/2017.
 */
public class SemanticError {

    private static SemanticError instance = new SemanticError();

    private List<String> errors;

    private SemanticError() {
        errors = new ArrayList<>();
    }

    public static void addIdentAlreadyDeclaredError(AST astNode) {
        SemanticError.add(astNode, "Identifier with this name has already been declared.");
    }

    public static void add(AST astNode, String errorMsg) {
        LOC errorLoc = astNode.getLOC();
        instance.errors.add(String.format("Semantic Error at (line %d, col %d): %s.\n\t%s\n",
                errorLoc.getLineNumber(), errorLoc.getColumnNumber(), errorMsg, errorLoc.getLine()));
    }

    public static void add(int semanticId, AST astNode, String errorMsg) {
        LOC errorLoc = astNode.getLOC();
        instance.errors.add(String.format("Semantic Error S%d at (line %d, col %d): %s.\n\t%s\n",
                semanticId, errorLoc.getLineNumber(), errorLoc.getColumnNumber(), errorMsg, errorLoc.getLine()));
    }

    public static List<String> getErrors() {
        return instance.errors;
    }

}
