# Introduction

Lorem ipsum

# Storage

We will push activation records for each scope, preducure and function of the program into the runtime stack. Each activation record contains the following words: return data, dynamic link (pointer to the start of the previous record) and then the following arguments and/or variables in that specific scope. The records are in this specific order, meaning the first word in a record is the return data, followed by the dynamic link then the arguments and/or variables. Once the record needs to be used, you simply pop all but the bottom two words. Push the result into the return data and use the dynamic link to go back to the previous record.

![](images/storage-1)

Each scope will have an associated lexical level (LL) in reference to the main scope. The main scope has a LL of 0. The display is initialized at display[0] pointing to the head of the main activation record. Note that the dynamic link for this record will be null as it has nowhere to return to. Any initilized variables are also pushed in to the stack, these also act as the global variables within that program.


Suppose we are currently at the lexical level curLL.


If a procedure or function occurs next, we get the address of the top of the stack from the machine (MSP) then set it to the display[curLL+1]. We then set the dynamic link for that record to be display[curLL-1]. We initialize arguments and variables respetive to functions and procedures as before. Note that procedures do not have a return data, thus set the return data to null.


As our programs are sequential, it implies we can use the same activation record for all the minor scopes as they will only refere to variables in sequential order. We will create a record just like before, however, we will not increment the lexical level and thus not create a new display pointer. We will simply give it the same dynamic link as the main scope it is currently sitting in.


## Integer and Boolean Constants, Text Constants

I'm not too sure what they are really asking here. The machine itself tells us how they are represented. Maybe something like this?

>Integers will be stored in the word as is. As the integers have a bound of -32767 to +32767 and the memory is 16 bits, it will align. 