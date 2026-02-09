package com.excelparser;

import com.excelparser.model.Cell;
import com.excelparser.model.Event;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel解析器
 * 解析Excel文件并转换为MapData对象
 */
public class ExcelParser {
    
    /**
     * 解析Excel文件
     * @param filePath Excel文件路径
     * @param mapId 地图ID
     * @param width 地图宽度
     * @param height 地图高度
     * @return 解析后的单元格列表
     * @throws IOException 文件读取异常
     */
    public List<Cell> parseExcel(String filePath, int mapId, int width, int height) throws IOException {
        List<Cell> cells = new ArrayList<>();
        
        try (FileInputStream fis = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            // 创建公式计算器，用于计算公式单元格的值
            FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
            
            // 从第4行开始读取数据（第1行是中文标题，第2行是字段名，第3行是数据类型）
            int startRowIndex = 3; // Excel行索引从0开始，第4行对应索引3
            
            for (int rowIndex = startRowIndex; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                
                // 读取pos列（A列，索引0）
                org.apache.poi.ss.usermodel.Cell posCell = row.getCell(0);
                if (posCell == null || getCellValueAsInt(posCell, evaluator) == 0) {
                    continue; // 跳过空行
                }
                
                int pos = getCellValueAsInt(posCell, evaluator);
                
                // 计算x和y坐标
                int x = (pos - 1) % width;
                int y = (pos - 1) / width;
                
                // 创建Cell对象，tileId默认为0，同时保存原始的pos值
                Cell cell = new Cell(pos, x, y, 0);
                
                // 读取事件1
                // Excel列：B列(event1) -> events[].type, C列(percent1) -> events[].weight, D列(id1) -> events[].id
                Event event1 = readEvent(row, 1, 2, 3, evaluator);
                if (event1 != null) {
                    cell.addEvent(event1);
                }
                
                // 读取事件2
                // Excel列：E列(event2) -> events[].type, F列(percent2) -> events[].weight, G列(id2) -> events[].id
                Event event2 = readEvent(row, 4, 5, 6, evaluator);
                if (event2 != null) {
                    cell.addEvent(event2);
                }
                
                // 读取事件3
                // Excel列：H列(event3) -> events[].type, I列(percent3) -> events[].weight, J列(id3) -> events[].id
                Event event3 = readEvent(row, 7, 8, 9, evaluator);
                if (event3 != null) {
                    cell.addEvent(event3);
                }
                
                // 只有当至少有一个事件时才添加到列表中
                if (!cell.getEvents().isEmpty()) {
                    cells.add(cell);
                }
            }
        }
        
        return cells;
    }
    
    /**
     * 读取事件数据
     * 映射关系：
     * - Excel的event1列 -> JSON的events[].type
     * - Excel的id1列 -> JSON的events[].id
     * - Excel的percent1列 -> JSON的events[].weight
     * 
     * @param row Excel行对象
     * @param eventColIndex Excel中event列索引（如event1在B列，索引为1）
     * @param percentColIndex Excel中percent列索引（如percent1在C列，索引为2）
     * @param idColIndex Excel中id列索引（如id1在D列，索引为3）
     * @param evaluator 公式计算器，用于计算公式单元格的值
     * @return Event对象，如果数据为空则返回null
     */
    private Event readEvent(Row row, int eventColIndex, int percentColIndex, int idColIndex, FormulaEvaluator evaluator) {
        // 读取Excel单元格
        org.apache.poi.ss.usermodel.Cell eventCell = row.getCell(eventColIndex);      // event1列
        org.apache.poi.ss.usermodel.Cell percentCell = row.getCell(percentColIndex); // percent1列
        org.apache.poi.ss.usermodel.Cell idCell = row.getCell(idColIndex);           // id1列
        
        // 检查三个字段是否都为空
        if (eventCell == null && percentCell == null && idCell == null) {
            return null;
        }
        
        // 读取值：event1 -> type, id1 -> id, percent1 -> weight（会自动计算公式）
        int type = getCellValueAsInt(eventCell, evaluator);    // event1列的值 -> events[].type
        int id = getCellValueAsInt(idCell, evaluator);         // id1列的值 -> events[].id
        int weight = getCellValueAsInt(percentCell, evaluator); // percent1列的值 -> events[].weight
        
        // 如果所有值都是0，则认为该事件为空
        if (type == 0 && weight == 0 && id == 0) {
            return null;
        }
        
        // 创建Event对象：type对应event1, id对应id1, weight对应percent1
        return new Event(type, id, weight);
    }
    
    /**
     * 获取单元格的整数值（会自动计算公式）
     * @param cell Excel单元格
     * @param evaluator 公式计算器，用于计算公式单元格的值
     * @return 整数值，如果单元格为空则返回0
     */
    private int getCellValueAsInt(org.apache.poi.ss.usermodel.Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) {
            return 0;
        }
        
        // 如果单元格包含公式，先计算公式的值
        if (cell.getCellType() == CellType.FORMULA) {
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue == null) {
                return 0;
            }
            // 根据公式计算结果的数据类型获取值
            switch (cellValue.getCellType()) {
                case NUMERIC:
                    return (int) cellValue.getNumberValue();
                case STRING:
                    try {
                        return Integer.parseInt(cellValue.getStringValue().trim());
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                case BOOLEAN:
                    return cellValue.getBooleanValue() ? 1 : 0;
                default:
                    return 0;
            }
        }
        
        // 处理非公式单元格
        switch (cell.getCellType()) {
            case NUMERIC:
                return (int) cell.getNumericCellValue();
            case STRING:
                try {
                    return Integer.parseInt(cell.getStringCellValue().trim());
                } catch (NumberFormatException e) {
                    return 0;
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() ? 1 : 0;
            case BLANK:
                return 0;
            default:
                return 0;
        }
    }
}
