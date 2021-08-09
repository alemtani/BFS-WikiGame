# BFS-WikiGame
Use BFS to get from one Wikipedia article to another using internal links.

## Usage
You can change the start and target articles on lines 96 and 97 (respectively).

## Running the Application (for Eclipse IDE)
If you navigate to the `src/main/java` folder and find the `App.java` inside the main package, you can right click and go to Run As --> Java Application. This will print the pathway on the console for the server, going from the starting article to the target one top-down.

## Running using Maven
Make sure you have Maven installed [here](https://maven.apache.org/download.cgi). To compile the project after making changes, open a terminal into the project folder and run

`mvn compile`

Then, you can run the Maven app by calling

`mvn exec:java -Dexec.mainClass="com.wikigame.sixdegrees.App"`

## Warning: BFS is very inefficient. Even articles with only two "degrees of separation" may take over an hour to find a pathway.

## Helpful Resources (for developers)
- [HTTP Request in Java](https://www.baeldung.com/java-http-request)
- [MediaWiki API](https://www.mediawiki.org/wiki/API:Main_page)
