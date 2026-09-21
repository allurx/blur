# Blur

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

* A non-empty `regexp` overrides `startOffset` and `endOffset`. Each non-empty whole match is masked; capturing groups do not select a separate masking region. When `regexp` is empty, the offsets specify how many UTF-16 `char` units to preserve at the beginning and end. They must be non-negative and together must not exceed the input length.
* Masking preserves the UTF-16 length of a string. A false `condition` or a regular expression with no match leaves the value unchanged. Invalid regular expressions or offsets used for masking raise an exception; failures do not fall back to the original value.
* Custom `Condition` implementations are shared between calls and must be stateless or thread-safe.
* Object traversal and copying follow [annotation-parser](https://github.com/allurx/annotation-parser). Inherited fields require `@Cascade(inherited = true)`; final and transient fields of ordinary classes are not processed. Supported container implementations and instance creation also follow that library's rules.

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

[Apache License 2.0](https://github.com/allurx/blur/blob/master/LICENSE.txt)
