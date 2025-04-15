// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

/**
 * バッチの起動パラメータを保持するDTOクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/11/01
 */
@Value
@AllArgsConstructor
@Builder
@Jacksonized
public class ParameterDto {
    
    /**
     * タスクID．
     */
    @NotEmpty
    private String taskId;
    
}
