#! /bin/sh
#  Location of directory containing  dist/compiler488.jar
WHERE=.
#  Compiler reads one source file from command line argument
#  Output to standard output 
java -jar $WHERE/dist/compiler488.jar -X -D a $1
exit 0

# on windows
# java -jar .\dist\compiler488.jar -D ax -T x .\tests\a3.488
