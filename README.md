# Health Facility Registry Mediator
[![Java CI Badge](https://github.com/SoftmedTanzania/hfr-mediator/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/SoftmedTanzania/hfr-mediator/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Coverage Status](https://coveralls.io/repos/github/SoftmedTanzania/hfr-mediator/badge.svg?branch=development)](https://coveralls.io/github/SoftmedTanzania/hfr-mediator?branch=development)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/647b7d4897a443b7af7fd160f148c81b)](https://www.codacy.com/gh/SoftmedTanzania/hfr-mediator/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SoftmedTanzania/hfr-mediator&amp;utm_campaign=Badge_Grade)

An [OpenHIM](http://openhim.org/) mediator for handling system integration  with Health Facility Registry.

# Getting Started
Clone the repository and run `npm install`

Open up `src/main/resources/mediator.properties` and supply your OpenHIM config details and save:

```
  mediator.name=HFR-Mediator
  # you may need to change this to 0.0.0.0 if your mediator is on another server than HIM Core
  mediator.host=localhost
  mediator.port=4000
  mediator.timeout=60000

  core.host=localhost
  core.api.port=8080
  # update your user information if required
  core.api.user=openhim-username
  core.api.password=openhim-password
```

To build and launch our mediator, run

```
  mvn install
  java -jar target/hfr-mediator-0.1.0-jar-with-dependencies.jar
```