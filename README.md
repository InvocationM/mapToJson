# Excel转JSON转换器

这是一个Java工具，用于将Excel文件转换为指定格式的JSON文件。

## 功能说明

- 读取Excel文件（.xlsx格式）
- 解析地图单元格数据和事件信息
- 输出标准JSON格式文件

## Excel文件格式

Excel文件应包含以下列：

- **A列 (pos)**: 单元格位置序号（从1开始）
- **B列 (event1)**: 事件1的类型
- **C列 (percent1)**: 事件1的权重
- **D列 (id1)**: 事件1的ID
- **E列 (event2)**: 事件2的类型（可选）
- **F列 (percent2)**: 事件2的权重（可选）
- **G列 (id2)**: 事件2的ID（可选）
- **H列 (event3)**: 事件3的类型（可选）
- **I列 (percent3)**: 事件3的权重（可选）
- **J列 (id3)**: 事件3的ID（可选）

**注意**: 
- 第1行是中文分类标题（可忽略）
- 第2行是字段名（可忽略）
- 第3行是数据类型（可忽略）
- 从第4行开始是实际数据

## 使用方法

### 1. 编译项目

```bash
mvn clean compile
```

### 2. 运行程序

**方式一：使用默认参数**

将Excel文件放在 `src/main/resources/map.xlsx`，然后运行：

```bash
mvn exec:java -Dexec.mainClass="com.excelparser.ExcelToJsonConverter"
```

**方式二：使用命令行参数**

```bash
mvn exec:java -Dexec.mainClass="com.excelparser.ExcelToJsonConverter" -Dexec.args="Excel文件路径 mapId width height 输出文件路径"
```

示例：
```bash
mvn exec:java -Dexec.mainClass="com.excelparser.ExcelToJsonConverter" -Dexec.args="src/main/resources/map.xlsx 1001 20 20 output/map.json"
```

### 3. 参数说明

- **Excel文件路径**: Excel文件的完整路径（必需）
- **mapId**: 地图ID（默认：1001）
- **width**: 地图宽度（默认：20）
- **height**: 地图高度（默认：20）
- **输出文件路径**: JSON输出文件路径（默认：output/map.json）

## 输出JSON格式

```json
{
  "mapId": 1001,
  "width": 20,
  "height": 20,
  "cells": [
    {
      "x": 0,
      "y": 0,
      "tileId": 0,
      "events": [
        {
          "type": 2,
          "id": 1,
          "weight": 100
        }
      ]
    }
  ]
}
```

## 坐标计算规则

Excel中的 `pos` 值会通过以下公式转换为 `x` 和 `y` 坐标：

- `x = (pos - 1) % width`
- `y = (pos - 1) / width`

例如：pos=1, width=20 → x=0, y=0；pos=21 → x=0, y=1

## 依赖

- Apache POI 5.2.3（Excel读取）
- Jackson 2.15.2（JSON生成）
- Java 8+
# mapToJson
