# Blur

[English](README.md) | 简体中文

Blur 是一个基于注解的 Java 数据脱敏库，支持字符串、对象字段、集合、数组和 Map 中的敏感数据。
使用灵活，支持以下脱敏类型：

* **字符串**
* **姓名**
* **密码**
* **电子邮箱**
* **电话号码**
* **身份证号**
* **银行卡号**
* **级联脱敏**
* **基于自定义注解的脱敏**

# 使用

## JDK 版本

Blur 需要 JDK 25 或更高版本。使用旧版 JDK 的项目，可参考兼容 Java 8 的 [v2.4.6 使用指南](https://github.com/allurx/blur/tree/v2.4.6)。

## Maven 依赖

将 `LATEST_VERSION` 替换为 Maven Central 上列出的最新版本：

<pre><code>&lt;dependency&gt;
    &lt;groupId&gt;io.allurx&lt;/groupId&gt;
    &lt;artifactId&gt;blur&lt;/artifactId&gt;
    &lt;version&gt;<a href="https://central.sonatype.com/artifact/io.allurx/blur">LATEST_VERSION</a>&lt;/version&gt;
&lt;/dependency&gt;</code></pre>

## 示例

### 对象字段脱敏

下面的 `Person` 类包含敏感字段，以及需要级联处理的嵌套对象。

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

为敏感字段添加 `@Name`、`@PhoneNumber`、`@Password` 等注解；需要级联脱敏的对象字段使用 `@Cascade`。
调用以下方法，按配置的规则处理字段并返回新实例：

```java
var person = Blur.blur(new Person());
```

### 值脱敏

`String`、`Collection`、`Array` 和 `Map` 同样支持脱敏。

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

构造 `AnnotatedTypeToken` 可保留待脱敏对象的实际类型及其注解信息。

## 注意事项

* `startOffset` 和 `endOffset` 分别表示头尾保留的 Unicode 码点数。两者必须非负，且总和不能超过输入的码点数。输入包含补充平面字符时，原先按 UTF-16 计算的偏移量可能需要调整。
* 输出保持 `String.length()` 不变：`@Name` 将 `𠮷田` 转为 `𠮷*`，将 `张𠮷` 转为 `张**`。码点不等同于字素簇；不会修复输入中未配对的代理项。
* 非空 `regexp` 优先于偏移量。条件为 `false` 或正则未匹配时保留原值；无效的正则或实际用于脱敏的偏移量会抛出异常。匹配及占位符规则见[脱敏选项](src/main/java/io/allurx/blur/annotation/Strings.java)。
* 自定义条件实例会在调用之间共享，必须无状态或保证线程安全。
* 对象遍历、容器支持和实例创建遵循 [annotation-parser](https://github.com/allurx/annotation-parser) 的规则。处理继承字段需要 `@Cascade(inherited = true)`；普通类的 `final` 和 `transient` 字段不会脱敏。

# 工作原理

Blur 使用 [annotation-parser](https://github.com/allurx/annotation-parser) 解析脱敏注解，并遍历受支持的对象和容器。
详细说明见该项目文档。

# 扩展

如果项目使用 Spring Boot，希望自动应用脱敏而不在业务代码中手动调用，可参考 [blur-spring-boot](https://github.com/allurx/blur-spring-boot) 的项目文档。

# 构建

使用 JDK 25 或更高版本及 Maven 3.9.x，在仓库根目录执行：

```sh
mvn -B -ntp clean verify
```

同时生成源码包和 Javadoc，但跳过签名：

```sh
mvn -B -ntp -Prelease "-Dgpg.skip=true" clean verify
```

CI 和发布流程使用 [allurx-build](https://github.com/allurx/allurx-build)。

# 许可证

[Apache License 2.0](LICENSE.txt)
