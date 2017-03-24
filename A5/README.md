# Documentation of changes made

- Each AST node's lexic-level is now calculated during semanticCheck phase.

# Things we should have changed but didn't:
- Currenly declarations are executed in the same order that the program was written.
  This causes us to keep adding BR instructions in-between routine declarations and variable declarations, so that we would not execute any of the declarated routines and simply jump over their code.