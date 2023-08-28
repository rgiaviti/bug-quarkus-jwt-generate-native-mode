# JWT Builder Not Working on Native Mode

## Key Generation

```bash
openssl genrsa -out PrivateKey.pem 2048
openssl rsa -in PrivateKey.pem -outform PEM -pubout -out PublicKey.pem
```

Then, I moved the generated keys to `resources/META-INF/keys`.

## application.yml

```yaml
smallrye:
  jwt:
    sign:
      key:
        location: "META-INF/keys/PrivateKey.pem"
    encrypt:
      key:
        location: "META-INF/keys/PublicKey.pem"
    verify:
      key:
        location: "META-INF/keys/PublicKey.pem"
```

## Build and Run on JVM

### Java Version, Kotlin Version and Vendor

- Java 17.0.8
- Kotlin 1.9

```txt
openjdk version "17.0.8" 2023-07-18
OpenJDK Runtime Environment Temurin-17.0.8+7 (build 17.0.8+7)
OpenJDK 64-Bit Server VM Temurin-17.0.8+7 (build 17.0.8+7, mixed mode, sharing)
```

### Running

```bash
quarkus build
java -jar ./build/quarkus-app/quarkus-run.jar
```

### Request

```bash
curl --request POST --url http://localhost:8080/generate-token
```

### Response (200 OK)

```json
{
	"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiIzOGYwOTc1YS0zNjk1LTQxMDYtODUwNC1kZmZlYTk4ZGU5NWEiLCJ1cG4iOiJqb2huZG9lQGV4YW1wbGUuY29tIiwiZ3JvdXBzIjpbIlNPTUUgUk9MRSJdLCJpc3MiOiJBIElzc3VlciIsImlhdCI6MTY5MzIyMjA2NiwiZXhwIjoxNjkzMjI1NjY2LCJlbWFpbCI6ImpvaG5kb2VAZXhhbXBsZS5jb20iLCJnaXZlbl9uYW1lIjoiSm9obiIsImZhbWlseV9uYW1lIjoiRG9lIiwiZnVsbF9uYW1lIjoiSm9obiBEb2UiLCJzb21lX2N1c3RvbV9jbGFpbSI6ImEgdmFsdWUiLCJqdGkiOiJkOTE0ZmZmMy1mOGIzLTRlOTYtOGIzZC0zZTE5NjJlODg5YjgifQ.TBUy4RE82KcNop-Wfhbbml8vnZbO8zuY0DgnW9zIcfXYOBl60XubAMFspEzG90nV_au7IYiSvXIZW4AJV8XJhU56c9jC9LRB4c_z0TLfFnBAmxjNyBQ-3D8T2W3RwENnK9lwsMq789T4SuUzlNB8ouxDKO9x5UmDuKqym_SuH3z5PXDrfHqWbaMoiYK7v8ftKuK2HtuYlAokVO7dvFh1vRIUYoRGy1VmJSfk8QIRqXRGm5rWE9IkJFw5UDzcAF6L8WAWP4cYBeGzE49Xy3lG3J6MsY_FHN8ehUaa_ItKs-dANx-yW0oWJcmdS3kPjjN6kkuKGUTbcCsvxF5-IXdPHw"
}
```

## Compile to Native and Run

```bash
sdk use java 23.r17-mandrel
quarkus build --native
```



<details>
  <summary>Output of compilation</summary>
```txt
========================================================================================================================
GraalVM Native Image: Generating 'jwt-native-1.0.0-SNAPSHOT-runner' (executable)...
========================================================================================================================
[1/8] Initializing...                                                                                    (5.4s @ 0.17GB)
 Java version: 17.0.7+7, vendor version: Mandrel-23.0.0.0-Final
 Graal compiler: optimization level: 2, target machine: x86-64-v3
 C compiler: gcc (linux, x86_64, 11.4.0)
 Garbage collector: Serial GC (max heap size: 80% of RAM)
 3 user-specific feature(s)
 - io.quarkus.runner.Feature: Auto-generated class by Quarkus from the existing extensions
 - io.quarkus.runtime.graal.DisableLoggingFeature: Disables INFO logging during the analysis phase
 - org.eclipse.angus.activation.nativeimage.AngusActivationFeature
[2/8] Performing analysis...  [******]                                                                  (43.0s @ 1.15GB)
  12,713 (87.55%) of 14,521 types reachable
  18,488 (59.59%) of 31,023 fields reachable
  65,096 (57.21%) of 113,775 methods reachable
   3,913 types,   226 fields, and 3,197 methods registered for reflection
      63 types,    68 fields, and    55 methods registered for JNI access
       4 native libraries: dl, pthread, rt, z
[3/8] Building universe...                                                                               (5.8s @ 2.12GB)
[4/8] Parsing methods...      [**]                                                                       (4.6s @ 1.98GB)
[5/8] Inlining methods...     [***]                                                                      (2.2s @ 2.38GB)
[6/8] Compiling methods...    [******]                                                                  (36.3s @ 1.68GB)
[7/8] Layouting methods...    [**]                                                                       (4.6s @ 1.61GB)
[8/8] Creating image...       [***]                                                                      (6.6s @ 2.23GB)
  26.11MB (49.29%) for code area:    42,035 compilation units
  26.53MB (50.09%) for image heap:  322,751 objects and 40 resources
 336.20kB ( 0.62%) for other data
  52.96MB in total
------------------------------------------------------------------------------------------------------------------------
Top 10 origins of code area:                                Top 10 object types in image heap:
  12.49MB java.base                                            6.03MB byte[] for code metadata
   1.88MB c.f.jackson.core.jackson-databind-2.15.2.jar         3.12MB java.lang.Class
   1.29MB svm.jar (Native Image)                               2.95MB java.lang.String
 946.59kB jwt-native-1.0.0-SNAPSHOT-runner.jar                 2.62MB byte[] for general heap data
 847.60kB modified-io.vertx.vertx-core-4.4.4.jar               2.55MB byte[] for java.lang.String
 691.16kB org.jboss.resteasy.resteasy-core-6.2.4.Final.jar     1.07MB com.oracle.svm.core.hub.DynamicHubCompanion
 575.93kB com.fasterxml.jackson.core.jackson-core-2.15.2.jar 726.26kB byte[] for reflection metadata
 552.41kB org.yaml.snakeyaml-2.0.jar                         691.17kB java.util.HashMap$Node
 421.34kB io.netty.netty-buffer-4.1.94.Final.jar             584.82kB java.lang.String[]
 398.10kB io.netty.netty-codec-http-4.1.94.Final.jar         458.91kB c.o.svm.core.hub.DynamicHub$ReflectionMetadata
   5.82MB for 96 more packages                                 4.89MB for 3277 more object types
------------------------------------------------------------------------------------------------------------------------
Recommendations:
 HEAP: Set max heap for improved and more predictable memory usage.
 CPU:  Enable more CPU features with '-march=native' for improved performance.
------------------------------------------------------------------------------------------------------------------------
                        7.4s (6.7% of total time) in 73 GCs | Peak RSS: 3.67GB | CPU load: 6.26
------------------------------------------------------------------------------------------------------------------------
Produced artifacts:
 /home/rgiaviti/Dev/Projects/jwt-native/build/jwt-native-1.0.0-SNAPSHOT-native-image-source-jar/jwt-native-1.0.0-SNAPSHOT-runner (executable)
 /home/rgiaviti/Dev/Projects/jwt-native/build/jwt-native-1.0.0-SNAPSHOT-native-image-source-jar/jwt-native-1.0.0-SNAPSHOT-runner-build-output-stats.json (build_info)
========================================================================================================================
Finished generating 'jwt-native-1.0.0-SNAPSHOT-runner' in 1m 49s.
```
</details>

### Executing the Native App

```bash
./build/jwt-native-1.0.0-SNAPSHOT-runner
```

### Request

```bash
curl --request POST --url http://localhost:8080/generate-token
```

### Response (200 OK)

```json
{}
```