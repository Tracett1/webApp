# cs122b-spring20-team-182
### Demo URL: 
cs122b-spring20-team-182 created by GitHub Classroom

### Deployment Instructions: 
1. Clone this repository using `git clone https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182.git`
2. Open IntelliJ -> Import Project -> Choose the project you just cloned (The root path must contain the pom.xml!) -> Choose Import project from external model -> choose Maven -> Click on Finish -> The IntelliJ will load automatically
3. In Tomcat Deployment Configuration, make sure the application context is: /cs122b_spring20_project1_api_example_war
4. Ensure the configurations are the same as Canvas' Tomcat setup configurations.
5. Username and passwords are valid if they are located in moviedb database 

*No additional frameworks from the class examples are used. Front-end frameworks are limited to Bootstrap.*

### Substring Matching Usage 
* Titles, directors and stars are queried for using sql query `LIKE % [any keyword] %`
* When browsing, characters are searched for using `LIKE [insert alphanumeric char]%`
* Asteriks are queried for using `REGEXP '^[a-zA-z0-9]` regext statement in the sql query
### Contributions of Members: 
Other than the help of online resources, class lectures and code examples, I've developed the Fabflix website on my own with 
no other parter or contributions. 
