### Please Regrade: 
Project 1, Project 2 & Project 3 





### Demo URL: 
https://youtu.be/4JEOfqiLuTA

still need to update urls..... 

### Queries w/ Prepared Statements 
- Confirmation servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/ConfirmationServlet.java
- Dashboard servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/DashboardServlet.java
- DB Login servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/DBLoginServlet.java
- Login servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/LoginServlet.java
- Main servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/MainServlet.java
- Movies servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/MoviesServlet.java
- Payment servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/PaymentServlet.java
- Single Movie servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/SingleMovieServlet.java
- Single Star servlet https://github.com/UCI-Chenli-teaching/cs122b-spring20-team-182/blob/master/src/SingleStarServlet.java



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
Other than the help of online resources, class lectures and code examples, I, Tracey Tran, have developed the Fabflix website on my own with 
no other parter or contributions. 

UCI EMAIL: tracett1@uci.edu
STUDENT ID#: 45682491
