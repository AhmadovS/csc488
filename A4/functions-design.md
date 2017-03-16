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
After a routine reaches it's return statement, it sets the value for the `return value` if it's a function, cleans up all the stack values down to, but excluding `ADDR $L 3` (so only return address, return value, static link and dynamic link are on stack), we then update display[$callerL], where $callerL is lexic level of the caller, before branching to the caller's code. Once branched to the caller's code, the caller is responsible for update the rest of the display.

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
# We need to remember that only display[$curL] is valid,
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