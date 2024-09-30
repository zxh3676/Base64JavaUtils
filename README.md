# Base64 编解码工具

## 简介

- 这是一个简单的命令行工具，可以将文本或文件编码为 Base64，或将 Base64 解码为文件。支持直接在命令行输入 Base64 字符串，也可以从文件读取编码内容。
- 本程序通过设置 3 KB 缓冲区来解决处理大量数据时内存溢出的问题。

## 功能

- **文本编码为 Base64**
- **文件编码为 Base64**
- **解码 Base64 字符串**
- **从文件读取 Base64 字符串并解码**
- *将结果输出到文件（可选）*
## 使用方法

### 编码

语法：
`encode -ft text_or_fileName path/to/output.txt`

- `-f`:对文件进行编码
- `-t`:对文本进行编码
- `text_or_fileName`:若之前指定了`-f`选项，则输入文件名；若指定的是`-t`选项，此处应为文本
- `path/to/output.txt`:将编码输出保存为文本文件。若不指定路径，则会保存到程序的同目录下

### 解码

语法：
`decode -f input.txt_or_base64String output.bin`

- `-f`:从文本文件读取base64字符串并解码
- `input.txt_or_base64String`:指定读取base64字符串的文本文件（可指定路径），或直接输入base64字符串。
- *注意：当直接输入base64字符串进行解码时，不要输入第二个`-f`选项*
- `output.bin`: 指定将输出结果保存为文件（可选）。根据不同情况，输出的文件可能并不是二进制文件，而是纯文本文件，请根据实际情况调整文件扩展名。

## 环境要求

- **Java 8 及以上版本**

## 安装与运行

1. 克隆代码:
 ```bash
git clone https://github.com/zxh3676/Base64JavaUtils.git

```
2. 编译并运行:
 ```bash
javac B64.java
java B64

```

## 许可证
本项目采用 MIT License。你可以自由使用、修改和分发本项目，需保留原始作者的版权声明。
