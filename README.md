# Java Client Library for WikiPathways

## Introduction
Java library for the WikiPathways web service.

This library reimplements most of the methods from [the original WikiPathways API client](https://github.com/wikipathways/wikipathways-api-client-java).

Instead of accessing the WikiPathways pathway database from Java through the REST web service API (http://webservice.wikipathways.org), the library now access the database via [WikiPathways JSON API](https://www.wikipathways.org/json/) and [WikiPathways GitHub assets](https://github.com/wikipathways/wikipathways-assets) for better performance and maintainability.

It provides nearly identical functionality to [rWikiPathways](https://github.com/wikipathways/rWikiPathways), an R package in Bioconductor available to R programmers.

## Dependencies
You need the following tools:

- A computer with Windows, macOS, or Linux
- [JDK 11](https://www.oracle.com/technetwork/java/javase/downloads/jdk11-downloads-5066655.html)
- [Maven 3](https://maven.apache.org/) (Tested with version 3.8.6)

## Usage
Compile the code and run the tests with:

```bash
cd org.wikipathways.client
mvn clean install
```

## New Methods
This library includes the following new methods:

- getCommunities
- getPathwaysByCommunity
- getCommunitiesByPathway

## Deprecated Methods
The method for maintaining a local cache of WikiPathways pathways is now deprecated. The following methods are also deprecated:

- getRecentChanges
- login
- updatePathway
- createPathway
- removeCurationTag
- saveCurationTag
- getCurationTags
- getCurationTagsByName
- getCurationTagHistory
- getColoredPathway
- findInteractions
- saveOntologyTag
- removeOntologyTag
- getUserByOrcid
