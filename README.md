# jshell-maven-plugin
This maven plugin helps you to explore your codes and dependencies in your maven project with in jshell -- the official Java REPL tool.

## Installing
(Now I'm working on deploying this to Central Repository. Until done, please clone this repository and install by your hand.)

```
JAVA_HOME=/path/to/your/jdk9 mvn clean install
```
It requires version `3.3.9` or something like that for `mvn`.

## Getting started
### Run plugin from mvn command line without editing pom.xml
Run following
```
JAVA_HOME=/path/to/your/jdk9 mvn net.java.openjdk.shinyafox:jshell-maven-plugin:1.0-SNAPSHOT:compile
```

### OR Configure your pom.xml to reduce your future types
Add following to your pom.xml:
```
    <build>
        <plugins>
            <plugin>
                <groupId>net.java.openjdk.shinyafox</groupId>
                <artifactId>jshell-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
            </plugin>
        </plugins>
    </build>
```

and type following in your project:
```
JAVA_HOME=/path/to/your/jdk9 mvn jshell:compile
```

It requires version `3.3.9` or something like that for `mvn`.

## Results
```
[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building mavenproject1 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- jshell-maven-plugin:1.0-SNAPSHOT:jshell (default-cli) @ mavenproject1 ---
[INFO] Starting jshell with /home/bitter_fox/.m2/repository/com/google/guava/guava/21.0/guava-21.0.jar
|  JShellへようこそ -- バージョン9-ea
|  概要については、次を入力してください: /help intro

jshell> [Type your code as you like and enjoy!!]

```
