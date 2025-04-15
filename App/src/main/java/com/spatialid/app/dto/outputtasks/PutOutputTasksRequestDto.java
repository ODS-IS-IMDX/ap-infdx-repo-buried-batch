// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.outputtasks;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 設備データタスク出力参照APIのリクエストデータ項目を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/19
 */
@Data
@Builder
@AllArgsConstructor
public class PutOutputTasksRequestDto {
    
    /**
     * タスクID．
     */
    @JsonIgnore
    private String taskId;
    
    /**
     * タスク状況．
     */
    private String taskStatus;
            
    /**
     * ファイル格納先URL．
     */
    private String fileUrl;
    
    /**
     * タスク実行日時
     */
    private String taskStartDate;
    
    /**
     * エラー内容．
     */
    private String errorDetail;
    
}
