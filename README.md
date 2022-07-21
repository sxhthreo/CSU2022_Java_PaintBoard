这是CSU2022应用基础实践一在线画板课程设计。

# 一、白板程序业务分析

## 1.1 相关业务分析

本程序由教师端和学生端两部分组成。教师端启动后，进入图形化界面，包含聊天框、输入框，发送信息、发送文件、提醒听课等按钮，画图板，直线、圆形、矩形、铅笔、画笔、清空等按钮，红、黄、蓝、绿、黑、白等颜色按钮，以及在线学生的显示。

当每个学生端进行连接时，会首先跳出登录界面；当用户是首次使用本系统时，可以进行注册，即点击“注册”按钮，学生端输入帐号、密码和确认密码进行注册。学生还可以选取自己的头像，当没有选择头像时，系统将会选用默认头像作为他的头像。当学生端提交注册信息时，教师端的服务器将会进行以下的判断条件：若帐号被注册，则拒绝本次注册，否则判断密码和确认密码是否相同；若相同则同意本次注册，并将帐号和密码信息存储于数据库中；若不相同则拒绝本次注册。注册完的账户当进行再次登录系统时，无需注册，直接登录即可。学生端进行登录时，首先输入帐号和密码，并点击“登录”，教师端的服务器通过数据库查询操作判断帐号是否存在、帐号和密码信息是否对应，若存在并对应，再判断用户是否重复登录，若非重复登录则同意登录，开放文件传输端口并与学生端连接，同时将帐号和ChatThread类对象的对应关系记录于HashMap中，以便聊天记录的转发等，否则任意一种情况都将拒绝登录。

学生端连接课堂后，可以发送文字消息和举手发言，并同步教师端的画图板信息，显示当前在线的学生信息。教师端可以选择自己上课需要的形状（点击相应按钮），如直线、圆形、矩形，也可以选择铅笔、画笔来进行画图；教师端相应形状颜色的绘制可以选择红色、黄色、蓝色、绿色、黑色、白色等。

当教师发现学生听课不专心时（如线下教学情况），可以点击“提醒听课”，学生端对应的学生会振动2s，并在文字显示框内显示警告信息。

## 1.2 相关业务流程图

1.1所述的相关的业务流程图如图所示：

（1）注册、登录业务：

![image-20220721194916760](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721194916760.png)

（2）系统功能业务：

 ![image-20220721194920011](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721194920011.png)

# 二、白板程序系统设计

## 2.1 系统功能定义

### 2.1.1 系统实现的功能描述

\1. 编写图形用户界面，教师为服务器端，学生为客户端，一个服务端可以接受多个客户端连接。

\2. 要求完成教师白板的建立,实现教师在白板上画图，学生端能够实时刷新。

\3. 教师可以传输文件给学生。

\4. 学生可以向教师进行文字提问，问答内容所有学生都能看到。

\5. 学生端可以向教师进行语音提问。

\6. 教师可以提醒学生听课，学生端进行振动，并显示警告信息。

### 2.1.2 消息头部标记的定义

与聊天室程序相同，我们需要为教师端与学生端的通信设计不同的消息头部标记。下面的表格描述了本项目中不同头部标记的定义。

| 消息头部 | 具体含义                             |
| -------- | ------------------------------------ |
| REG1     | 检查注册时密码和确认密码一致性       |
| YES      | 注册密码和确认密码一致               |
| NO       | 注册信息有误或登录信息有误           |
| REG2     | 检查注册时用户名是否已经被注册       |
| EXISTS   | 用户名已经存在                       |
| INSERT   | 用户名已经成功注册                   |
| LOGIN    | 用户登录                             |
| CHONG    | 用户重复登录，登录失败               |
| NO       | 用户不存在或输入信息不正确，登录失败 |
| NEW      | 新用户登入课堂                       |
| USER     | 服务器发送好友列表                   |
| LOGOUT   | 客户端离开课堂                       |
| SLOGOUT  | 教师端为其他客户端发送离开课堂信息   |
| LINE     | 教师端发送直线                       |
| YUAN     | 教师端发送圆形                       |
| JUXING   | 教师端发送矩形                       |
| QIANBI   | 教师端发送铅笔                       |
| HUABI    | 教师端发送画笔                       |
| YANSE    | 教师端发送当前绘制所用的颜色         |
| EMPTY    | 教师端发送画板清空信号               |
| RECORD   | 有学生端想要发言                     |
| OKRECORD | 教师端同意学生发言                   |
| NORECORD | 教师端拒绝学生发言                   |
| CARE     | 教师端提醒学生认真听课               |

## 2.2 类的结构设计

（1）教师端：

![image-20220721194947997](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721194947997.png)

（2）学生端：

 ![image-20220721194951812](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721194951812.png)

# 三、白板程序运行结果与测试分析

## 3.1 程序运行结果

教师端运行界面：

![image-20220721195022938](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195022938.png)

学生端登录界面：

 ![image-20220721195027565](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195027565.png)

学生端注册界面：

![image-20220721195101311](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195101311.png)

注册时选取头像并提交注册：

 ![image-20220721195106561](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195106561.png)

 ![image-20220721195110392](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195110392.png)

若注册账号已经被注册：

 ![image-20220721195113927](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195113927.png)

若登录账号或密码错误：

 ![image-20220721195118432](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195118432.png)

若该用户已经登录：

 ![image-20220721195122090](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195122090.png)

学生端课堂界面：

 ![image-20220721195126837](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195126837.png)

教师端和学生端可以通过聊天窗口进行互动：

 ![image-20220721195130500](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195130500.png)

教师端在白板上画图，学生端能够实时刷新：

 ![image-20220721195134410](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195134410.png)

教师端可以通过右侧按钮，选择不同形状和颜色：

 ![image-20220721195137920](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195137920.png)

教师端可以查看当前在线学生信息：

 ![image-20220721195141261](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195141261.png)

学生端可以通过文字进行提问：

 ![image-20220721195145454](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195145454.png)

学生端可以向老师进行语音提问，并等待教师同意请求：

 ![image-20220721195149717](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195149717.png)

若教师同意请求，则学生端可以进行语音提问：

 ![image-20220721195154588](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195154588.png)

提问的内容在教师端和学生端都能听到。如果拒绝请求，则学生端会显示教师端的拒绝信息：

 ![image-20220721195158878](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195158878.png)

教师端可以发送文件给学生端：

![image-20220721195203906](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195203906.png)

学生端收到文件后，弹出文件是否接收消息框，并在聊天框显示教师发送了一个文件：

 ![image-20220721195216220](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195216220.png)

若选择“是”，则收到文件并打开：

 ![image-20220721195220240](https://qianzeshu.oss-cn-hangzhou.aliyuncs.com/img/image-20220721195220240.png)

若选择“否”，则取消接收文件。

## 3.2 程序测试分析

- 正确性：能够正确的实现教师端和学生端的通信，可以实现教师端与学生端进行课堂交互所需的文字提问、语音提问、文件传输、画板绘制、同步画板信息等场景需求。本系统具备正确性。

- 健壮性：系统实现了对异常操作的处理。若前端输入非法或未定义的信息，不会影响程序其他功能的正常运行。本系统具备健壮性。