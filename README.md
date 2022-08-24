<h1><img src="https://github.com/eggohito/eggolib/blob/1.19/src/main/resources/assets/eggolib/icon.png" height="192" width="192" alt="Mod icon" align="right">eggolib</h1>

[![JitPack](https://img.shields.io/jitpack/v/github/eggohito/eggolib)](https://jitpack.io/#eggohito/eggolib) [![GitHub issues](https://img.shields.io/github/issues/eggohito/eggolib)](https://github.com/eggohito/eggolib/issues) [![GitHub Pull Requests](https://img.shields.io/github/issues-pr/eggohito/eggolib)](https://github.com/eggohito/eggolib/pulls)

### Required on the client and the server!


eggolib is a Fabric mod library that extends the functionality of [Apoli](https://github.com/apace100/apoli) for funsies. This mod library adds new power, condition and action types to be used for developing Origins/Apoli datapacks.

[See here](https://eggolib.github.io) for the documentation for eggolib.

<br>

**Q: Are the versions for 1.18.x going to be updated?**
<br>
A: Probably not, since it'd be quite a task for me to maintain versions for different Minecraft versions.

**Q: Can you back-port this to X?**
<br>
A: The same answer from above still applies, but if you can or want to, feel free!

<br>


## Using as a dependency
You can use eggolib as a dependency by modifying the `gradle.properties` and `build.gradle` files of your project, like so: <br>
*(You can remove the `include` part if you don't want to include eggolib in your project)*

<br>

**`build.gradle`**

```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}

dependencies {
    modImplementation "com.github.eggohito:eggolib:${project.eggolib_version}"
    include "com.github.eggohito:eggolib:${project.eggolib_version}"
}
```

<br>

**`gradle.properties`**
```properties
eggolib_version=[INSERT VERSION HERE]
```
