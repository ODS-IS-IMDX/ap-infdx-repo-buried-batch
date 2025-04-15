// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.outputtasks;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 設備データタスク出力参照APIへのリクエストデータ項目を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/18
 */
@Data
@Builder
@AllArgsConstructor
public class GetOutputTasksRequestDto {
    
    /**
     * タスクID．
     */
    private String taskId;
    
    
    /**
     * 処理区分．
     */
    private String processClass;
    
}
