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
    private List<String> warnings;

    private SemanticError() {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    public static void addWarning(AST astNode, String msg) {
        LOC errorLoc = astNode.getLOC();
        instance.warnings.add(String.format("**Warning** at (line %d, col %d): %s.\n\t>>> %s\n",
                errorLoc.getLineNumber(), errorLoc.getColumnNumber(), msg, errorLoc.getLine()));
    }

    public static void addIdentAlreadyDeclaredError(AST astNode) {
        SemanticError.add(astNode, "Identifier with this name has already been declared");
    }

    public static void addIdentNotDeclaredError(AST astNode) {
        SemanticError.add(astNode, "Identifier with this name has not been declared");
    }

    public static void addIdentNotDeclaredError(int semanticId, AST astNode) {
        String errorMsg;
        switch(semanticId) {
            case 37:
                errorMsg = "Identifier has not been declared as scalar variable";
                break;
            case 38:
                errorMsg = "Identifier has not been declared as an array";
                break;
            case 39:
                errorMsg = "Identifier has not been declared as a parameter";
                break;
            case 40:
                errorMsg = "Identifier has not been declared as a function";
                break;
            case 41:
                errorMsg = "Identifier has not been declared as a procedure";
                break;
            default:
                throw new IllegalArgumentException("semantic id is invalid");
        }
        SemanticError.add(semanticId, astNode, errorMsg);
    }

    public static void add(AST astNode, String errorMsg) {
        LOC errorLoc = astNode.getLOC();
        instance.errors.add(String.format("Semantic Error at (line %d, col %d): %s.\n\t>>> %s\n",
                errorLoc.getLineNumber(), errorLoc.getColumnNumber(), errorMsg, errorLoc.getLine()));
    }

    public static void add(int semanticId, AST astNode, String errorMsg) {
        LOC errorLoc = astNode.getLOC();
        instance.errors.add(String.format("Semantic Error S%d at (line %d, col %d): %s.\n\t>>> %s\n",
                semanticId, errorLoc.getLineNumber(), errorLoc.getColumnNumber(), errorMsg, errorLoc.getLine()));
    }

    public static List<String> getErrors() {
        return instance.errors;
    }

    public static List<String> getWarnings() {
        return instance.warnings;
    }

}
