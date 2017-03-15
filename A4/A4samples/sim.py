import sys

class Machine:

	def __init__(self):
		self.memory = []
		self.display = []
		self.msp = 0

	def ADDR(self, LL, ON):
		a = int(self.display[LL])
		self.PUSH(a + ON)

	def LOAD(self):
		a = int(self.POP())
		if len(self.memory) <= a:
			raise Exception("LOAD: memory[a] out of bound.")
		else:
			self.PUSH(self.memory[a])

	def STORE(self):
		v = self.POP()
		a = int(self.POP())
		self.memory[a] = v

	def PUSH(self, V):
		if V == None:
			V = sys.float_info.min
		self.memory.append(V)

	def PUSHMT(self):
		self.PUSH(self.msp)

	def SETD(self, LL):
		while len(self.display) <= LL: self.display.append(0)
		self.display[LL] = self.POP()

	def POPN(self):
		n = int(self.POP())
		while 0 < n:
			self.POP()
			n -= 1

	def POP(self):
		return self.memory.pop() if 0 < len(self.memory) else None

	def DUPN(self):
		n = int(self.POP())
		v = self.POP()
		while 0 < n:
			self.PUSH(v)
			n -= 1

	def DUP(self):
		top = self.memory[len(self.memory)-1]
		self.PUSH(top)

	def BR(self):
		a = int(self.POP())
		return a

	def BF(self):
		a = int(self.POP())
		v = int(self.POP())
		if v != 1: return a

	def NEG(self):
		t = int(self.POP()) * -1
		self.PUSH(t)

	def _OPP(self):
		y = int(self.POP())
		x = int(self.POP())
		return (x,y)	

	def ADD(self):
		(x,y) = self._OPP()
		self.PUSH(x + y)

	def SUB(self):
		(x,y) = self._OPP()
		self.PUSH(x - y)

	def MUL(self):
		(x,y) = self._OPP()
		self.PUSH(x * y)

	def DIV(self):
		# (x,y) = self._OPP()
		y = float(self.POP())
		x = float(self.POP())	
		if y == 0: raise Exception("Divide by 0")
		self.PUSH(int(x / y))

	def EQ(self):
		(x,y) = self._OPP()
		self.PUSH(1 if x == y else 0)

	def LT(self):
		(x,y) = self._OPP()
		self.PUSH(1 if x < y else 0)

	def OR(self):
		(x,y) = self._OPP()
		self.PUSH(x or y)

	def SWAP(self):
		x = self.POP()
		y = self.POP()
		self.PUSH(x)
		self.PUSH(y)

	def PRINTC(self):
		c = int(self.POP())
		print("\tPrinted Char: " + ord(c))

	def execute(self, inst):
		# Execute instruction
		S = inst[0].upper()
		if S.find("ADDR") > -1:
			return self.ADDR(int(inst[1]), int(inst[2]))
		elif S.find("LOAD") > -1:
			return self.LOAD()
		elif S.find("STORE") > -1:
			return self.STORE()
		elif S.find("PUSHMT") > -1:
			return self.PUSHMT()
		elif S.find("PUSH") > -1:
			return self.PUSH(inst[1])
		elif S.find("SETD") > -1:
			return self.SETD(int(inst[1]))
		elif S.find("POPN") > -1:
			return self.POPN()
		elif S.find("POP") > -1:
			return self.POP()
		elif S.find("DUPN") > -1:
			return self.DUPN()
		elif S.find("DUP") > -1:
			return self.DUP()
		elif S.find("BR") > -1:
			return self.BR()
		elif S.find("BF") > -1:
			return self.BF()
		elif S.find("NEG") > -1:
			return self.NEG()
		elif S.find("ADD") > -1:
			return self.ADD()
		elif S.find("SUB") > -1:
			return self.SUB()
		elif S.find("MUL") > -1:
			return self.MUL()
		elif S.find("DIV") > -1:
			return self.DIV()
		elif S.find("EQ") > -1:
			return self.EQ()
		elif S.find("LT") > -1:
			return self.LT()
		elif S.find("OR") > -1:
			return self.OR()
		elif S.find("SWAP") > -1:
			return self.SWAP()
		elif S.find("PRINTC") > -1 or S.find("WRITE") > -1:
			return self.PRINTC()

	def print(self, bound):
		L = len(self.memory)
		print("\n\tDisplay:", self.display)
		print("\tMemory:")
		for i in range(0, min(bound, L)):
			print("\t"+str(i)+": "+str(self.memory[i]))
		# Print last few if bound
		if bound and bound < L:
			print("\t...")
			for i in range(max(0, L-5), L):
				print("\t"+str(i)+": "+str(self.memory[i]))


def parse(line):
	# If it's a comment, don't do anything
	if line[0] == "#" or len(line) < 1:
		return None
	else:
		# Parse out inline comments
		n = line.find("#")
		if -1 < n: line = line[:n]
		# Strip spaces at the end 
		# and seperate instruction and arg
		line = line.strip().split(" ")
		# Fill rest with none
		while(len(line) < 3): line.append(None)
		# Convert any "UNDEFINED" into None
		for i in range(0, len(line)):
			if 0 <= str(line[i]).find("UNDEFINED"):
				line[i] = None
		return line

if __name__ == '__main__':
	# Get file
	# ARGUMENT STYLE
	# python sim.py [file] [run without prompt until line number] [number of memory to show]

	if len(sys.argv) < 2: raise Exception("Please specify a bytecode file!")

	M = Machine()
	f = open(sys.argv[1], "r")
	lines = [l for l in f]
	runTill = int(sys.argv[2]) if 3<=len(sys.argv) else None
	bound = int(sys.argv[3]) if 4<=len(sys.argv) else 20
	stepByStep = True

	lineNumber = 0
	while(lineNumber < len(lines)):
		instr = parse(lines[lineNumber])

		if (runTill is None or runTill < lineNumber+1) and instr != None:
			u = input("->")
			stepByStep = False if u is "r" else True

		if instr != None:
			print("\n#"+str(lineNumber+1)+": ", instr)
			result = M.execute(instr)
			M.print(bound)
			if 0<=instr[0].find("BR") or 0<=instr[0].find("BF"):
				if result != None:
					lineNumber = result-1
					print("\n\tBRANCHING TO: #" + str(result))

		lineNumber += 1