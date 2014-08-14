ot-base
=======

General Java classes for Open Tree of Life development with Neo4j

Dependencies
-----

ot-base depends on the jade repo at: https://github.com/FePhyFoFum/jade. You must install jade as a maven dependency, which is straightforward. Navigate to where you would like to put the jade repo, and do the following:
```
git clone git@github.com:FePhyFoFum/jade.git
cd jade
sh mvn_install.sh
```
Failing that, see the readme file for the [jade repo](https://github.com/FePhyFoFum/jade) for instructions.

Install
-----

Installing the packages in this repo is done in the same way as installing the jade package that this repo depends on. To install this package as a local maven repository:

1. git clone the ot-base repo locally
2. cd into the ot-base directory and run:
```
sh mvn_install.sh
```
3. copy and paste the following code into the pom.xml file of any local maven project to access the ot-base packages

```
<dependency>
  <groupId>org.opentree</groupId>
  <artifactId>ot-base</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```
