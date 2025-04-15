// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.outputtasks;

import java.util.List;

import lombok.Data;

/**
 * 設備データタスク出力参照APIからのレスポンスデータ項目を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/18
 */
@Data
public class GetOutputTasksResponseDto {
    
    /**
     * タスク情報リスト．<br>
     * タスク情報をリストとして受け取るが、本バッチでは基本的に1件のタスク情報が返却される想定．
     */
    private List<OutputTasksDto> taskList;
    
}
