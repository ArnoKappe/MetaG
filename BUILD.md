# Build MetaG

## Requirements
- IDE [IntellJ IDEA](https://www.jetbrains.com/idea/)
- JDK 17 (e.g. [Eclipse Temurin](https://adoptium.net/))

## Build MetaG with GUI
1. Download a version:
    - Release: [`MetaG-2022.1.0.zip`](https://github.com/dluxe0/MetaG/archive/refs/tags/2022.1.0.zip)
    - Current: [`MetaG-main.zip`](https://github.com/dluxe0/MetaG/archive/refs/heads/main.zip)
2. Unzip the cloned code
3. Open in IntellJ
   > check Project SDK is set to 17+ (Project Structure -> Project -> SDK)
4. Set Gradle JVM to Project SDK (Gradle -> Gradle Settings -> Gradle JVM)
5. Gradle -> Tasks -> build -> build
## Build MetaG without GUI
1. Download a version:
    - Release: [`MetaG-2022.1.0.zip`](https://github.com/dluxe0/MetaG/archive/refs/tags/2022.1.0.zip)
    - Current: [`MetaG-main.zip`](https://github.com/dluxe0/MetaG/archive/refs/heads/main.zip)
2. Unzip the cloned code
3. Open in IntellJ
   > check Project SDK is set to 17+ (Project Structure -> Project -> SDK)
4. Set Gradle JVM to Project SDK (Gradle -> Gradle Settings -> Gradle JVM)
5. Change `mainClass` in `build.gradle` from
   
       application {
       mainModule = 'eu.kwrhannover.jufo.metag'
       mainClass = 'eu.kwrhannover.jufo.metag.MetaGApplication'
       }

   to

       application {
               mainModule = 'eu.kwrhannover.jufo.metag'
               mainClass = 'eu.kwrhannover.jufo.metag.MetaG'
       }
6. Gradle -> Tasks -> build -> build


> In this version, settings can be changed in the `MetaG.properties` file. See [**Settings**](README.md#settings)
## Run
Gradle -> Tasks -> application -> `run`

## Create executable file
Gradle -> Tasks -> distribution -> (choose)