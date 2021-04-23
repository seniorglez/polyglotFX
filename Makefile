compile_llvm:
	${LLVM_TOOLCHAIN}/clang -shared notifier.c -lgraalvm-llvm `pkg-config --cflags --libs libnotify` -o notifier
compile_java:
	javac --module-path ${PATH_TO_FX} --add-modules javafx.controls PolyglotFX.java
compile: compile_llvm compile_java
run:
	java --module-path ${PATH_TO_FX} --add-modules javafx.controls PolyglotFX
clean:
	rm *.class notifier
