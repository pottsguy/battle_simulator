#!/usr/bin/env bash
# -ea = Enable Assertions
# -cp = set Class Path
#java -ea -cp ./target/db_test-1.0-SNAPSHOT-jar-with-dependencies.jar com.mycompany.app.App

mvn compile exec:java -Dexec.mainClass="dsm.Main"

