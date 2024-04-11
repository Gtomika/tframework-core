# Versioning and release

This file documents the versioning conventions and the release process for this project.

## Versioning

Semantic versioning is used, such as `0.1.1`. Additionally, pre-releases have 
the suffix `-pre` at the end of the version. For example `0.1.1-pre`.

Note that version is not in `build.gradle`, instead, a fixed `0.0.0` can be found there. 
Version is controlled by **git tags**. Gradle will find the version based on the 
latest available git tag.

## Release

Release process is automated with GitHub Actions. It can be triggered by pushing a 
git tag to the remote.

- If a semantic versioned tag such as `0.1.1` is pushed, it will trigger the release workflow.
It can be seen [here](../.github/workflows/release.yaml). Only tags pointing to `master` 
branch can be released. This will create a proper GitHub release and upload the JARs to it.
- If a semantic versioned tag such as `0.1.1-pre` is pushed, it will trigger the pre-release workflow.
It can be seen [here](../.github/workflows/pre-release.yaml). This will create a GitHub pre-release and 
upload the proper JARs to it.

The result of the release process will be a GitHub release, which includes the 
tag that triggered it, the release notes (auto generated) and the JAR files. This 
release can then be used and downloaded with JitPack. 