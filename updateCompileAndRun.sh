#!/bin/bash
find . -type f -name '*.class' -delete;
git pull &&  javac -cp json-20171018.jar:pircbot-1.5.0/pircbot.jar:commons-io-2.6/commons-io-2.6.jar:src:ressources:. src/main/Main.java && java -cp json-20171018.jar:pircbot-1.5.0/pircbot.jar:commons-io-2.6/commons-io-2.6.jar:src:ressources: main.Main
