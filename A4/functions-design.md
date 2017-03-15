WORK IN PROGRESS

# 3. Functions and Procedures

## Overview
NOTES:
- routine: either a function or a procedure.
- In the byte codes below, variables prefixed with $ (e.g. $L) are computed at compile time.
- All lexical levels and return addresses are computed at compile time.
- for a lexical level $L, ADDR $L 0 points to the bottom of acivation record for that lexical leve.
- For any routine at lexical level $L, ADDR $L 0 points to beginning of it's activation record (which is the address of return value).

Imagine we are currently executing routine P which calls routine Q. P has the responsibility to update the display only for the lexical level of P. It then creates the activation record for Q and evaluates its arguments, and then branches to the first instruction of the routine Q.

After Q finishes running it sets the return value and pops all the values from it's activation record except return value and return address. It then branches to return address of it's caller. And the caller has the resposibility of using the return value which is now on top of stack or POP it if it's not used.

## The activation record
Both functions and procedures have the same activation record structure, diagrammed below.

ACTIVATION RECORD:

--------------------
|local variables, ...
|-------------------
|nth parameter
|-------------------
|...
|-------------------
|1st parameter
|-------------------
|dynamic link (pointer to previous activation record - maynot be parent.)
|-------------------
|static link (pointer to lexical parent acitvation record.)
|-------------------
|return address (points to code segment to return to)
|-------------------
|return value (procedures would leave return value as UNDEFINED)
--------------------

## Calling a routine
Let $L be the lexical scope of the callee and $curL lexical scope of caller.

Before calling a routine (or evaluating its arguments), we first emit codes for pushing the bottom four fields (return value, return address, static link, dynamic link) of the activation record of the callee, and then evaluate the arguments (if any) for the callee.

To call a routine, the caller first updates the display[$L] for the lexical scope of callee. display[$L] is going to point to top of stack, since that space is soon going to be allocated for the acivation record of the callee.

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

Note that since a routine can only call routines that are it's immediate childs or of a lexical lever lower than it's own (i.e. ascendents and siblings), display[$L-1] always contains the link to activation record of the statically containing scope.

Afterwards the caller pushes the activation record for the callee, sets the return address, static and dynamic links and fills in the parameters, and then branches to it. Note that return value can be accessed by `ADDR $L 0` and first parameter (if available) is at `ADDR $L 4`.

## Display management
Let $L be the lexical scope of the callee.
After branching to the callee, we need to remember that only display[$L] is valid, and all lower display fields need to be updated. We do this by recursively following the static links.

```
# Caller has just branched to this code
# We now update the rest of display

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

# Now we're done with updating the display
# We now run first "actual" instruction of the callee.
```

## Procedure and function exit code.



