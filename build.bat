@echo off
javac -cp src/ -d out/ src/*java
jar cf out/MSortApp out/MSortApp.class