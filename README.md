# Excel 导入导出框架 (基于 EasyExcel)

## 设计理念

本框架基于 EasyExcel 深度封装，旨在解决企业级 Excel 处理中的复杂场景，核心设计理念包括：

1. **分层抽象**：通过模板类抽象通用逻辑，业务只需关注核心数据处理
2. **强类型约束**：使用泛型确保类型安全，减少运行时错误
3. **批处理优化**：内置分页机制处理大数据量场景
4. **多场景支持**：
   - 单页/多页导出
   - 不同结果类型导入（消息/文件）
   - 动态列导出
5. **可扩展样式**：通过 WriteHandler 机制支持自定义样式
6. **错误处理标准化**：统一导入错误收集和返回格式

## 核心设计

### 1. 类关系架构

```markdown
IExcelBusinessBase (接口)
├─ IExBase (导出接口)
│  └─ ExcelExportTemplate (单页导出模板)
│     └─ ExcelMultipartExportTemplate (多页导出模板)
│
└─ IImBase (导入接口)
   └─ ExcelImportTemplate (导入模板)
      └─ CommonImportTemplate (导入基础)
```

### 2. 关键组件

#### 导出核心：

- **ExcelExportTemplate**：单页导出模板

  - 泛型：`<T>` 数据模型, `<K>` 请求参数

  - 核心方法：

    ```java
    void config() // 配置导出参数
    R verifyParamData(K) // 参数校验
    long calculateDataCount() // 计算总数
    List<T> queryData(int,int) // 分页查询
    ```

- **ExcelMultipartExportTemplate**：多页导出模板

  - 扩展方法：

    ```java
    List<ExcelMultipartBaseResult> initBaseData() // 初始化多页信息
    T queryData(String,int,int) // 按sheet分页查询
    ```

#### 导入核心：

- **ExcelImportTemplate**：导入模板

  - 泛型：`<T>` 数据模型, `<K>` 请求参数, `<I>` 结果类型

  - 核心方法：

    ```java
    void read(List<T>) // 处理读取数据
    int getHeadRowNumber() // 获取表头行数
    void addErrorData(T, String, String) // 添加错误数据
    ```

#### 结果类型：

- **ImportResultDTO**：结果基类
  - `MsgImportResultDTO`：消息型结果（错误详情）
  - `FormImportResultDTO`：文件型结果（错误文件）

#### 样式控制：

- **WriteHandlerStaticFactory**：样式处理器工厂
- **ExcelStyleParam**：样式参数基类
  - `ExcelHeadStyle`：表头样式
  - `ExcelCellStyle`：单元格样式
  - `ExcelMergeParam`：合并单元格参数

## ExcelHandlePerformer 核心设计

`ExcelHandlePerformer` 是整个框架的任务调度入口，负责处理导入导出任务的同步/异步执行、状态管理和进度追踪。其核心功能包括：

```java
public class ExcelHandlePerformer {
    
    // 同步执行入口
    public <T extends IExcelBusinessBase> Object startSync(T task) {
        // 处理同步任务执行
    }
    
    // 异步执行入口（核心方法）
    public <T extends IExcelBusinessBase> String startAsy(
        T task, 
        ExcelProgressAsyLineStructure progressStructure
    ) {
        // 1. 生成唯一任务ID
        // 2. 初始化任务状态
        // 3. 提交到线程池执行
        // 4. 返回任务ID
    }
    
    // 任务状态查询
    public ExcelProgressAsyLineStructure getTaskStatus(String taskId) {
        // 从进度存储器获取状态
    }
}
```

## ExcelProgressAsyLineStructure 进度结构



```java
public class ExcelProgressAsyLineStructure {
    private String taskId;      // 任务唯一ID
    private String status;      // 任务状态: RUNNING/COMPLETED/FAILED
    private int progress;       // 进度百分比(0-100)
    private String result;      // 任务结果(文件路径/错误信息)
    private long startTime;     // 任务开始时间
    private long updateTime;    // 最后更新时间
}
```



## 使用手册

### 启动导出任务入口

```java
@RestController
@RequestMapping("/excel")
public class ExcelExportController {

    @Autowired
    private ExcelHandlePerformer excelPerformer;
    
    @PostMapping("/export")
    public R<void> startAsyncExport(@RequestBody ExcelTranslationBaseParam param) {
        // 3. 启动任务
        ExcelTranslationResult result = excelPerformer.start(param);
        // 可解析 result
        // 非异步模式仅使用response流导出，会自动将文件流响应给客户端
    }
}
```

```java
@Configuration
public class ExcelHandlePerformerConfiguration {

    @Bean
    pubilc ExcelHandlePerformer providerExcelHandlePerformer(){
        ExcelHandlePerformer perfomer = ExcelHandlePerformer.build();
        // 如果 IExcelStorage 存在于spring容器中，会自动注入，也可以手动指定
        perfomer.setStorage( new IExcelStorage(){...});
    }
    
}
```

### 单页导出示例

```java
@ExcelBusinessCode(value=10001L,businessName="案例导出")  // 任务唯一编码
public class UserExport extends ExcelExportTemplate<UserVO, UserQuery> {
    
    @Override
    public void config() {
        // 设置表头
        setHead(new String[]{"ID", "姓名", "部门"});
        
        // 设置样式
        setHeadStyle(() -> new CustomHeadStyle());
        setCellStyle(() -> new CustomCellStyle());
        
        // 开启批量处理（>10000条时必需）
        openBatch(2000);
    }
    
    @Override
    public R<Object> verifyParamData(UserQuery query) {
        if(query.getStartDate() == null) 
            return R.fail("起始日期必填");
        return R.success();
    }
    
    @Override
    public long calculateDataCount() {
        return userService.countUsers(getParamData());
    }
    
    @Override
    public List<UserVO> queryData(int page, int size) {
        return userService.listUsers(getParamData(), page, size);
    }
}
```

### 多页导出示例

```java
@ExcelBusinessCode(value=10002L,businessName="案例导出2")  // 任务唯一编码
public class DeptReportExport 
    extends ExcelMultipartExportTemplate<DeptDataResult, DeptReportQuery> {

    @Override
    public List<ExcelMultipartBaseResult> initBaseData() {
        List<Dept> depts = deptService.listAll();
        return depts.stream()
            .map(dept -> new ExcelMultipartBaseResult(
                dept.getName(), 
                userService.countByDept(dept.getId())
            ))
            .collect(Collectors.toList());
    }
    
    @Override
    public DeptDataResult queryData(String sheetName, int page, int size) {
        Long deptId = deptService.getIdByName(sheetName);
        List<UserVO> users = userService.listByDept(deptId, page, size);
        return new DeptDataResult(sheetName, users);
    }
    
    //... 其他方法实现类似单页导出
}
```

### 导入处理示例

```java
@ExcelBusinessCode(value=10003L,businessName="案例导入")  // 任务唯一编码
public class UserImport extends ExcelImportTemplate<UserImportDTO, ImportParam, MsgImportResultDTO> {

    @Override
    protected int getHeadRowNumber() {
        return 1; // 表头占1行
    }

    @Override
    public void read(List<UserImportDTO> batch) {
        batch.forEach(user -> {
            if(StringUtils.isEmpty(user.getMobile())) {
                addErrorData(user, "mobile", "手机号不能为空");
            } else if(!Pattern.matches(REGEX_MOBILE, user.getMobile())) {
                addErrorData(user, "mobile", "手机号格式错误");
            } else {
                userService.importUser(user);
            }
        });
    }
    
    @Override
    public R<Object> verifyParamData(ImportParam param) {
        // 参数校验逻辑...
    }
}
```

### 样式自定义示例

```java
public class CustomHeadStyle extends ExcelHeadStyle {
    public CustomHeadStyle() {
        // 设置背景色
        setBackgroundColor(IndexedColors.LIGHT_BLUE);
        // 设置字体
        ExcelFont font = new ExcelFont();
        font.setBold(true);
        font.setFontHeightInPoints((short)12);
        setFont(font);
    }
}

// 在导出模板中使用
public void config() {
    setHeadStyle(() -> new CustomHeadStyle());
}
```

### 启动异步导出任务入口

```java
@RestController
@RequestMapping("/excel")
public class ExcelExportController {

    @Autowired
    private ExcelHandlePerformer excelPerformer;
    
    @Autowired
    private RedisProgressManager progressManager;

    @PostMapping("/async-export")
    public R<String> startAsyncExport(@RequestBody ExcelTranslationBaseParam param) {
        // 2. 初始化进度结构(ExcelProgressAsyLineStructure 为 )
        ExcelProgressAsyLineStructure progressStructure = 
            new ExcelProgressAsyLineStructure(){
            @Override
            void whenStart(ExcelProgressBean excelProgressBean){
                progressManager.initTask(...);
            }
            @Override
            void whenNextProgress(ExcelProgressNextBean excelProgressNextBean){
                progressManager.updateProgress(...);
            }
            @Override
            void whenEnd(ExcelProgressEndBean excelProgressEndBean){
                progressManager.completeTask(...);
            }
            @Override
            void whenException(ExcelProgressExceptionBean excelProgressExceptionBean){
                progressManager.error(...);
            }
        };
        
        // 3. 启动异步任务
        String taskId = excelPerformer.startAsy(param, progressStructure).getUuid();
        
        // 4. 返回任务ID给客户端
        return R.success(taskId);
    }
}
```

### Redis 进度管理器实现

```java
public class RedisProgressManager {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void initTask(String taskId, ExcelTaskType taskType) {
        // 初始化任务信息
        redisTemplate.opsForHash().put("excel:task:" + taskId, 
            "info", new ExcelProgressAsyLineStructure(taskId));
        
        // 设置初始进度
        redisTemplate.opsForValue().set("excel:progress:" + taskId, "0");
        
        // 添加到任务集合
        redisTemplate.opsForSet().add("excel:task:expiry", taskId);
    }
    
    public void updateProgress(String taskId, int progress) {
        // 更新进度值
        redisTemplate.opsForValue().set("excel:progress:" + taskId, 
            String.valueOf(progress));
        
        // 更新最后修改时间
        redisTemplate.opsForHash().put("excel:task:" + taskId, 
            "updateTime", System.currentTimeMillis());
    }
    
    public void completeTask(String taskId, Object result) {
        // 标记任务完成
        redisTemplate.opsForHash().put("excel:task:" + taskId, 
            "status", "COMPLETED");
        redisTemplate.opsForHash().put("excel:task:" + taskId, 
            "result", result);
        updateProgress(taskId, 100);
    }
    
    public ExcelProgressAsyLineStructure getProgress(String taskId) {
        // 从Redis组合进度信息
        ExcelProgressAsyLineStructure progress = (ExcelProgressAsyLineStructure) 
            redisTemplate.opsForHash().get("excel:task:" + taskId, "info");
        
        progress.setProgress(Integer.parseInt(
            redisTemplate.opsForValue().get("excel:progress:" + taskId)));
        
        return progress;
    }
}
```

### 进度查询接口

```java
@Autowired
private RedisProgressManager progressManager;

@GetMapping("/progress/{taskId}")
public R<ExcelProgressAsyLineStructure> getProgress(
    @PathVariable String taskId) {
    ExcelProgressBean progressBean = progressManager.getProgress(tasKId);
    Result resuult;
    switch(progressBean.status){
            // 根据状态判断处理....
            resuult = ...;
    }
    return R.success(resuult);
}
```



## 高级特性

### 1. 动态列导出

当需要完全自定义表头时：

```java
public class DynamicExport extends ExcelExportTemplate<List<String>, DynamicQuery> {
    
    @Override
    public void config() {
        // 设置二维表头
        List<List<String>> head = Arrays.asList(
            Arrays.asList("基本信息", "姓名"),
            Arrays.asList("基本信息", "年龄"),
            Arrays.asList("联系信息", "手机号")
        );
        setHead(head);
    }
    
    @Override
    public List<List<String>> queryData(int page, int size) {
        // 返回二维数据
        return dynamicService.getData(page, size);
    }
}
```

### 2. 合并单元格处理

```java
public void config() {
    // 合并A1到B2区域
    addCellMerge(0, 1, 0, 1);
}
```

### 3. 多结果类型导入

```java
// 返回错误文件
public class FileResultImport 
    extends ExcelImportTemplate<UserImportDTO, ImportParam, FormImportResultDTO> {
    // 框架会自动处理错误文件生成
}

// 控制器返回
public R importUsers(ImportParam param) {
    FileResultImport importer = new FileResultImport();
    importer.setParamData(param);
    String filePath = importer.process();
    return R.success(filePath);
}
```

## 使用建议

1. **大数据量处理**：
   - 导出超过10,000行必须启用`openBatch()`
   - 合理设置批量大小（建议500-2000）
   - 避免在`queryData`中处理复杂业务逻辑
2. **内存优化**：
   - 避免在VO中使用大对象
   - 流式处理导入数据
   - 及时清理批次数据引用
3. **错误处理**：
   - 导入时详细记录字段级错误
   - 使用`addErrorData()`规范错误格式
   - 超过阈值时中断导入
4. **异步任务**：
   - 结合Redis存储任务状态
   - 实现进度查询接口
   - 设置超时中断机制
5. **样式优化**：
   - 预定义样式工厂类
   - 避免每个单元格单独设置样式
   - 使用缓存复用样式对象