default: clean build run

build:
	javac *.java

run:
	java Othello

clean:
	$(RM) *.class *.ctxt
