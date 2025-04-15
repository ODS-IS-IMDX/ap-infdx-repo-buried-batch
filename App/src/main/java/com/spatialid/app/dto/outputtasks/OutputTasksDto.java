// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.outputtasks;

import java.io.Serializable;

import lombok.Data;

/**
 * {@link GetOutputTasksResponseDto}の子DTO．<br>
 * 具体的なタスク内容を格納する．<br>
 * JobExecutionContextへ保存するため、{@link Serializable}を実装する．
 * 
 * @author matsumoto kentaro
 * @version 1.0 2024/09/18
 */
@Data
public class OutputTasksDto implements Serializable {
    
    /**
     * シリアルバージョン
     */
    private static final long serialVersionUID = 1L;

    /**
     * 利用者システムID．
     */
    private String servicerId;
    
    /**
     * リクエスト内容．
     */
    private TaskRequestDto request;
    
}
