Progetto **TreniCal**
===================

Le precedenti modifiche erano state sviluppate su una repository diversa,
poiché la build Maven iniziale non era configurata correttamente. Tutte le
classi sono state migrate qui e il progetto può essere compilato con Maven.

Per sperimentare l'interazione delle classi lato server è presente il programma
`Prova.java`. Prima di eseguirlo assicurarsi di avere il driver JDBC per SQLite
(`sqlite-jdbc`) disponibile.

Compilazione ed esecuzione di esempio:

```bash
mvn package
java -cp target/classes:PATH/TO/sqlite-jdbc.jar it.trenical.server.Prova
```

In assenza del driver SQLite l'esecuzione terminerà con l'errore
"No suitable driver".
