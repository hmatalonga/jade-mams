#Jade Multi-agent meeting scheduling

[![License MIT](http://img.shields.io/badge/license-MIT-brightgreen.svg)](license.md)

## Introduction
This project was developed in the context of the course Group artificial intelligence from *AGH University of Science and Technology*.

## Project Overview
The problem concerns planning a meeting among several participants. We know from experience that setting up a meeting with more than 3-4 independent participants can be a tough job. This can be viewed as a constraint satisfaction problem, where each participant has time constraints resulting from their calendar and preferences.

For more information about the project visit the following link:
[http://luszpaj.pl/mams/](http://luszpaj.pl/mams/)

## Author
* Hugo Matalonga

### Prerequisites
* JDK 1.5.0 or better - [http://java.oracle.com/](http://java.oracle.com/)
* Apache Ant 1.9.2 or better - [http://ant.apache.org/](http://ant.apache.org/)
* JADE framework - [http://jade.tilab.com/](http://jade.tilab.com/)

### Building from the terminal 
1. `ant compile`
2. `ant run`

### Usage details
Information about parameters of the project can be found in `build.xml`.

Please notice that *JADE* framework `.jar` file is not included.

If you want to set different *JADE* framework path. Change the line: `<classpath location="jade/lib/jade.jar"/>` in compile target. By default it is set to `jade/lib/jade.jar`.

To add more agents. More info soon.
