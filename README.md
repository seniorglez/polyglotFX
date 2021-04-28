# Polyglot ETL

This is a project is a code test for the Oracle Labs Internship program. One of the themes they proposed was to make a simple polyglot application. I have decided to approach this topic in such a way that my application solves a real-world problem.

This project is a simple messaging application that needs to solve two problems for which java is not the best option. The first of these problems is that we are going to receive the messages in JSON format and we are going to want to extract their values, for that we will use JS. And finally, we want to notify the user that new messages have arrived, so we will have to connect to one of the OS libraries using C.

I have decided to only implement the input of the messages since the objective of this project is to use polyglot programming and I believe that implementing more features will only add noise.

## Prerequisites
 
 For this explanation I am going to assume that you are using a Linux distro.

### GraalVM

First and foremost, get the GraalVM distribution form it's [Github release](https://github.com/graalvm/graalvm-ce-builds/releases/tag/vm-21.0.0.2). Then take your download to the desired path. On my case /otp

Unzip the archive:

```bash
tar -xzf graalvm-ce-java11-linux-amd64-21.0.0.2.tar.gz
```

It is time to define the environment variables (I'm defining them on /etc/profile):

```bash
export GRAALVM_HOME=/opt/graalvm-ce-java11-21.0.0.2    

export JAVA_HOME=$GRAALVM_HOME

export PATH=$JAVA_HOME/bin:$PATH
```

To make the changes take effect immediately you can use the source command on your configuration file.

```bash
source /etc/profile
```
Now if you run `java -version` you should get somethink similiar to:

```bash
openjdk version "11.0.10" 2021-01-19
OpenJDK Runtime Environment GraalVM CE 21.0.0.2 (build 11.0.10+8-jvmci-21.0-b06)
OpenJDK 64-Bit Server VM GraalVM CE 21.0.0.2 (build 11.0.10+8-jvmci-21.0-b06, mixed mode, sharing)
```

### JavaFX

To keep the project simple I am not going to use any kind of project control software, so we are going to simply download JavaFX as a jar from [Gluon's page](https://gluonhq.com/products/javafx/). 
I am using JavaFX Linux SDK version 11 in this example.

I have decided to save javaFX also in /opt but if you are not going to use it other than to run this project, I do not think it is worth doing the whole process, just export $PATH_TO_FX in your terminal.

The library is compressed in zip format so we first have to unzip it. I use unzip but use the package that is most convenient for you.

```bash
unzip openjfx-11.0.2_linux-x64_bin-sdk.zip
```

Now I'm going to move it to /otp which is where I have all my JVM tools.

```bash
sudo mv javafx-sdk-11.0.2 /otp
```

As it is convenient for me to have the library handy, I am going to define an environment variable PATH_TO_FX in / etc / profile but if you don't want to simply export it in terminal manually.

```bash
export PATH_TO_FX=/opt/javafx-sdk-11.0.2/lib/
```
Everything you need to compile and run the project is reflected in the Makefile, however, if you use different modules than the ones I have used, such as FXML, 
you should modify it.

### LLVM

Now that we have to install the LLVM tools, we must use the Graal component updater.

```bash
gu install llvm-toolchain
```

You also have to make sure that you have the [libnotify](https://archlinux.org/packages/?name=libnotify) library installed on your distribution. 
Don't worry, this package is included in most of the repositories of the popular distributions.

Since I have other c / cpp compilers installed and included in the path, I have defined an environment variable that points to the directory where the GraalVM ones are installed. If you don't do this be sure to modify the makefile to remove the reference to that variable.

```bash
export LLVM_TOOLCHAIN=$($GRAALVM_HOME/bin/lli --print-toolchain-path)
```

### Make

I have included a makefile to make it easier to compile and run the application so make sure you have [Make](https://archlinux.org/packages/core/x86_64/make/) installed.

## Compile and Run

The entire compilation and execution process is reflected within the Makefile.

To compile invoke the following command.

```bash
make package
```

To run the application, after compiling, invoke the following command.

```bash
make run
```

Finally, when you want to delete the compiled files invoke

```bash
make clean
```

## MIT LICENSE

Copyright (c) 2021 Diego Domínguez González

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
