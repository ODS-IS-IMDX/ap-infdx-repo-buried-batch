// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.entity;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * ファイルに出力を行うデータを定義したクラス．<br>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/23
 */
@Data
@AllArgsConstructor
@Builder
@JsonPropertyOrder({"isFacilityExist", "facilitySidList"})
public class ExportJsonEntity {

    /**
     * 埋設物有無．
     */
    @JsonProperty("isFacilityExist")
    private boolean isFacilityExist;
    
    /**
     * 埋設物リスト．
     */
    private List<SidAttributeEntity> facilitySidList;
    
}
