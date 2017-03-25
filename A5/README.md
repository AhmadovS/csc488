# Progress

* [x] ASTList
* [x] Expn
	* [x] IdentExpn
	* [x] BinaryExpn
		* [x] ArithExpn
		* [x] BoolExpn
		* [x] EqualsExpn
		* [x] CompareExpn
	* [x] ConditionalExpn
	* [x] ConstExpn
		* [x] BoolConstExpn
		* [x] IntConstExpn
		* [x] SkipConstExpn
		* [x] TextConstExpn
	* [x] FunctionalCallExpn
	* [x] UnaryExpn
		* [x] NotExpn
		* [x] SubsExpn
		* [x] UnaryMinusExpn
* [ ] Indentable
	* [x] Declaration
		* [x] RoutineDecl
		* [x] MultiDeclarations
		* [x] ScalarDecl
		* [x] DeclarationPart
			* [x] ScalarDeclPart
			* [x] ArrayDeclPart
	* [x] RoutineBody
	* [ ] Stmt
		* [x] AssignStmt
		* [ ] ExitStmt
		* [x] IfStmt
		* [x] LoopingStmt
			* [x] RepeatUntilStmt
			* [x] WhileDoStmt
		* [x] ProcedureCallStmt
		* [x] ReadStmt
		* [x] ReturnStmt
		* [x] Scope
			* [x] Program
		* [x] WriteStmt

# Notations
- Stack :: a -> b -> c
  This shows state of the top of the stack, with c being on top, followed by b and then a.

# Documentation of changes made

- Each AST node's lexic-level is now calculated during semanticCheck phase.

# Things we should have changed but didn't:
- Currenly declarations are executed in the same order that the program was written.
  This causes us to keep adding BR instructions in-between routine declarations and variable declarations, so that we would not execute any of the declarated routines and simply jump over their code.

# We have changed the structure of the activation record.
 
 We now put dynamic link below return address.

 -----------------
 3 static link
 -----------------
 2 return address
 -----------------
 1 dynamic link
 -----------------
 0 return value
 -----------------
