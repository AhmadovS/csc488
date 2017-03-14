# Introduction

Lorem ipsum

# Storage

We will push activation records for each scope, preducure and function of the program into the runtime stack. Each activation record contains the following words: return data, dynamic link (pointer to the start of the previous record) and then the following arguments and/or variables in that specific scope. The records are in this specific order, meaning the first word in a record is the return data, followed by the dynamic link then the arguments and/or variables. Once the record needs to be used, you simply pop all but the bottom two words. Push the result into the return data and use the dynamic link to go back to the previous record.

![](images/storage-1.png)

Each scope will have an associated lexical level (LL) in reference to the main scope. The main scope has a LL of 0. The display is initialized at display[0] pointing to the head of the main activation record. Note that the dynamic link for this record will be null as it has nowhere to return to. Any initilized variables are also pushed in to the stack, these also act as the global variables within that program.


Suppose we are currently at the lexical level curLL.


If a procedure or function occurs next, we get the address of the top of the stack from the machine (MSP) then set it to the display[curLL+1]. We then set the dynamic link for that record to be display[curLL-1]. We initialize arguments and variables respetive to functions and procedures as before. Note that procedures do not have a return data, thus set the return data to null.


As our programs are sequential, it implies we can use the same activation record for all the minor scopes as they will only refere to variables in sequential order. We will create a record just like before, however, we will not increment the lexical level and thus not create a new display pointer. We will simply give it the same dynamic link as the main scope it is currently sitting in.


## Integer and Boolean Constants, Text Constants

I'm not too sure what they are really asking here. The machine itself tells us how they are represented. Maybe something like this?

>Integers will be stored in the word as is. As the integers have a bound of -32767 to +32767 and the memory is 16 bits, it will align. 


#Expressions

##Constants

Since the constants have been pushed onto stack during parsing the execution. We can simply use POP to access them during the expression evaluation. For example: a != True, PUSH MACHINE_TRUE will be executed when parsing the expression and later we can use POP to access its value

##Scalar variables

Since the code genaration happens after the semantic analysis, we already have a symbol table created for this program. For each scalar variable in the program we can store the lexical level and offset. Then during code generation we can look up into our symbol table and get its lexical level and offset from the table and use ADDR operation to access the scalar variable.

Example: let's say a has been declared as MACHINE_TRUE and stored in our symbol table where its lexical level is 0 and offset is 4. Then we want to execute a == False.

//TODO ADD ACCESS TO SYMBOL TABLE
ADDR 0 4 (get the address of a)
LOAD

##Arrays

Our language only supports one-dimensional array. Then in order to declare an array with bounds, we have to allocate memory for that array. Let's say we have a declared array A[5]. And we want to access A[3]. We already know the base address of the array and then program executes following commands to access A[3]

PUSH 3
PUSH wordsize
MULT
PUSH base_addr
ADD
LOAD

##Arithmetic operations

To do the arithmetic operations, we have to first access the left and right handside of the expression and pop and push them onto the top of the stack. After that we call MULT,DIV,ADD,SUB operators respectively. Example: x declared as 5 and y declared as 2 and assume they have been already accessed and placed on the top of the stack. 

x*y
PUSH X
PUSH Y
MULT

##Comparison operators

We do the same thing as above in the arithmetic operations, access the variables and place them on the top of the stack. Then call LT, EQ to do comparisons. Assume x and y has been declared and placed on the top of stack.

x >= y

push y
push x
LT
push y
push x
EQ
OR

##Boolean operations

Since we do not have a AND instruction, we can use De Morgan's law to replace AND statement with OR.  A and B = neg(neg(A) or neg(B)). Again as above, we access the variables and place them on top of the stack and call appropriate instruction.

A AND B

PUSH A
NEG
PUSH B
NEG
OR 
NEG

##Conditional expressions

For conditional expression, we access the value of conditional expression and place it on top of the stack and compare it with True. If it is an if-else statement, push the address of first instruction in else block and do branch false. If it is an ordinary if statement with an else clause, then place first address of the first instruction after if statement on top of the stack and do branch false.

if a > 5:
  b = 1
else:
  b = 2

Let's assume value of a > 5 has been placed on top of the stack

PUSH MACHINE_TRUE
EQ
PUSH addr_else (the address of first instruction in else clause)
BF
