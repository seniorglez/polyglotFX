compile_llvm:
	${LLVM_TOOLCHAIN}/clang -shared notifier.c -lgraalvm-llvm `pkg-config --cflags --libs libnotify` -o notifier
compile_java: compile_llvm
	javac -d target --module-path ${PATH_TO_FX} --add-modules javafx.controls -cp . src/com/seniorglez/polyglotfx/*/*.java
compile: compile_llvm compile_java
package: compile
	mv notifier target
	( cd target ; echo Main-Class: com.seniorglez.polyglotfx.PolyglotFX >manifest.txt ; jar cvfm First.jar manifest.txt com/seniorglez/polyglotfx/*)
run:
	( cd target ; java --module-path ${PATH_TO_FX} --add-modules javafx.controls -jar First.jar)
clean:
	rm -r target