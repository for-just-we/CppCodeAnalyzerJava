
一个基于[Joern](https://github.com/octopus-platform/joern)开发的工具，目前只包括基础的将代码解析为CFG，CDG和DDG的功能，目前CodeParser只定义了一些基础的接口，不包括Main函数，各个接口的用法可参考test中的代码。


各个目录功能

- graphutils: 定义图的基类（控制流图，数据流图，控制依赖图）

- misc：相当于commonutils

- ast: 定义用到的AST结点类型

- parsing: 通过遍历AntlrAST生成自定义的AST

- cfg: 包含控制流图的定义以及由自定义AST向控制流图转换代码

- cdg: 包含控制依赖图的定义以及由控制流图生成控制依赖图的代码

- udg: 包含对每个CFG语句进行Use-Def分析的代码，即分析单个语句Def什么symbol以及生成了什么Symbol

- ddg: 包含数据依赖图的计算，DDG的计算依赖于udg模块的结果


与Joern相比，CodeParser做出了以下改变：

- 采用的文法来自Antlr官方的[grammars-v4](https://github.com/antlr/grammars-v4)库，Joern采用的文法有时候会出现语句解析错误，比如 `wchar_t data[50] = L'A';` 中， `wchar_t, data` 等每个token都会被分配到一个CFG结点中，我们采用的文法不会出现这个问题，因此，我们的parsing模块和Joern的不同

- 计算CFG时增加了 `for range` 和 `try-catch`，对于 `for-range`，CodeParser新建一个空的Condition，for range init 会和语句块的代码一起作为循环主体。对于try-catch，CodeParser直接保留try中的语句并丢弃catch中的语句。解析try-catch的控制流优点困难，也找不到资料

- udg模块中和Joern的有些不同，在计算定义的symbol时，Joern通过Tainted变量考虑到了指针Def问题，比如 `func(a)` 中如果 `func` 第一个参数被设置为 `Tainted`，那么该语句定义symbol `* a`，但是Joern没考虑到指针的Use问题，具体可参考 `udg/useDefAnalysis/CalleeInfos.java`


CodeParser的工作流程与Joern相同，关于Joern的工作流程可参考[程序分析-Joern工具工作流程分析](https://blog.csdn.net/qq_44370676/article/details/125089161)


分析中的难点：

- 分析控制流时 `try-catch` 和 `for range语句` 时，目前没有找到可靠的资料

- 计算use-def时，对于指针变量处理的难度，比如 `*(p+i+1) = a[i][j];` 使用了 `symbol`包括 `p, i, j, a` 和 `* a`，定义的symbol包括 `* p`，这种结果不是很准确，但是很难计算出使用和定义的指针位置，只能确定是以 `* a` 和 `* p` 开头的地址区域，这种结果会带来以下问题

```cpp
s1: memset(source, 100, 'A');
s2: source[99] = '\0';
s3: memcpy(data, source, 100);
```

在上述代码片段：

- 计算结果中s1和s2均定义了`* source`，后者kill了前者定义的 `* symbol`，因此数据依赖图只存在s2->s3的边（imprecise的结果）

- 实际上s1定义了 `* source`，s2定义了 `* (source + 99)`，数据依赖图中应该存在s1->s3, s2->s3（precise的结果）


Python版本为[CppCodeAnalyzer](https://github.com/for-just-we/CppCodeAnalyzer)

使用的时候计算数据依赖时会用到calleeInfos.json，使用如下代码加载：

```java
String calleeInfoFile = "src/main/resources/calleeInfos.json";
File file = new File(calleeInfoFile);
String file1 = FileUtils.readFileToString(file);//前面两行是读取文件JSONObject jsonobject = JSON.parseObject(file1);
CalleeInfos calleeInfos = new CalleeInfos();

JSONObject defInfos = jsonobject.getJSONObject("ArgDefs");
for (Map.Entry<String, Object> entry: defInfos.entrySet()){
    String funcName = entry.getKey();
    JSONArray idxs = (JSONArray) entry.getValue();
    idxs.forEach(i -> calleeInfos.addArgDef(funcName, (Integer) i));
}

JSONObject useInfos = jsonobject.getJSONObject("ArgUses");
for (Map.Entry<String, Object> entry: useInfos.entrySet()){
    String funcName = entry.getKey();
    JSONArray idxs = (JSONArray) entry.getValue();
    idxs.forEach(i -> calleeInfos.addArgUse(funcName, (Integer) i));
}

JSONObject defStart = jsonobject.getJSONObject("ArgDefStartIds");
for (Map.Entry<String, Object> entry: defStart.entrySet()){
    String funcName = entry.getKey();
    Integer idx = (Integer) entry.getValue();
    calleeInfos.addArgDefStartIds(funcName, idx);
}

astAnalyzer.setCalleeInfos(calleeInfos);
converter.setAstAnalyzer(astAnalyzer);

CFGAndUDGToDefUseCFG defUseConverter = new CFGAndUDGToDefUseCFG();
DDGCreator ddgCreator = new DDGCreator();
CDGCreator cdgCreator = new CDGCreator();
CalleeInfos calleeInfos = new CalleeInfos();
```

`astAnalyzer.setCalleeInfos(calleeInfos);` 和 `converter.setAstAnalyzer(astAnalyzer);` 非常重要，不然加载不了calleeInfo
