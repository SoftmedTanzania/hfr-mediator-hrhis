# Health Facility Registry Mediator

[![Java CI Badge](https://github.com/SoftmedTanzania/hfr-mediator-hrhis/workflows/Java%20CI%20with%20Maven/badge.svg)](https://github.com/SoftmedTanzania/hfr-mediator-hrhis/actions?query=workflow%3A%22Java+CI+with+Maven%22)
[![Coverage Status](https://coveralls.io/repos/github/SoftmedTanzania/hfr-mediator-hrhis/badge.svg?branch=development)](https://coveralls.io/github/SoftmedTanzania/hfr-mediator-hrhis?branch=development)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/990f14d05bf448ef9038330e566d502a)](https://www.codacy.com/gh/SoftmedTanzania/hfr-mediator-hrhis/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=SoftmedTanzania/hfr-mediator-hrhis&amp;utm_campaign=Badge_Grade)

An [OpenHIM](http://openhim.org/) mediator for handling system integration with Health Facility Registry.

## 1. Dev Requirements

1. Java 1.8
2. IntelliJ or Visual Studio Code
3. Maven 3.6.3

## 2. Mediator Configuration

This mediator is designed to work with multiple systems that do not require significant message transformations or any
data validation to the message payload.

## 3. Pre-Deployment

**The file contents of `mediator.properties` and `mediator-registration-info.json` must be changed before deployment**

This idea behind this mediator is to deploy the same JAR with a different set of configurations parameters based on the
system to connect.

### 3.1 Configuration Parameters

The configuration parameters specific to the mediator and destination system can be found at

`src/main/resources/mediator.properties`

```
    mediator.name=HFR-Mediator
    mediator.host=localhost
    mediator.port=3015
    mediator.timeout=60000
    mediator.heartbeats=true
    
    core.scheme=openhim-scheme
    core.host=openhim-address
    core.api.port=8080
    core.api.user=root@openhim.org
    core.api.password=openhim-password
    
    destination.host=destination-system-address
    destination.api.port=destination-system-address-port
    destination.api.path=/destination-system-path
    destination.scheme=destination-system-scheme
```

The configuration parameters specific to the mediator and the mediator's metadata can be found at

`src/main/resources/mediator-registration-info.json`

```
    {
      "urn": "urn:uuid:83c54769-5622-4cec-9f58-ccfc0ad24382",
      "version": "0.1.0",
      "name": "HFR Mediator",
      "description": "Description",
      "endpoints": [
        {
          "name": "HFR Mediator Route",
          "host": "localhost",
          "port": "3015",
          "path": "/hfr",
          "type": "http"
        },
        {
          "name": "HFR Mediator Route",
          "host": "localhost",
          "port": "3015",
          "path": "/hfr-ack",
          "type": "http"
        }
      ],
      "defaultChannelConfig": [
        {
          "name": "HFR Mediator",
          "urlPattern": "^/hfr$",
          "type": "http",
          "allow": [
            "hfr-mediator"
          ],
          "routes": [
            {
              "name": "HFR Mediator Route",
              "host": "localhost",
              "port": "3015",
              "path": "/hfr",
              "type": "http",
              "primary": "true"
            }
          ]
        },
        {
          "name": "HFR ACK Mediator",
          "urlPattern": "^/hfr-ack$",
          "description": "An OpenHIM mediator for handling acknowledgments",
          "type": "http",
          "allow": [
            "hfr-mediator"
          ],
          "routes": [
            {
              "name": "ACK",
              "host": "localhost",
              "port": "3015",
              "path": "/hfr-ack",
              "type": "http"
            }
          ]
        }
      ]
    }
```

## 4. Deployment

To build and run the mediator after performing the above configurations, run the following

```
  mvn clean package -DskipTests=true -e source:jar javadoc:jar
  java -jar target/hfr-mediator-<version>-jar-with-dependencies.jar
```
