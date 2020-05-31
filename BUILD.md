# Build

## Pre-requisite

* Java 8

* Create or update **<USER_HOME>\\.gradle\\gradle.properties** to have the following properties:

    ```properties
    artifactoryUsername=<VALID_USERNAME>
    artifactoryPassword=<VALID_PASSWORD>
    ```

    > If you don't have access to my **artifactory**, update all the **repositories section** in the **build.gradle** file **after cloning**, from:
    >
    > ```
    > repositories {
    >      maven {
    >            url 'https://repo.ronella.xyz/artifactory/java-central'
    >            credentials {
    >                username "${artifactoryUsername}"
    >                password "${artifactoryPassword}"
    >            }
    >      }
    >  }
    >    ```
    >    
    >    to
    >    
    >    ```
    >  repositories {
    > 	mavenCentral()
    > }
    >```

## Testing

Run the following command to test the application:

```
gradlew clean check
```

> The preceding command must be run from the location where you've cloned the repository.

## Building

Run the following command to build the application:

```
gradlew build
```

> The preceding command must be run from the location where you've cloned the repository.

## Packaging

Run the following command to package the application:

```
gradlew jar
```

> The preceding command must be run from the location where you've cloned the repository.