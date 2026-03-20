JAVAC = javac
JAVA = java
SRC_DIR = src/main/java
OUT_DIR = ../EXE

# Agafa TOTS els .java de totes les subcarpetes
SOURCES = $(shell find $(SRC_DIR) -name "*.java")

# Main class can be overridden: `make run MAIN=your.package.Main`
MAIN ?= Main

# Optional: additional args for KMeansDriver (e.g. `make execute_KMeans ARGS="case1 case2"`)
ARGS ?=

# Collect any local jars (from build/libs or libs) so javac/java can use them if present
JARS = $(wildcard build/libs/*.jar) $(wildcard libs/*.jar)

.PHONY: all compile clean run execute_KMeans test

# `all` must only compile the application with javac (do NOT call Gradle here)
all: compile

compile:
	mkdir -p $(OUT_DIR)
	@if ls build/libs/*.jar 1> /dev/null 2>&1 || ls libs/*.jar 1> /dev/null 2>&1; then \
		JARS=$$(for f in build/libs/*.jar libs/*.jar; do [ -e "$$f" ] && printf "%s:" "$$f"; done | sed 's/:$$//'); \
		echo "Compiling with classpath: $$JARS"; \
		$(JAVAC) -cp "$$JARS" -d $(OUT_DIR) $(SOURCES); \
	else \
		mkdir -p libs; \
		if command -v curl > /dev/null 2>&1; then \
			echo "Downloading gson to libs/gson-2.10.1.jar..."; \
			curl -fSL -o libs/gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar || true; \
		elif command -v wget > /dev/null 2>&1; then \
			echo "Downloading gson to libs/gson-2.10.1.jar..."; \
			wget -O libs/gson-2.10.1.jar https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar || true; \
		else \
			echo "No curl/wget available to download dependencies; please run 'make test' (Gradle) or add jars to libs/"; \
		fi; \
		if ls build/libs/*.jar 1> /dev/null 2>&1 || ls libs/*.jar 1> /dev/null 2>&1; then \
			JARS=$$(for f in build/libs/*.jar libs/*.jar; do [ -e "$$f" ] && printf "%s:" "$$f"; done | sed 's/:$$//'); \
			echo "Compiling with classpath: $$JARS"; \
			$(JAVAC) -cp "$$JARS" -d $(OUT_DIR) $(SOURCES); \
		else \
			echo "Compiling without external jars"; \
			$(JAVAC) -d $(OUT_DIR) $(SOURCES); \
		fi; \
	fi

clean:
	rm -rf $(OUT_DIR) build
	find $(SRC_DIR) -name "*.class" -delete

run: all
	@JARS_RUNTIME=$$(for f in build/libs/*.jar libs/*.jar; do [ -e "$$f" ] && printf "%s:" "$$f"; done | sed 's/:$$//'); \
	if [ -n "$$JARS_RUNTIME" ] ; then \
		echo "Running with classpath: $$JARS_RUNTIME"; \
		$(JAVA) -cp "$(OUT_DIR):$$JARS_RUNTIME" $(MAIN); \
	else \
		$(JAVA) -cp "$(OUT_DIR)" $(MAIN); \
	fi

# Execute KMeansDriver after compiling; pass optional ARGS
execute_KMeans: all
	@echo "Running KMeansDriver with ARGS='$(ARGS)'"
	@JARS_RUNTIME=$$(for f in build/libs/*.jar libs/*.jar; do [ -e "$$f" ] && printf "%s:" "$$f"; done | sed 's/:$$//'); \
	if [ -n "$$JARS_RUNTIME" ] ; then \
		echo "Running with classpath: $$JARS_RUNTIME"; \
		$(JAVA) -cp "$(OUT_DIR):$$JARS_RUNTIME" KMeansDriver $(ARGS); \
	else \
		$(JAVA) -cp "$(OUT_DIR)" KMeansDriver $(ARGS); \
	fi

# `test` should invoke Gradle (prefer the wrapper)
test:
	@if [ -f gradlew ] ; then \
		chmod +x gradlew || true; \
		./gradlew test --rerun-tasks; \
	elif [ -f gradlew.bat ] ; then \
		cmd.exe /c gradlew.bat test --rerun-tasks; \
	else \
		echo "No gradle wrapper found (skipping tests)"; \
	fi
