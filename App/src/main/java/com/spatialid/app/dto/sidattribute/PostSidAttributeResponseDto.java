// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.sidattribute;

import java.util.List;

import lombok.Data;

/**
 * 空間・属性情報参照APIからのレスポンスデータ項目を保持するクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/20
 */
@Data
public class PostSidAttributeResponseDto {
    
    /**
     * 空間・属性情報リスト．
     */
    private List<SidAttributeDto> sidAttributeList;
    
}
