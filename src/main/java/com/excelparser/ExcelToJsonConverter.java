package com.excelparser;

import com.excelparser.model.Cell;
import com.excelparser.model.MapData;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Excel转JSON转换器主类
 */
public class ExcelToJsonConverter {
    
    public static void main(String[] args) {
        // 默认参数
        String excelPath = "src/main/resources/地图1001.xlsx";
        int mapId = 1001;
        int width = 20;
        int height = 20;
        
        // 从命令行参数读取配置
        if (args.length >= 1) {
            excelPath = args[0];
        }
        if (args.length >= 2) {
            mapId = Integer.parseInt(args[1]);
        }
        if (args.length >= 3) {
            width = Integer.parseInt(args[2]);
        }
        if (args.length >= 4) {
            height = Integer.parseInt(args[3]);
        }
        
        // 根据Excel文件名自动生成JSON文件名
        String outputPath = generateJsonPath(excelPath);
        
        try {
            System.out.println("========================================");
            System.out.println("Excel转JSON转换器");
            System.out.println("========================================");
            System.out.println("Excel文件路径: " + excelPath);
            System.out.println("地图参数 - mapId: " + mapId + ", width: " + width + ", height: " + height);
            System.out.println("输出JSON路径: " + outputPath);
            System.out.println("========================================");
            
            // 检查Excel文件是否存在
            File excelFile = new File(excelPath);
            if (!excelFile.exists()) {
                System.err.println("错误: Excel文件不存在: " + excelPath);
                System.err.println("请确保文件路径正确，或将Excel文件放在: " + excelPath);
                return;
            }
            
            // 创建解析器
            ExcelParser parser = new ExcelParser();
            
            // 解析Excel文件
            System.out.println("正在解析Excel文件...");
            List<Cell> cells = parser.parseExcel(excelPath, mapId, width, height);
            
            System.out.println("解析完成，共读取 " + cells.size() + " 个单元格");
            
            // 创建MapData对象
            MapData mapData = new MapData(mapId, width, height);
            mapData.setCells(cells);
            
            // 转换为JSON
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 格式化输出
            
            // 确保输出目录存在
            File outputFile = new File(outputPath);
            outputFile.getParentFile().mkdirs();
            
            // 写入JSON文件
            System.out.println("正在生成JSON文件...");
            objectMapper.writeValue(outputFile, mapData);
            
            System.out.println("========================================");
            System.out.println("转换完成！");
            System.out.println("JSON文件已生成: " + outputFile.getAbsolutePath());
            System.out.println("========================================");
            
        } catch (IOException e) {
            System.err.println("处理文件时发生错误: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("发生未知错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 根据Excel文件路径生成对应的JSON文件路径
     * 例如: src/main/resources/map.xlsx -> output/map.json
     *      data/test.xlsx -> output/test.json
     */
    private static String generateJsonPath(String excelPath) {
        File excelFile = new File(excelPath);
        String fileName = excelFile.getName();
        
        // 获取文件名（不含扩展名）
        String baseName = fileName;
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex > 0) {
            baseName = fileName.substring(0, lastDotIndex);
        }
        
        // 生成JSON文件路径（放在output目录下）
        return "output/" + baseName + ".json";
    }
}
