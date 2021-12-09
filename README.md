# Kotlin-RestAssured Test Automation

This is a sample API project for Rest Assured with Maven Kotlin DSL & JUnit 5

## Introduction
All the Test Cases kept in this repository. Developers can select the test cases which they need to execute during 
their build Pipeline(by adding a tag, eg: `pipeLine1`).

# Prerequisites
This requires [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) v11 and Maven.


## Open the Project

You can open the project by any IDE (Select `pom.xml` file when opening the project)

In this project you can find two branches:
- master - Contain up-to-date code
- develop - In progress development of the Test cases


## Set up the Project

You can set your project configurations in `config.yml`. Please make sure you DO NOT commit sensitive information in this file.
You can pass those data via your CI/CD tools during the execution.

## Continuous Integration/Continuous Deployment with Jenkins

Here I have used Jenkins as my CI/CD tool. You can edit `Jenkinsfile` based on your requirement.
How to create [Jenkinsfile](https://www.jenkins.io/doc/book/pipeline/jenkinsfile/).


## Running Test Suites

- Regression tests (excluding wip tests) - `mvn clean site -P regressionTest`
- Developer PipeLine tests (excluding wip tests) - `mvn clean site -P pipeLine1_Test`
- All test cases - `mvn clean install site test`

Default test suite will be `regressionTest`.

# Test Reports

You can find following types of reports:

- "Dependency Information" Report `target/site/dependency-info.html`
- "Dependency Management" Report  `target/site/dependency-management.html`
- "About" Report  `target/site/index.html`
- "Plugin Management" Report  `target/site/plugin-management.html`
- "Plugins" Report `target/site/plugins.html`
- "Project Summary" Report   `target/site/summary.html`
- "Team" Report    `target/site/team.html`
- "Test Execution Summary" Report    `target/site/surefire-report.html`

## Adding custom test tasks

Custom test suits can be added in `pom.xml`. 
 
Current test suits are as follows.
  
- General Regression Test Task (excluding wip tests)  - regressionTest
- Developer PipeLine Test Task (excluding wip tests)  - pipeLine1_Test
- 
# Create Pull Request

When you crete a PR from the GitHub, you need to mark a checklist which related to your merge.

![PR-Template](src/main/resources/pr_template.png)

## Adding code owners

You can add code owners username to the `/.github/CODEOWNERS` file. So they will automatically get an email 
for any pull requests.

# References for learn Kotlin

* https://kotlinlang.org/docs/maven.html
* https://openclassrooms.com/en/courses/5774406-learn-kotlin/5931841-enhance-your-classes
* https://kotlinlang.org/docs/reference/classes.html
