JAVAC = javac
SRC_DIR = src/main/java
OUT_DIR = src/main/java

# Agafa TOTS els .java de totes les subcarpetes
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

all:
	mkdir -p $(OUT_DIR)
	$(JAVAC) -d $(OUT_DIR) $(SOURCES)

clean:
	rm -rf out
	find src/main/java -name "*.class" -delete

run: all
	java -cp $(OUT_DIR) App

execute_KMeans: all
	java -cp $(OUT_DIR) KMeansDriver

test: 
	chmod +x gradlew
	./gradlew test --rerun-tasks