# Build

## Pre-requisite

* Java 11

* [IntelliJ 2019.3.4](https://download.jetbrains.com/idea/ideaIC-2019.3.4.exe) *(This may require Java 8)*

* Create or update **<USER_HOME>\\.gradle\\gradle.properties** to have the following properties:

    ```properties
    artifactoryUsername=<VALID_USERNAME>
    artifactoryPassword=<VALID_PASSWORD>
    ```
    > If you don't have access to my **artifactory**, update all the **repositories section** in the **build.gradle** file **after cloning**, from:
    >
    > ```groovy
    > repositories {
    >        maven {
    >            url 'https://repo.ronella.xyz/artifactory/java-central'
    >            credentials {
    >                username "${artifactoryUsername}"
    >                password "${artifactoryPassword}"
    >            }
    >        }
    >    }
    >    ```
    >    
    >    to
    >    
    >    ```groovy
    >    repositories {
    >  	mavenCentral()
    > }
    >```

## Install Gosu Plugin

1. **Open** your **IntelliJ 2019.3.4**.
2. Select **File-> Settings** menu.
3. Click **Plugins**.
4. Click **Marketplace**.
5. Type **OS Gosu**.
6. Click the **Install** button.
7. Click the **Restart IDE** button.
8. Click the **Restart** button.

## Testing

1. In your **IntelliJ**, **open the directory** where you've **cloned repository**.

2. **Open the project structure** by clicking: **File** -> **Project Structure** menu.

3. On the **Project Settings/Project**, ensure that the **Project SDK** is set to **Java 11** and the **Project language level** to **11**.

4. On the **Project Settings/Modules**, ensure that the **Language level** for **main** and **test** are **11**.

5. **Click the OK button** at the bottom.

6. **Find the gradle tab** that is usually on the **right side** and **expand it by clicking it**.

7. Expand the **Tasks folder** -> **verification**.

8. **Double click** the **check** task.

   > All the test must pass.

## Building

> Before doing this you must follows the procedure for testing.
>

1. **Find the gradle tab** that is usually on the **right side** and **expand it by clicking it**.

2. Expand the **Tasks folder** -> **build**.

3. **Double click** the **build** task.

   > The build must pass.

## Packaging

> Before doing this you must follows the procedure for testing.

1. **Find the gradle tab** that is usually on the **right side** and **expand it by clicking it**.

2. Expand the **Tasks folder** -> **build**.

3. **Double click** the **jar** task.

   > The **jar file** will be generated in the **build/libs** directory. 