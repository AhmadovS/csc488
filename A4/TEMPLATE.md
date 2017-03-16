# 1. Storage

## Overview
Storage is divided into stack of activation records, with the display having a pointer to the base of each activation record. 

Activation records are treated slightly different from major and minor scopes in the semantic phase of the compiler. In code generation only the program scope, and functions and procedures have their own activation record, and all other statements and minor scopes are not given any special treatment. ( We need to worry about declaration inside minor scopes and how storage is allocated for them. This will be discussed further below.)

In the rest of this document, major scope only refers to a lexical level, namely the main program scope, functions and procedures. And all other scopes are called minor scopes i.e. `{ declaration statement}` scopes, loop statements and if statements.

### Important conventions:
Each activation record has an associated lexical level, therefore main program scope is given lexical level(LL) of 0, and following that only functions and procedures are given their own lexical level, and each child function/procedure is going to have a lexical level 1 above its parent. Note that `display` is indexed using the lexical level.
Let $L be the lexical level of an activation record, `ADDR $L 0`, will always point to the bottom of that activation record i.e. EMPTY_STORAGE_1 for program scope and `return value` for functions and procedures. Therefor for program scope, variables are allocated beginning `ADDR 0 2`, and for functions and procedures local variables are allocated at `ADDR $L (3 + number of params)`.
In the byte code example below, any variable with a `$` prefix, is computed at compile time.

Activation record of the main program is diagrammed below:

|Program activation record|
|---|
|global variables, ...|
|EMPTY_STORAGE_2 |
|EMPTY_STORAGE_1 |

The program activation record has two EMPTY_STORAGE fields which we have left there in case they might become useful later on, e.g. returning a exit status or errno. On top of these two words, we will allocated storage for the global program variables.

Activation record of functions/procedures is diagrammed below:

| Routine Activation Record |
|---|
|local variables, ... |
|nth parameter |
|...|
|1st parameter|
|dynamic link (pointer to previous activation record - may not be parent.)|
|static link (pointer to lexical parent activation record.)|
|return address (points to code segment to return to)|
|return value (procedures would leave return value as UNDEFINED)|

For all functions and procedures, the bottom four fields (return value, ... , dynamic link) are available in their activation record. If they have any parameters, then they are pushed on top of `dynamic link`, and storage for the local variables will be allocated on top of that.

## Variable storage 
Whenever a declaration is reached, we will calculate the required storage at compile time and allocated that amount at run-time by using the schemes below. Since the storage requirements of all variables is known at compile time, the order number of each declared variable is well-defined.

However we also have to note that minor scopes can also contain declarations. Since minor scopes don’t have their own activation record, any declared variable inside of them is allocated on top of the stack, and their order number starts from 1 + the order number of last declaration in the containing lexical level (i.e. the containing major scope).

Since, by the time we finish executing any statement, the top element of the stack is guaranteed to be the storage area for the last declared variable (or last array element) for the lexical level that we are in during program execution, we can just allocated storage on top of the stack for any declaration encountered in a minor scope, and be safe that its order number is well-defined, and that storage for the declared variables is not going to be allocated on top of any temporary values on the stack.

### Scalar variable declaration and access

When we declare the scalar variables (Integer, Boolean) we first `PUSH UNDEFINED` to allocate the memory for that variable. Program uses DUPN to allocate memory for multideclaration.

Example: `var a : Boolean`
```
PUSH UNDEFINED
```

Example: `var a,b,c,d : Integer`
```
PUSH UNDEFINED
PUSH 4
DUPN
```
Since, the order numbers are well-defined and hence known at compile time, scalar variables are simply accessed using the following template.

Scalar variable access template
```
ADDR $LL $ON
LOAD
```

### Array Declaration and Indexing
Our language only supports one dimensional arrays. And arrays will have lower and upper bounds, to calculate required memory slots for array we will subtract lower bound from upper bound during the compile time(since our in array declaration, first and last elements are inclusive) and allocate that many slots for arrays during run-time. Note: for arrays with only upper bound declaration we will take 1 as a lower bound. The lower bound of A[n] is 1 and lower bound of A[m..n] is m.

Example array declaration:
var A[7] :Integer
```
PUSH UNDEFINED
PUSH 7  # length of array computed at compile time.
DUPN
```

Example array declaration:
B[2..5]
```
PUSH UNDEFINED
PUSH 4  # length of array computed at compile time.
DUPN
```

Array indexing template:
```
ADDR $LL $arrayBaseOrderNumber
# then calculate the subexpression inside
PUSH $lowerBound
SUB   # subtract lower bound from the value of subexpression inside.
ADD   # adds subexpn to base address to get the memory location
```

## Integer and Boolean Constants, Text Constants

Integers will be stored in the word as is. As the integers have a bound of -32767 to +32767 and the memory is 16 bits, it will align.
Boolean constants are defined in `Machine.java`
Text constants are pushed onto the stack as needed in reverse order.

Since all values of the above each take a full word (16 bits), we don’t have alignment problems.

# 2. Expressions

## Constants

Since the constants have been pushed onto stack during parsing the execution. We can simply use POP to access them during the expression evaluation. For example: a != True, PUSH MACHINE_TRUE will be executed when parsing the expression and later we can use POP to access its value

## Scalar variables

Since the code generation happens after the semantic analysis, we already have a symbol table created for this program. For each scalar variable in the program we can store the lexical level and offset. Then during code generation we can look up into our symbol table and get its lexical level and offset from the table and use ADDR operation to access the scalar variable.

Example: let's say a has been declared as MACHINE_TRUE and stored in our symbol table where its lexical level is 0 and offset is 4. Then we want to execute a == False.

```
ADDR 0 4 (get the address of a)
LOAD
```

## Arrays

Our language only supports one-dimensional array. Then in order to declare an array with bounds, we have to allocate memory for that array. Let's say we have a declared array A[5]. And we want to access A[3]. We already know the base address of the array and then program executes following commands to access A[3]

```
PUSH 3
PUSH base_addr
ADD
LOAD
```

## Arithmetic operations

To do the arithmetic operations, we have to first access the left and right handside of the expression and pop and push them onto the top of the stack. After that we call MUL,DIV,ADD,SUB operators respectively. Example: x declared as 5 and y declared as 2. x is located at address display[LL] + 2 and y is located at display[LL] + 3

x*y

```
ADDR 0 2
LOAD
ADDR 0 3
LOAD
MUL
```

## Comparison operators

We do the same thing as above in the arithmetic operations, access the variables and place them on the top of the stack. Then call LT, EQ to do comparisons. 

3 <= 1

```
PUSH 3
PUSH 1
LT
PUSH 3
PUSH 1
EQ
OR
```

3 > 1

```
PUSH 3
PUSH 1
LT

PUSH 3
PUSH 1
EQ
OR
PUSH 0
EQ
```

## Boolean operations

Since we do not have a AND instruction, we can use De Morgan's law to replace AND statement with OR.  A and B = not(not(A) or not(B)). Again as above, we access the variables and place them on top of the stack and call appropriate instruction. Note, we have to also define "not" as it is not in our instruction set. We do this by a equivalence comparison to 0.

For NOT:
```
PUSH A
PUSH 0
EQ
```
This gives us the result: ```1 == 0 -> 1``` and ```1 == 0 -> 0```.

Now for AND (example of loading two different variables):
```
PUSH A
PUSH 0
EQ
PUSH B
PUSH 0
EQ
OR
PUSH 0
EQ
```

For OR:
```
PUSH 1	
PUSH 0
OR		% top = true or false
```

## Conditional expressions

For conditional expression, we access the value of conditional expression and place it on top of the stack and compare it with True. If it is an if-else statement, push the address of first instruction in else block and do branch false. If it is an ordinary if statement with an else clause, then place first address of the first instruction after if statement on top of the stack and do branch false.

```
	if a < 5:
	  b = 1
	else:
	  b = 2
```

Suppose a is at display[0]+4. This gets translated to:

```
1.ADDR 0 4
2.LOAD
3.PUSH 5
4.LT
5.PUSH 12
6.BF
7.ADDR 0 4
8.PUSH 1
9.STORE
10.PUSH 15
11.BR
12.ADDR 0 4
13.PUSH 2
14.STORE
15. // REST OF CODE
```

# 3. Functions and Procedures

## Overview
NOTES:
- routine is defined as either a function or a procedure.
- In the byte codes below, variables prefixed with $ (e.g. $L) are computed at compile time.
- All lexical levels and return addresses are computed at compile time.
- For any routine at lexical level $L, `ADDR $L 0` points to beginning of it's activation record (which is the address of return value).
- For this section et $L be the lexical scope of the callee and $curL lexical scope of caller.

Imagine we are currently executing routine P which calls routine Q. P has the responsibility to update the display only for the lexical level of Q (i.e. update display[$L]). It then creates the activation record for Q and evaluates its arguments, and then branches to the first instruction of the routine Q (which actually first runs the rest of the display update algorithm).

After Q finishes running, it sets the return value if it's a function, and pops all the values from it's activation record except for return value and return address, it then branches to return address of it's caller. The caller has the resposibility of using the return value which is now on top of the stack or POP it if it's not used.

## The activation record
Both functions and procedures have the same activation record structure, diagrammed below.

|Activation Record
|--------------------
|local variables, ...
|n-th parameter
|...
|1-st parameter
|dynamic link (pointer to previous activation record - may not be parent.)
|static link (pointer to lexical parent acitvation record.)
|return address (points to code segment to return to)
|return value (procedures would leave return value as UNDEFINED)

## Calling a routine

Before branching to a routine (or even evaluating its arguments), we first updated display[$L] and then we emit the codes for pushing the bottom four fields (return value, return address, static link, dynamic link) of the activation record of the callee, and then evaluate the arguments (if any). This is to ensure that by the time we have finished evaluating the arguments, the new activation record has the correct structure.

The first step of display update algorithm when calling a routine is to update display[$L]. Base address of the new activation record is going to be the value of MSP pointer, which is top of the stack. We then begin by pushing values that define the activation record of the callee.

```
# Sets display[$L] to starting word of it's (soob to be) activation record
PUSHMT
SETD $L

# Pushing the bottom four fields of callee's activation record.
PUSH UNDEFINED    # return value
PUSH $returnAddr  # return address
ADDR $(L-1) 0     # static link
ADDR $curL 0      # dynamic link

# Evaluating the arguments (if any)

# Branching to the callee.
BR $calleeCodeSegment
```

Note that since a routine can only call routines that are it's immediate children, or of a lexical lever; lower than it's own (i.e. it's ascendents and siblings), display[$L-1] always contains the link to activation record of the statically containing scope.

Afterwards the caller pushes the activation record for the callee, sets the return address, static and dynamic links and fills in the parameters, and then branches to it. Note that `return value` is pointed to by `ADDR $L 0` and first parameter (if available) is at `ADDR $L 4`.

## Display management
Let $L be the lexical scope of the callee.
After branching to the callee, we need to remember that only display[$L] is valid, and all lower display fields need to be updated. We do this by recursively following the static links.

```
# Caller has just branched to this code (first word of the callee)
# We now update rest of the display

ADDR $L 2      # Pushes addr of static link to stack
SETD $(L-1)    # Sets $(L-1) as the parent activation record of $L

# Since display[$(L-1)] now has a valid value, we can now follow
# it's static link, to get it's parent.
ADDR $(L-1) 2  
SETD $(L-2)

ADDR $(L-2) 2
SETD $(L-3)
...
ADDR 1 2
SETD 0

# We're done with updating the display
# Now we run first "actual" instruction of the callee.
```
We're gonna need to update the display after a routine call returns, this is explained in the section below.

## Procedure and function exit code.
After a routine reaches it's return statement, it sets the value for the `return value` if it's a function, cleans up all the stack values down to, but excluding `ADDR $L 3` (so only return address, return value, static link and dynamic link are on stack), we then update display[$callerL], where $callerL is lexic level of the caller, before branching to the caller's code. Once branched to the caller's code, the caller is responsible for updating the rest of the display.

After function reaches `return with` statement:
```
# Assumming return value is now on top of the stack.
ADDR $L 0  # addr of return value field
SWAP       # STORE needs value to be on top of stack
STORE
```

Now we cleanup and branch to caller:
```
# Calculate number of words to pop 
# (from top of stack to and including static link)
PUSHMT
ADDR $L 4
SUB
POPN

# Update display (at this point we assume display values are all invalid)
# Top of stack is now the dynamic link pointing 
# to base of caller's activation record.
# $callersL is the caller's lexical level.
SETD $callersL

POP # We don't need the static link, just pop it.

BR  # pops return address and branches to the caller.
```

<!-- After branching to the caller, if the caller doesn't use the `return value`, it needs to pop it. So in case of having called a procedure or a function whose return value we're not gonna use, we always need to call `POP`.
 -->
After branching to it's caller, 
```
# We need to remember that only display[$curL]  (right now $curL == $callerL) is valid,
# similar to the code above, we need to recursively examine
# the static link of the current activation record and update display.

ADDR $curL 2
SETD $(curL - 1)
ADDR $(curL - 1) 2
SETD $(curl - 2)
...
ADDR 1 2
SETD 0
```

# Statements

## Assignment statement

For each assignment statement, we will first need to calculate the address of the variable by looking at its enclosing scope's activation record. Each variable inside a scope is assigned an offset, so that inside a contiguous region of memory that contains the all of the scope's variables, the address of any given variable can be determined to be the starting address of the region of memory + the particular variable's offset. Since the scope's activation record has all of its variables stored in a contiguous region of memory, we can then easily determine the variable's address in the activation stack to be display[currentLL] + 2 * wordSize + variableOffset. Once that is done, the value (constant or variable) can be stored/pushed to the top of the stack, right before we store the value in the variable.

Example (Assume that variable x has already been declared as an Integer): ```x := 3```

can cause the following machine code to be generated (Note that currentLL will always be a constant/address of the currentLL value that is stored in the memory, and variableOffset is also a constant that determines the position of the given variable inside the variables region of the activation record):

```
ADDR currentLL 0
PUSH 2
PUSH 16
MUL
ADD 
PUSH variableOffset
ADD
PUSH 3
STORE
```

## if statements

Each if statement will be composed of 1) a conditional statement, 2) a "then clause" and 3) an "else clause" (in the case that the if statement is actually an if-else statement). In memory, the machine code for the conditional statement will be followed by the code for the "then clause", which will in turn be followed the machine code for the "else clause" (in the case of an if-else statement). 
It is important to note that the address of each and every instruction in the program is known at compile time. During code generation every node in the AST tree will need to be traversed. Although the length of the "then clause" is unknown when the node for the enclosing if statement is first encountered, the generator will nevertheless proceed to examine the then clause's node in a depth first manner before coming back up to its parent node (i.e the if statement's node), at which point the length of the "then clause" will be known.
Therefore the length of the then clause will be known at compile time. If firstAddress is the address of the first instruction belonging to the "then clause" (which is also known at compile time), then we can calculate the address of the first instruction that follows the "then clause" to be firstAddress + length of "then clause"

Example (Assume that variable b has already been declared as an Integer): 

```
if (false) 
  then b := 1 
else 
  b := 0
```

can cause the following machine code to be generated (Note that thenLength is a constant representing the length of the "then clause" that is determined at compile time):

```
PUSH firstAddress
PUSH thenLength
ADD
PUSH false
BF
```

## the while and repeat statements
The repeat statement is somewhat similar to an if statement. As the address of each and every instruction in the program is known at compile time, a while statement can be translated to a sequence of instructions for the loop's body followed by a conditional branch that branches to the loop body if the conditional expression evaluates to false.
For a while statement however, the statement's conditional expression is evaluated before any instruction belonging to the loop's body is executed. The evaluation fo the conditional expression is then followed by a conditional branch (i.e BF), so that the program will jump to the first instruction following the while statement if the conditional evalutes to false. The instructions/machine code belonging to the loop body follows the conditional branch, and finally an unconditional branch to the instruction that initially evaluated the conditional expression is added after the loop body.

Example 1: ```repeat b := 1 until false```

can cause the followiong code to be generated (Assume that firstAddr is the address of the first instruction, b has already been declared as an Integer, and the address of variable b has already been calculated and put on top of the stack):

```
PUSH 1
STORE   % b := 1

PUSH firstAddr % firstAddr is the address of the instruction PUSH 1

PUSH 0

BF
```

Example 2: ```while true do b := 100```

causes the following code to be generated (Again assume that b has already been declared as an Integer):

```
PUSH nextStatementAddr      % Address of the first statement that immediately follows the current while statement
                            % is equal to the address of the instruction PUSH nextStatementAddr + total length of while
PUSH 1

BF
% Assume that inside the current activation record's list of variables, variable b's distance from the first variable in the % list is the constant bOffset 
ADDR currentLL 0

PUSH 2

PUSH 16

MUL

ADD 

PUSH bOffset

ADD             % address of b = display[currentLL] + 2 * wordSize + bOffset

PUSH 100

STORE           % b := 100

BR firstAddr    % firstAddr is the address of PUSH nextStatementAddr
```

## all forms of exit statements
An exit statement will cause an uncoditional branch to be generated by the code generator. The address that will be branched to would be the address of the first statement that follows the enclosing loop/scope (which can be determined at compile time). Similarly, an exit i statement that exits from i loops (where i is an integer) will cause an unconditional branch statement to be generated. In this case, the address that will be branched to is the address of the first instruction that follows the n-th enclosing loop.
Finally, upon encountering an exit i when e expression (where is an integer, e is a conditional expression) the generator will generate an unconditional branch also generate an unconditional branch that branches to the address of the first instruction that follows the n-th enclosing loop. However, the branch will be preceded by an evaluation of the conditional e and a conditional branch BF that causes the unconditional branch to be skipped, iff e evaluates to false.

Example 1: ```exit```

generates:
br address          % where address is the address of the first instruction following the enclosing loop

Example 2: ```exit 2```

generates:
br nAddress          % where nAddress is the address of the first instruction following the second enclosing loop

Example 3: ```exit 2 when false```
PUSH 0      % push the conditional expression (which is false in this case) on to the top of the stack

BF nextAddress      % nextAddress is the address of current instruction + 2 

br nAddress          % where nAddress is the address of the first instruction following the second enclosing loop

## return statements
In the case of a simple return statement (for a procedure), the size of the current procedure's activation record is first calculated, before the entire record is popped off the stack.
If the return statement returns with an expression however, everything on the activation record except the return value will be popped off the stack.

Example 1 (Assume the following statement is nested inside a procedure): ```return```

generates:
ADDR currentLL 0    % Get the starting address of the activation record

PUSHMT              % Get the address of the top of the stack

SUB                 % Get the difference to calculate the size of the activation record

POPN                % Pop off the activation record

Example 2 (Assume the following statement is nested inside a function): ```return with 1```

generates:

ADDR currentLL 0    % Get the starting address of the activation record

PUSHMT              % Get the address of the top of the stack

SUB                 % Get the difference to calculate the size of the activation record

PUSH 1              % Size of the return value on the activation record

POPN                % Pop off everything on the record except the return value

## 'read' and 'write' statements

During code generation, a 'write' output instruction can result in a mixture of integers and strings being outputted. Due to this fact, output will be converted an ASCII string by the code generator if it is an Integer. Similarly, a character or newline can be represented as '\n' in Unix systems, so any newline character will be converted to the string "\n". This is done so that a sequence of characters, strings, newlines, integers can be treated as a single string by the code generator. Once the entire output argument has been converted into a string, we will be able to print output as a sequence of ASCII characters.

Example 1: ```write "Hello", newline```

generates:

PRINTC

PRINTC

PRINTC

PRINTC

PRINTC

PRINTC

PRINTC

PUSH 110

PUSH 92 

PUSH 111

PUSH 108 

PUSH 108 

PUSH 101 

PUSH 72 

'read' statements only read in Integers, so no ASCII conversions are used for this particular statement. A sequence of inputs will be read in using multiple READI instructions, as shown below. 

Example 2 (Assume a, b have already been declared as Integer variables): ```read a, b```

generates:

ADDR currentLL 0

PUSH 2

PUSH 16

MUL

ADD 

PUSH aOffset 

ADD             % address of a = display[currentLL] + 2 * wordSize + aOffset

READI

STORE 

ADDR currentLL 0

PUSH 2

PUSH 16

MUL

ADD 

PUSH bOffset 

ADD             % address of b = display[currentLL] + 2 * wordSize + bOffset

READI

STORE 

## handling of minor scopes
    


