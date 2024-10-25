# Blur

Blur is a Java library for masking and obfuscating sensitive data in any data structure. 
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

Blur is built on JDK 21. For projects using JDK 1.8 or later, please refer to this [user guide](https://github.com/allurx/blur/tree/v2.4.6).

## Maven Dependency

```xml
<dependency>
    <groupId>io.allurx</groupId>
    <artifactId>blur</artifactId>
    <version>${latest version}</version>
</dependency>
```

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
Finally, to obfuscate all the sensitive information within the object and return a new instance, use the following:

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

# How It Works

Blur uses [annotation-parser](https://github.com/allurx/annotation-parser) to parse custom blurring annotations across any data structure. 
For more details, you can refer to the project documentation.

# Extension

If your application is built on Spring Boot and you prefer not to manually call blurring methods in your code, 
the [blur-spring-boot](https://github.com/allurx/blur-spring-boot) library can be very helpful. You can find more information in the project documentation.

# License

[Apache License 2.0](https://github.com/allurx/blur/blob/master/LICENSE.txt)
