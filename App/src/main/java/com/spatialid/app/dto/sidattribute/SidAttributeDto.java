// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.sidattribute;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.spatialid.app.common.mapper.PrivateAttributeDeserializer;
import com.spatialid.app.common.mapper.PrivateAttributeSerializer;

import lombok.Data;

/**
 * {@link PostSidAttributeResponseDto}の子DTO．<br>
 * 空間IDのリストと、そこに紐づいた情報を格納する．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/20
 */
@Data
public class SidAttributeDto {
    
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
    private String facilityClassificationName;
    
    /**
     * 設備属性．
     */
    @JsonSerialize(nullsUsing = PrivateAttributeSerializer.class)
    @JsonDeserialize(using = PrivateAttributeDeserializer.class)
    private Map<String, String> facilityAttribute;
    
    /**
     * 空間IDのリスト．
     */
    private List<String> sidList;
    
}
