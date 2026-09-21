# Blur

English | [简体中文](README.zh-CN.md)

Blur is a Java library for annotation-based masking of sensitive data in strings, object fields, collections, arrays, and maps.
It is designed to be flexible and easy to use, and supports the following types of data blurring:

* **String**
* **Name**
* **Password**
* **Email Address**
* **Phone Number**
* **ID Card Number**
* **Bank Card Number**
* **Cascading Blurring**
* **Custom Annotation-Based Blurring**

# Usage

## JDK Version

Blur requires JDK 25 or later. For projects using older JDK versions, see the [v2.4.6 user guide](https://github.com/allurx/blur/tree/v2.4.6) for the Java 8-compatible version.

## Maven Dependency

Replace `LATEST_VERSION` with the latest release listed on Maven Central:

<pre><code>&lt;dependency&gt;
    &lt;groupId&gt;io.allurx&lt;/groupId&gt;
    &lt;artifactId&gt;blur&lt;/artifactId&gt;
    &lt;version&gt;<a href="https://central.sonatype.com/artifact/io.allurx/blur">LATEST_VERSION</a>&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

## Example

### Object Field Blurring

Below is an example of a `Person` class containing some sensitive data fields and nested sensitive data fields.

```java
public class Person {

    @Name
    public String name = "allurx";

    @PhoneNumber
    public String phoneNumber = "19962000001";

    @Password
    public String password = "123456789";

    @Cascade
    public Father father;    

}
```
Simply annotate the sensitive data fields with the appropriate annotations like `@Name`, `@PhoneNumber`, `@Password`, etc. 
If the field contains an object that requires cascading blurring, mark it with the `@Cascade` annotation. 
To apply the configured masking rules to the object's fields and return a new instance, use the following:

```java
var person = Blur.blur(new Person());
```

### Value Blurring

Blurring sensitive data in `String`, `Collection`, `Array`, or `Map` types is just as simple and easy.

```java
void blur() {

    // String
    var v1 = Blur.blur("123456@qq.com", new AnnotatedTypeToken<@Email String>() {
    });
    assertEquals("1*****@qq.com", v1);

    // Collection
    var v2 = Blur.blur(Stream.of("123456@qq.com").collect(Collectors.toList()), new AnnotatedTypeToken<List<@Email String>>() {
    });
    v2.forEach(s -> assertEquals("1*****@qq.com", s));

    // Array
    var v3 = Blur.blur(new String[]{"123456@qq.com"}, new AnnotatedTypeToken<@Email String[]>() {
    });
    Arrays.stream(v3).forEach(s -> assertEquals("1*****@qq.com", s));

    // Map
    var v4 = Blur.blur(Stream.of("allurx").collect(Collectors.toMap(s -> s, s -> "123456@qq.com")), new AnnotatedTypeToken<Map<@Name String, @Email String>>() {
    });
    v4.forEach((s1, s2) -> {
        assertEquals("a*****", s1);
        assertEquals("1*****@qq.com", s2);
    });
}
```
In this example, constructing the `AnnotatedTypeToken` for the blurred objects is necessary to accurately capture the actual type of the object being blurred along with the appropriate annotations.

## Notes

* `startOffset` and `endOffset` retain Unicode code points. They must be nonnegative and total at most the input's code point count. Existing UTF-16 offsets may need adjustment for supplementary characters.
* Output preserves `String.length()`: `@Name` produces `𠮷田 → 𠮷*` and `张𠮷 → 张**`. Code points are not grapheme clusters; unpaired surrogates are not repaired.
* A nonempty `regexp` overrides offsets. False conditions and unmatched patterns leave values unchanged; invalid patterns or offsets used for masking throw an exception. See [masking options](src/main/java/io/allurx/blur/annotation/Strings.java) for matching and placeholder rules.
* Custom conditions are shared between calls and must be stateless or thread-safe.
* Object traversal, container support, and instance creation follow [annotation-parser](https://github.com/allurx/annotation-parser). Inherited fields require `@Cascade(inherited = true)`; ordinary classes' final and transient fields are not masked.

### JPMS

For JPMS applications, open packages containing private fields to the parser in `module-info.java`:

```java
module com.example.app {
    requires io.allurx.blur;
    opens com.example.model to io.allurx.annotation.parser;
}
```

Replace the example module and model package names. Classpath applications do not need this configuration.

# How It Works

Blur uses [annotation-parser](https://github.com/allurx/annotation-parser) to parse masking annotations and traverse supported objects and containers.
For more details, you can refer to the project documentation.

# Extension

If your application is built on Spring Boot and you prefer not to manually call blurring methods in your code, 
the [blur-spring-boot](https://github.com/allurx/blur-spring-boot) library can be very helpful. You can find more information in the project documentation.

# Build

Use JDK 25 or later and Maven 3.9.x. From the repository root, run:

```sh
mvn -B -ntp clean verify
```

To also build sources and Javadoc without signing:

```sh
mvn -B -ntp -Prelease "-Dgpg.skip=true" clean verify
```

CI and releases use [allurx-build](https://github.com/allurx/allurx-build).

# License

[Apache License 2.0](LICENSE.txt)
