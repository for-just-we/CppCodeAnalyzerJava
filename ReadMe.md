
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

