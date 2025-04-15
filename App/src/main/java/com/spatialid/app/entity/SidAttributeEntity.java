// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.entity;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spatialid.app.common.mapper.PrivateAttributeSerializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Json出力を行う空間・属性情報を定義したクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/11/15
 */
@Data
@AllArgsConstructor
@Builder
public class SidAttributeEntity {
    
    /**
     * インフラ事業者ID．
     */
    private String infraCompanyId;
    
    /**
     * データ種別．
     */
    private String dataType;
    
    /**
     * オブジェクトID．
     */
    private String objectId;
    
    /**
     * 設備種別名．
     */
    private String objectName;
    
    /**
     * 設備属性．
     */
    @JsonSerialize(nullsUsing = PrivateAttributeSerializer.class)
    private Map<String, String> facilityAttribute;
    
    /**
     * 空間IDのリスト．
     */
    private List<String> sidList;

}
