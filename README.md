# featurevisor-example-java

A simple Java application that uses Featurevisor Java SDK.

Learn more about Featurevisor: https://featurevisor.com

## Installation

Clone the repository, and install the dependencies:

## Setup `settings.xml`

Create a new file in this repository at `~/.m2/settings.xml` with the following content:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <servers>
    <server>
      <id>github</id>
      <username>YOUR_GITHUB_USERNAME</username>
      <password>YOUR_GITHUB_TOKEN</password>
    </server>
  </servers>

</settings>
```

You can generate a new GitHub token here with `read:packages` scope: https://github.com/settings/tokens

This file is meant to not be committed to the repository, as `.gitignore` is configured to ignore it.

## Install dependencies

```
$ mvn install
```

## Compile

```
$ mvn clean compile
```

## Usage

```
$ mvn exec:java -Dexec.mainClass="com.example.App"
```

## Featurevisor project

Uses this Featurevisor project to fetch the configuration from: https://github.com/featurevisor/featurevisor-example-cloudflare
