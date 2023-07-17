# Getting Started

Details of the Tests
- Performed GET, POST and DELETE requests for GitHub REST API for a user 
- used the Bearer Toekn for authentication
- Also validated error codes which gets generated for invalid url or already deleted entity etc.
- Used logging where required with info messages

Assumptions
- Intellij Idea already installed
- Latest Java already Installed
- Cucumber plugin integrated with Intellij Idea

Installation
- Clone the Repo "https://github.com/Raj03042023/SpringBootTest"
- Open the Folder in IntelliJ Idea as a Project (pom.xml)
- Update the GitHub Bearer Token in "src\main\resources\application.properties" path with the supplied new key in the email
- Open the Terminal, from the 'SpringBootTest' folder enter 'mvn clean install test' followed by Return Key

Execution
- Now Open the Feature File "GitHubAPITests.feature" in path "SpringBootTest/src/test/resources" and run the feature as shown in the picture "Running Tests from Cucumber Feature.png"
- Results will appear as per picture "Execution Results.png"


