# GS CodeNarc Extension

A CodeNarc extension library for gosu. 

Inspired by Guidewire **GosuCodeNarcStaticAnalysisTool**.

## Pre-Requisites

* Gradle 6.0.1

## Rules

| Rule                        | Description                                                  | Priority | Property Name       | Property Type | Property Default |
| --------------------------- | ------------------------------------------------------------ | -------- | ------------------- | ------------- | ---------------- |
| GosuClassSize               | Lines of code within a Gosu source code file, including comments. | 2        | maxLines            | int           | 1200             |
| GosuCommentedOutCode        | Check for commented out code.                                | 2        |                     |               |                  |
| GosuCyclomaticComplexity    | This class performs a basic complexity check of Gosu functions. | 1        | maxMethodComplexity | int           | 7                |
| GosuFindStatement           | Lines of code which use the find statement, which can be performance intensive. | 1        |                     |               |                  |
| GosuFunctionParameterLength | Check for the maximum allow parameter count.                 | 2        | maxParameters       | int           | 4                |
| GosuFunctionSize            | Lines of code within a Gosu function, including comments.    | 2        | maxLines            | int           | 25               |
| GosuGetCountUsage           | Incorrect usage of getCount() == 0                           | 3        |                     |               |                  |
| GosuIllegalImports          | Illegal imports rule. Check for violations of the illegal imports rule. | 2        |                     |               |                  |
| GosuInternalImports         | Internal imports rule. Check for usage of com.guidewire.* internal classes. | 3        |                     |               |                  |
| GosuJavaStyleLineEnding     | Java style lind ending \';\' has been used.                  | 3        |                     |               |                  |
| GosuNestedIf                | Nested if rule. Check for violations in nested if statement depth. | 1        | maxNestedDepth      | int           | 4                |
| GosuObjectEquality          | Incorrect usage of checking for object equality by using Objects themselves instead of PublicID | 1        |                     |               |                  |
| GosuReturnCount             | Check for a maximum return statement count from Gosu functions. | 1        | maxReturnCount      | int           | 4                |
| GosuUnusedFunctionParameter | Unused function parameter within a Gosu function.            | 2        |                     |               |                  |
| GosuUnusedImports           | Unused imports rule. Check for violations of the unused imports rule. | 2        |                     |               |                  |
| GosuUnusedFunctionMethod    | Unused private function within a Gosu class.                 | 2        |                     |               |                  |
| GosuVariableScope           | Detects usage of request or session scoped variables.        | 3        |                     |               |                  |

## The codenarc.xml File

The **codenarc.xml** holds the ruleset configuration for codenarc. The ruleset for gosu is as follows:

```xml
<ruleset xmlns="http://codenarc.org/ruleset/1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://codenarc.org/ruleset/1.0 http://codenarc.org/ruleset-schema.xsd"
	xsi:noNamespaceSchemaLocation="http://codenarc.org/ruleset-schema.xsd">
	
	<ruleset-ref path='rulesets/gosu.xml'>
  
		<rule-config name='GosuClassSize'>
			<property name='maxLines' value='1200' />
		</rule-config>
		
		<rule-config name='GosuFunctionSize'>
			<property name='maxLines' value='25' />
		</rule-config>
		
		<rule-config name='GosuReturnCount'>
			<property name='maxReturnCount' value='4' />
		</rule-config>
		
		<rule-config name='GosuCyclomaticComplexity'>
			<property name='maxMethodComplexity' value='7' />
			<property name='priority' value='1' />
		</rule-config>
		
		<rule-config name='GosuNestedIf'>
			<property name='maxNestedDepth' value='2' />
		</rule-config>
		
		<rule-config name='GosuFunctionParameterLength'>
			<property name='maxParameters' value='4' />
		</rule-config>
		
		<!--You can exclude rules from being executed as follows-->
		<!--<exclude name='GosuFunctionSize' />-->
	
	</ruleset-ref>

</ruleset>
```

## Usage in Gradle

1. Create the **codenarc.xml** and store in the following directory:

   ```
   <PROJECT_DIR>/config/codenarc
   ```

   > **PROJECT_DIR** is where the **build.gradle** can be found.
   
2. Create or update the **gradle.properties** to have the following:

   ```properties
   #The maximum number of priority 1 violations to fail the build.
   maxPriority1Violations = 0
   
   #The maximum number of priority 2 violations to fail the build.
   maxPriority2Violations = 0
   
   #The maximum number of priority 3 violations to fail the build.
   maxPriority3Violations = 0
   ```

   > Update these three properties accordingly to your needs.

3. Create the **rulesets directory** as follows:

   ```
   <PROJECT_DIR>/src/main/resources/rulesets
   ```

4. Copy the [gosu.xml](https://github.com/rcw3bb/gs-codenarc-ext/blob/master/src/main/resources/rulesets/gosu.xml) into the newly created **rulesets directory**:

5. In your **build.gradle**, apply the plugin **org.gosu-lang.gosu** as follows:

   ```groovy
   apply plugin: "org.gosu-lang.gosu"
   ```

   > If you are having problem on this, you can access the following template as reference:
   >
   > https://github.com/rcw3bb/template-gosu-library 
   >
   > *Use the branch **gosu-15** if you want to use the more recent gosu.*

6. Add the following to your **buildscript dependencies**:

   ```groovy
   classpath group: 'org.codenarc', name: 'CodeNarc', version: '1.5'
   classpath group: 'xyz.ronella.gosu', name: 'gs-codenarc-ext', version: '1.0.0'
   ```

   *Example*

   ```groovy
   buildscript {
       repositories {
           mavenCentral()
       }
       dependencies {
           classpath group: 'org.codenarc', name: 'CodeNarc', version: '1.5'
           classpath group: 'xyz.ronella.gosu', name: 'gs-codenarc-ext', version: '1.0.0'
       }
   }
   ```

7. Create a **codenarcGosu** ant task as follows:

   ```groovy
   ant.taskdef(name: 'codenarcGosu', classname: 'org.codenarc.ant.CodeNarcTask') {
       classpath {
           buildscript.configurations.classpath.files.each { ___file ->
               fileset(file: ___file)
           }
       }
   }
   ```

8. Create a **codenarcMainGosu** gradle task as follows:

   ```groovy
   task codenarcMainGosu {
       doLast {
           ant.codenarcGosu(ruleSetFiles: file("${projectDir}/config/codenarc/codenarc.xml").toURI().toString(),
                   maxPriority1Violations: maxPriority1Violations,
                   maxPriority2Violations: maxPriority2Violations,
                   maxPriority3Violations: maxPriority3Violations) {
               report(type: 'html') {
                   option(name: "outputFile", value: "${buildDir}/reports/codenarc/main/codenarc.html")
                   option(name: "title", value: "Gosu Library Report")
               }
               sourceSets.main.allGosu.srcDirs.each { ___file ->
                   fileset(dir: ___file)
               }
           }
       }
   }
   ```

9. If everything is good, you can now execute the following the gradle command:

   ```
   gradle codenarcMainGosu
   ```

   > The report will be available in the following directory:
   >
   > ```
   > build/reports/codenarc
   > ```

## The Template Gosu Library

An alternative of configuring this yourself with your gradle project is just use the template from the following:

https://github.com/rcw3bb/template-gosu-library 

> Be sure to use the branch **gosu-15** *(i.e. the master branch doesn't have the gs-conenarc-ext configuration)*.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## [Build](BUILD.md)

## [Changelog](CHANGELOG.md)

## Author

* Ronaldo Webb
