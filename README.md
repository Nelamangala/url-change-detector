# url-change-detector
Gets web page located at given url, compares it with archived content. Sends notification email 
if changes are above 10%. Performs this repeatedly for given interval.


### Build project

Requires Maven.
```mvn clean install```

### Project execution

Execute with below command

`java -jar <jar> <url-to-monitor> <emailNotificationReceipient> <checkIntervalInMillis>`

Example:

```java -jar target/url-change-detector-0.0.1-SNAPSHOT.jar http://localhost:8081/ ganesh.sit@gmail.com 10000```


### Tool Verification
Integration test spins up a web container with a page at http://localhost:8080/ that changes its content on every url request. The change detector detects changes on this page and calls mocked up email notifier.

### Key Highlights
1. Uses open source tool JSOUP to get url contents and sanitize it.
2. Uses Levenshtein distance to compare previous vs new
3. Uses distance algorithm from [Java String similarity algorithms](https://github.com/tdebatty/java-string-similarity#jaccard-index) . (Levenshtein algorithm can be swapped with better performing algorithms with more testing involved.)
4. Unit tests validate notifications will be sent when content changes are above 10% only.
5. Email smtp properties can be altered from application.properties
6. Uses java timer to schedule detect change for given interval. 