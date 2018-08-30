## Compile
```js
mvn clean compile
```

## Run tests
```js
mvn test
```

## Run main application
```js
mvn exec:java -Dexec.mainClass=it.bitrock.GooseGame
```

## One line compile and run
```js
mvn clean compile test exec:java -Dexec.mainClass=it.bitrock.GooseGame
```