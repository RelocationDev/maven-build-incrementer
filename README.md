# 🔨🐸 Build Incrementer Maven Plugin

## Description
The **Build Incrementer Maven Plugin** (`rBuildIncrementer`) is a lightweight Maven plugin that automatically tracks and increments build numbers for your project. It supports:
- **Total Build Count**: Tracks the total number of builds across all versions.
- **Version-Specific Build Count**: Tracks the number of builds for a specific project version.
- **Dynamic Placeholders**: Exposes build numbers as Maven properties (`${rBuilds}` and `${rBuildsVersion}`) for use in artifact names, manifests, or other configurations.

#

## Features
- Automatically increments **total build numbers** and **version-specific build numbers**.
- Generates or updates a `build-counter.properties` file in the project root to persist build counts.
- Allows placeholders (`${rBuilds}`, `${rBuildsVersion}`) to be used across the Maven lifecycle.
- Lightweight and integrates seamlessly into any Maven project.

#

## Installation

1. **Add the Plugin to Your `pom.xml`**:
   ```xml
   <build>
       <plugins>
           <plugin>
               <groupId>dev.relocation</groupId>
               <artifactId>rBuildIncrementer</artifactId>
               <version>1.0.0-stable</version>

               <executions>
                   <execution>
                       <goals>
                           <goal>increment-build</goal>
                       </goals>
                   </execution>
               </executions>
           </plugin>
       </plugins>
   </build>
   ```

2. **Add Dependencies** (if needed):
   Ensure you have the required dependencies for your project, such as `spigot-api` or other libraries.

#

## Usage

### Placeholder Variables
The plugin provides two placeholders that can be used in your `pom.xml`:
- **`${rBuilds}`**: Represents the total build count across all versions.
- **`${rBuildsVersion}`**: Represents the build count for the current version.

### Example `pom.xml`
```xml
<build>
    <finalName>my-artifact-build.${rBuilds}</finalName>

    <plugins>
        <plugin>
            <groupId>dev.relocation</groupId>
            <artifactId>rBuildIncrementer</artifactId>
            <version>1.0.0-stable</version>
            <executions>
                <execution>
                    <goals>
                        <goal>increment-build</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>

        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-jar-plugin</artifactId>
            <configuration>
                <archive>
                    <manifestEntries>
                        <Build-Number>${rBuilds}</Build-Number>
                        <Build-Number-For-Version>${rBuildsVersion}</Build-Number-For-Version>
                    </manifestEntries>
                </archive>
            </configuration>
        </plugin>
    </plugins>
</build>
```

#

## Outputs

1. **`build-counter.properties`**
   The plugin generates or updates a `build-counter.properties` file in the project root with the following structure:
   ```
   totalBuilds=15
   buildsForVersion_1.0.0=5
   buildsForVersion_1.1.0=10
   ```

2. **Dynamic Artifact Naming**:
   If configured, your artifact will be named dynamically:
   ```
   my-artifact-build.15.jar
   ```

3. **Manifest Entries**:
   The build numbers are added as metadata in the artifact manifest:
   ```
   Build-Number: 15
   Build-Number-For-Version: 5
   ```

#

## Configuration Options

1. **Build File Location**:
   By default, the plugin generates the `build-counter.properties` file in the root of your project. This can be customized if needed by modifying the plugin.

2. **Lifecycle Phase**:
   The plugin runs during the `initialize` phase by default, ensuring placeholders are available for later phases (e.g., `package`).

#

## Example Maven Command

Run the following command to trigger the build increment and generate your artifact:
```bash
mvn clean package
```

#

## Contributing

1. Fork the repository.
2. Create a feature branch.
3. Submit a pull request with your changes.
