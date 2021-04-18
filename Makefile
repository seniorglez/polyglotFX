compile:
	javac --module-path ${PATH_TO_FX} --add-modules javafx.controls HelloFX.java
	${LLVM_TOOLCHAIN}/clang notifier.c -lgraalvm-llvm `pkg-config --cflags --libs libnotify` -o notifier
run: compile
	java --module-path ${PATH_TO_FX} --add-modules javafx.controls HelloFX
clean:
	rm HelloFX.class notifier
