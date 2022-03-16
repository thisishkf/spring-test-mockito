# spring-test-mockito

* Read from other language: [English](README.md), [繁體中文](README-zh-TW.md)

Unit test for spring application with mockito

## Table of Content

- [spring-test-mockito](#spring-test-mockito)
    - [Table of Content](#Table-of-Content)
    - [Project Set ups](#Project-Set-ups)
        - [Spring Dependencies](#Spring-Dependencies)
        - [Test Dependencies](#Test-Dependencies)
    - [Implementation](#Implementation)
        - [Create Mock Test Class](#Create-Mock-Test-Class)
        - [Create Mock Dependency](#Create-Mock-Dependency)
        - [Create Test Case](#Create-Test-Case)
        - [Testing Static Methods](#Testing-Static-Methods)
    - [Related Readings](#Related-Readings)

## Project Set ups

### Spring Dependencies

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Test Dependencies

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <version>3.4.4</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <scope>test</scope>
</dependency>
```

## Implementation

### Create Mock Test Class

1. we are using junit runner to run `MockitoJUnitRunner`

    ```
    @RunWith(MockitoJUnitRunner.class)
    ```

2. Target Test Class should be annotated with `@InjectMocks`
    
    ```
    public class GardenServiceTest {
        @InjectMocks
        private GardenServiceImpl service;
    }
    ```
3. Init the Mock instance before test context
    
    ```
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    ```

### Create Mock Dependency

If the Test class contains dependencies, we may also mock it to avoid `NullPointerException`. The dependencies should be annotated with `@Mock`

```
@Mock
private DoctorService doctorService;
```

### Create Test Case

As unit test in TDD, all scenarios(conditions) should be covered by test.

#### Basic Concept

1. Each Test is annotated with `@Test`

2. Each Test naming should include:
    1. target method name
    2. expected result
    3. (Optional) pre-condition

3. Each Test should test ONLY ONE behavior of code

Example:

```
@SneakyThrows
@Test
public void addBunny_shouldAddBunnyToGarden() {
    int originalCount = service.countBunny();
    Bunny bunny = new Bunny("bunny1", true);
    service.addBunny(bunny);
    assertEquals(1, service.countBunny() - originalCount);
}
```

#### Happy Case

This following test case is testing `addBunny()`.
It uses `assertEquals(object, object)` to validate `addBunny()` has added 1 bunny to garden.

```
@SneakyThrows
@Test
public void addBunny_shouldAddBunnyToGarden() {
    int originalCount = service.countBunny();
    Bunny bunny = new Bunny("bunny1", true);
    service.addBunny(bunny);
    assertEquals(1, service.countBunny() - originalCount);
}
```

This following test case is also testing `addBunny()`.
It uses `verify()` to validate `addBunny()` has call through method from dependencies.

```
@SneakyThrows
@Test
public void addBunny_shouldCheckBunnyHealthiness() {
    Bunny bunny = new Bunny("bunny1", true);
    service.addBunny(bunny);
    verify(doctorService, times(1)).check(bunny);
}
```

### Fail Case

Fail Cases are also the program behavior. Therefore, fail cases are always being covered by unit test.

We still take `addBunny()` as example. From the method signature, we can find that `BunnyAlreadyExistException` and `BunnyNotHealthyException` are potentially being thrown from the method. 
```
boolean addBunny(Bunny bunny) throws BunnyAlreadyExistException, BunnyNotHealthyException;
``` 

Therefore, we can create test case for those exceptions.

```
@SneakyThrows
@Test(expected = BunnyAlreadyExistException.class)
public void addBunny_shouldThrowException_whenBunnyIsAlreadyInGarden() {
    Bunny bunny = new Bunny("bunny1", true);
    service.addBunny(bunny);
    service.addBunny(bunny);
}
```

However, `BunnyNotHealthyException` is thrown from dependency. A unit test should have good isolation that nested thrown is meaningless to test.

`BunnyNotHealthyException` should leave for `doctorServiceTest` itself.

#### Testing Static Methods 

In order to test a static method from a non-bean class, we do not need to mock the test case.

Taking `MagicUtilTest` as an example, we can create a method with `@Test` for it.

## Related Readings
[spring-test-cucumber](https://github.com/thisishkf/spring-test-cucumber)
