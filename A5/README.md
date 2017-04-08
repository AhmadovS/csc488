# Notations
- Stack :: a -> b -> c
  This shows state of the top of the stack, with c being on top, followed by b and then a.

# Documentation of changes made since last assignment

- Each routine now calculates base of its own activation record.
  Display update algorithm is not split between caller and callee anymore

- Each AST node's lexic-level is now calculated during semanticCheck phase.

# Things we should have changed but didn't:
- Currenly declarations are executed in the same order that the program was written.
  This causes us to keep adding BR instructions in-between routine declarations and variable declarations, so that we would not execute any of the declarated routines and simply jump over their code.
  The better solution would be to reorder the AST.


# Contributors

Amir Hossein Heidari Zadi:
- Worked on Code-gen for functions, procedures and display update algorithm, and variable declarations and assignments.
- Fixed remining semantic check bugs, and added code to prepare the project for code generations.
- Wrote test cases for variable declarations and assignments.
- Worked on documentation for program initialization and exit, variable declarations and code-gen for functions and procedures.
- Wokred on check of code paths for return statements and issuing warnings.

Samud Ahmadov:
 - Worked on Code-gen for functions, procedures and display update algorithm 
 - worked on if statements
 - Wrote test cases for functions and if statements
 


Jathu Satkunarajah:
- Worked on code-gen for all expressions and statments
- Worked on test cases for expressions, statements and complex tests
- Inline documentation for code generation

Yang Song:
- Worked on code generation for the ExitStmt class
- Wrote documentation for the assignment
- Checked for bugs by running the test suite

