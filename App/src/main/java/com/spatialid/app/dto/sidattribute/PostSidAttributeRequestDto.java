// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.sidattribute;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 空間・属性情報参照APIへのリクエストデータ項目を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/20
 */
@Data
@Builder
@AllArgsConstructor
public class PostSidAttributeRequestDto {
    
    /**
     * 空間IDのリスト．
     */
    private List<String> sidList;
    
    /**
     * 平面検索フラグ．
     */
    @JsonProperty("isFlatSearch")
    private boolean isFlatSearch;
    
    /**
     * 利用者システムID．
     */
    private String servicerId;
    
    /**
     * インフラ事業者IDのリスト．
     */
    private List<String> infraCompanyIdList;
    
    /**
     * 更新日時．
     */
    private String updateTime;
    
    /**
     * 返却ズームレベル．
     */
    private Integer returnZoomLevel;
    
    /**
     * 呼び出し元がバッチであるかを示すフラグ．
     */
    @JsonProperty("isBatchProcess")
    private boolean isBatchProcess;
    
}
