# BodyMeasurementAPI

[![Build Status](https://travis-ci.org/pussinboots/BodyMeasurement.svg?branch=master)](https://travis-ci.org/pussinboots/BodyMeasurement)
[![Heroku](https://heroku-badge.herokuapp.com/?app=body-measurement&root=body/meta/status)](https://body-measurement.herokuapp.com/index.html)

[![Unit Tests](http://unitcover.herokuapp.com/api/pussinboots/BodyMeasurement/testsuites/badge)](https://unitcover.herokuapp.com/#/builds/pussinboots/BodyMeasurement/builds)

## Introduction

Implementation of a rest API that stores body measurements for example body tempreture or blood presure lets say from healts or a sports app. 

## Build Requirements

* Java 1.8+
* Maven 3+

## Frameworks

* Jersey 2
  Rest Service Framework
* Ormlite 5
  Simple Object Relational Mapping framework
* hsqldb 1.8
  InMemory database used for the integration tests
* postgresql 9
  JDBC driver for the postgresql database used in production
* lombok 1.16 
  For Reducing Boilerplate code
  
## Runtime Requirements

* Java 1.8+
* Web Container
* PostgreSQL 9

## Model

The following picture show the json scheme of the responses of that api.
![json response model](docs/uml/json_response.png?raw=true "json response model")

## Demo 

The running instance is deployed on Heroku [here](https://body-measurement.herokuapp.com/index.html). That link list all available rest 
method's and how they can be used. 

## Packaging

The application is packaged as an war archive.

## Deployment

The project is build with Travis and after a succesfull build also deployed to Heroku.
