<h1 align="center"> Discount Engine </h1> <br>

<p align="center">
  This simple application will calculate discount on set of rules
</p>


## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Requirements](#requirements)
- [Quick Start](#quick-start)
- [Improvements](#improvements)




## Introduction

This service is responsible for discount calculation. The decision will be take on the provided set of rules.

## Features
* Standalone service
* Simple and small
* Take inputs from file i.e. input.txt and validates
* Basic business validations are added


## Requirements
The application can be run locally. Containerization is in progress

### Local
* [Java 16 SDK](https://www.oracle.com/java/technologies/downloads/#java16)
* [Maven](https://downloads.apache.org/maven/maven-3/3.8.1/binaries/)


## Quick Start
Make sure your maven is pointing to JAVA_HOME and JAVA_HOME is set to Java16 JDK. Import the project in any IDE and run through the IDE.

## Improvements
* Rules can be in yml files, code will import the yml and define the rules
* Dynamic behaviour can be enhanced
* Simple jar run with input-output file names as command line arguments
* Serverless architecture
* Integration with CICD i.e. jenkins / rancher
* Integration with metrics collector i.e. prometheus
* Integration with grafana for better visibility, observability and alerts
* 100% Unit test code coverage 
