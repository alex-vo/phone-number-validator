## DISCLAIMER!
 
Application does not validate phone number length, because otherwise it would require maintaining possible length rules for each country.

## Execution

To run application on port 8080 execute:
```
./gradlew bootRun
``` 
Application will be available via http://localhost:8080

To run tests execute:
```
./gradlew test
```
To run tests including end-to-end UI test execute:
```
./gradlew test -PwithE2E=true
```  
End-to-end UI test has not been tested on Windows and MacOS machines, hence excluded by default.

After execution test reports are available under `build/reports/tests` 
