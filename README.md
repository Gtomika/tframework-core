# TFramework

My personal project to create a Java framework for developing applications.

### Build

Gradle is used to manage dependencies and build the project.

### Code style

Code style is enforced through [Gradle Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle). To
apply spotless formatting to the code, run

```
./gradlew :spotlessApply
```

It will make sure the code is in correct format:

- Unused imports removed.
- Google Java code style applied.
- License header added for each class.
