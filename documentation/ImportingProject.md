# Importing the software component in your project

## Maven

To add Open Universiteit Refactoring Advice in your software project that supports Maven, please add the following in the `pom.xml` file:
```xml
<dependency>
  <groupId>nl.ou.refactoring</groupId>
  <artifactId>nl.ou.refactoring.advice</artifactId>
  <version>[0.6.0,)</version>
</dependency>
```
You may decide how to specify the version, but it is recommended to always keep using the latest version. If breaking changes are introduced, it is recommended to keep using the latest minor version until your software has been adapted to the breaking changes.

If you wish to use the GitHub feed, please add the following in either your `pom.xml` or `settings.xml`:
```xml
<repository>
	<id>github</id>
	<url>https://maven.pkg.github.com/Open-Universiteit-Refactoring-Diagnosis/GraphAdvice</url>
	<snapshots>
		<enabled>true</enabled>
	</snapshots>
</repository>
```
You may decide which `id` to use or whether to support snapshots.