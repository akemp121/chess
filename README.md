# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## This is my diagram for project 3:
[Link](
https://sequencediagram.org/index.html?presentationMode=readOnly&shrinkToFit=true#initialData=C4S2BsFMAIEEoB4HIDO0DCALSK0GVIAnANyOgBEQBDAc0KoFsAoJqgY2AHtCNwRIAdsBYAHKoVBsQYodAIkio8ZOlVZACTUATKISUSQUmcDlFihyPpXGKVYFVhs2OFCy12qAIyooYWzyx0nACuItAASpA0ICjA9KCcAizofILAALQAfPKkhABc0ADaAAqcsQC60AD0wb48ADoC0ADeAES1RAKMkK3QBb2tADTQrWK4AO7cWr39I8OtkAxUIOAzIyMAviw5RFmaAjpEBW0dhF0MPX3rQyNjKJOE01cD84vLq8+bLPuHhFk5FgKhCiMWARAAFJForEiJEAI7BHDAACU2zMFiy5A8jmcuAKNEgwAAqnVwacAHLdVFMLH2HEuLL+AoAMRAB2gJKItKo0E8AE9oBTuoFCCEwgBRBDOEQJJqFTlnbrQKjgYFULQCkTA3yyNnQdz2by+cosbn03Dpf7o5wFBXctEkDFWhT5OCqyDqvkAFSoAGtBJLpbKHblLZkUvwhAUACwABgAzNBGi1WhdcLRLv1xYRRa7TucYCq1RroPZ-QJWlsmIItKbsU4Gc7zDboAJguBwCGMZkzQ28dA2GqwQqyXVudTe7iUIzPAVYFotByxx4u85MfWpwVBx6wbBgsBMOCqPvMOO63S+9PMky4Au4Cf7UwAWu9tpdAUoaDYTh28ImD9dDDHZXROOoCzWF4RmPA8vU4csIK+J8zF2LIIzSAoACZY1jJMmlAzpugQm5WmgzBYPgz5KxYGsRTFXgPR4WARBEPg2DsEBEmSVIhCbI4inISAoDBSoqn8ENdkyAC+LaLRBMJHoqykv5smtSAClkoTIHBFIGPhRFYmpZ9IHXC9NwHKBxHBCcNwZa9ZwiRZOFIZUO0FOoUGGGhug85V2VI8jBBQAA6WjQmgQNIBlDikiQx0XxUl0Ch08QIqizjYtDVDuOAAoAFZsNwlM0xQDMIOzXMCnK7hzIYtkaH1DwjQU6iDlXYzXwOd96PESIUF-b43xQhLcmOKtgLDNCo2gLCcOTZoqxopggjCgAZThohiyaMmGviSjKYARN8XBosK-DFQuIj5juB4nlmKiMokpTjnaMDCMoq6fHuKYiMUwblKMgpwHWtlwTWja9KRQzVJMhxL3xQkRyFC5rNM2yb1Zdk7Q8XkBSRywltFMLUtlaAAEZz1hqcgNUgo2w7NrqcS6ByU4YBmRCA5iZO8S-iyyMcugONZrw1MXFK54qrzOpW1Z6AADMOemBbWoJuiucSaaKfNacm0BJcuRXDKnR210AEkBDYbhgQ4YpPpu9WYvGvn0MF2NSdO0X0wJMqc24Ao2UtnNIA4aBru+5XaxpGyLV1lssfsBme2jlAtyHSA9wPI8Hw8FHKbR+z0DT5VW0gcZSzgwQGY634CjBtlepERJfAGzqhuA578zeu75n8iuK0osbkN58Nsswgq5pegiLveqCTwC-u7oj0KwjBkI-y23jXUKchxRW8UvXFQ6XBO5NSO4EAAC92MSAoAB5e-LTIeerrqz8IS-r4EO+H8EJ--z+2OaloBAxoGvUG6014QwMonScLh1JyV3CeLOME+6521jOAoAlNKlmwMqOefdl7hSlJFYMRt4rtw5F0E858L6QC0A7Z+zsppxndhPYq4ssy+zzFQg8NC6H3UWkZMMT1oCr33L1fq-9W7DwofNRhI9+Zj2Fi0JeqtVqgmgAAcW8lxfmm8CiFE0fvESXkLiFTfh-WU398GP2fpJP6BQLFXysdAe+Njf4t1+IAwGoJtFplBr47yUCUQwOTvDYAGdDw-wEGgy8GDoAY0XAeQs7iYrLQlMQtKMVYEx2GnrOmnYyHtRNraHhmA+H0MyaQp2CiXYsI9uw72EsuEFGCGUipAiVY5J1nklskTHxCKTqjfsfBYh+JwFZLWcS7IFE0YSYuoyTCcDli5cAOCYCmJwFXex0ja6BLTBI8Af4lKMxGimTZKA1iFDaJs025A1ikwwvGaM8xxiYDAJABU4E+iQVaJ4cA7BfRfK7r8zZlILiL3KIPF0E1R7TXHiLC5VybndDuQ8p5LyRhvI+cC6eeRfn-MBbizMoLujgqAQMDYULoAtUjukjARdxm6LSPoooxQADyeAvQmKVBPMFXdrgbHMdQ9+zjorWJQbYh6ykREoouOSn6wreGis-hKsifc-4nN6UA7cdhIDjO0mncZwSoZxWMkMvO-YCQRKQdE2JVMZkJLZIuaJhCHZkymVTbxrZ2yFKEayok7TRV0IYdK2FijXasJFo0zMIxJatKDZffhqjumnL1v0w2gzunhINfy5Gnr85JWwGwX00AQArM2dASACBQRoCLB6DUbqqknQwgW3JAMBzYFwEyoppy+LdNDTUraBihaVDYWLJpnCKrQEreMMAmBoAlTMQWFyxYBTVtrb0FNyc00tgKaE4ZKcBxGu6OCPNkB7WFoZTuQsM7hS9pfnxQuN7jU4EbgIZuUivGsrlZANFVwECAYQNCzKtSpozQ9rc+5AGgOqPpQAKU4HqHtG8SlsqJNy6olaJ4sSoHyIg6BOBA0IBBAA6uoU2B8qgACEVqwHQAAaWIlB9FzyhWnxFZY8Vrjol-3GjsmuKZcP4cIIR4jZGKNUdo-Rpj8wWNXEeWxpV5SVUuLcZKjxX7ALaoKAAKyQwIA1+m2SvoRJDA9lqj3Wsicg9V5ZL0WkdYk9ZeCNNpMJhkoMJ1yZR0PbuoB+6H1ocDU4kNzb0pDrhfU8dXtY2tHjYKRNtCla0rbT0jtGaE69otdrHNp6oMOavDeLAwdS3lpc5WjdsRXBqM8yQltaX-O019dsih4yWZs0VoOoe4a6kIqKhOuLCXvCLmBGZ2InTI6pu9WwLtKAe30vdfGVrQ85zuk9D6cs3WYVMIFnGRMMWSqTrjS01dDaBRlkEJNizuXBQiANPq098cqCeVRVoYYltiOFfifORcpxy4ubYMEIOshNnbJEYhkz3QG5N3xicn9IGhrDvhcouR1ZWpAA
)


