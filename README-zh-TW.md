# spring-test-mockito

* Read from other language: [English](README.md), [繁體中文](README-zh-TW.md)

利用Mockito對Spring程式進行單元測試.


## 目錄

- [spring-test-mockito](#spring-test-mockito)
    - [目錄](#目錄)
    - [項目配置](#項目配置)
        - [Spring 配件](#Spring-配件)
        - [測試配件](#測試配件)
    - [實現](#實現)
        - [生成模擬測試類](#生成模擬測試類)
        - [生成模擬依賴](#生成模擬依賴)
        - [生成測試案例](#生成測試案例)
            - [基本概念](#基本概念)
            - [成功案例](#成功案例)
            - [失敗案例](#失敗案例)
            - [測試 Static Methods](#測試-Static-Methods)
    - [相關閱讀](#相關閱讀)

## 項目配置

### Spring 配件

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

### 測試配件

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

## 實現

### 生成模擬測試類

1. 我們會利用 junit runner 來執行 `MockitoJUnitRunner`

    ```
    @RunWith(MockitoJUnitRunner.class)
    ```

2. 目標測試的類要加上`@InjectMocks`註釋
    
    ```
    public class GardenServiceTest {
        @InjectMocks
        private GardenServiceImpl service;
    }
    ```
3. 在測試任務前生成模擬實例
    
    ```
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    ```

### 生成模擬依賴

如果測試的類包含依賴, 我們可以把依賴加上`@Mock`註釋把模擬依賴, 避免`NullPointerException`.

```
@Mock
private DoctorService doctorService;
```

### 生成測試案例

在 測試驅動開發(Test-Driven-Development) 下的單元測試, 所有場景（條件）都應該被測試

#### 基本概念

1. 每個測試都註釋 `@Test`

2. 每個測試的命名都需要:
    1. 目標方法(method)
    2. 期望結果
    3. (可選的) 前設條件

3. 每個測試都只應該測試一個程式的行為

例子:

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

#### 成功案例

下面我們會用 `addBunny()`做展示.
他調用了 `assertEquals(object, object)` 去測試 `addBunny()` 成功把1隻小兔放到花園內.

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

我們繼續用 `addBunny()`做例子.
他調用了 `verify()` 去測試 `addBunny()` 調用了其他依賴1次.

```
@SneakyThrows
@Test
public void addBunny_shouldCheckBunnyHealthiness() {
    Bunny bunny = new Bunny("bunny1", true);
    service.addBunny(bunny);
    verify(doctorService, times(1)).check(bunny);
}
```

### 失敗案例

失敗案例都是程式的期望行為，所以失敗案例都需要被單元測試。

繼續用 `addBunny()`做例子. 由方法的簽名, 方法有可能拋出`BunnyAlreadyExistException` 和 `BunnyNotHealthyException`. 
```
boolean addBunny(Bunny bunny) throws BunnyAlreadyExistException, BunnyNotHealthyException;
``` 

所以我們可以針對上面的報錯生成測試。

```
@SneakyThrows
@Test(expected = BunnyAlreadyExistException.class)
public void addBunny_shouldThrowException_whenBunnyIsAlreadyInGarden() {
    Bunny bunny = new Bunny("bunny1", true);
    service.addBunny(bunny);
    service.addBunny(bunny);
}
```

但是, `BunnyNotHealthyException` 是由依賴拋出的. 一個好的單元測試應該要把每個單元分離好

所以 `BunnyNotHealthyException`應該留給 `doctorServiceTest` 自已做單元測試.

#### 測試 Static Methods 

去測試非實例的 static method, 我們不用生成模擬的實例.

用 `MagicUtilTest` 為例, 我們簡單地把測試掛上 `@Test` 就可以.

## 相關閱讀

[spring-test-cucumber](https://github.com/thisishkf/spring-test-cucumber)
