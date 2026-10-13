.PHONY: build run clean

build:
	javac *.java

run: build
	java Driver activity.log

clean:
	rm -f *.class *.log
