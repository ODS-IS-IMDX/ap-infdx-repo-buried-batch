// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.mapper;

import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * 埋設物情報取得バッチにおける設備属性情報のシリアライズを実装するクラス．<br>
 * <p>
 * 本クラスは、フィールドに付与される@JsonSerializeの引数として与えられ、フィールドがnullであった場合のみカスタムシリアライズを行う想定．
 * <pre>{@code @JsonSerialize(nullsUsing = PrivateAttributeSerializer.class) }</pre>
 * </p>
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/11/8
 */
public class PrivateAttributeSerializer extends JsonSerializer<Map<String, String>> {
        
    /**
     * 設備属性情報のカスタムシリアライザを実装する．
     * <p>
     * 設備属性情報がnullの場合は、空文字を返却する．
     * </p>
     * 
     * @param facilityAttribute 設備属性情報
     * @param gen {@link JsonGenerator}
     * @param serializers {@link SerializerProvider}
     */
    @Override
    public void serialize(Map<String, String> facilityAttribute,
            JsonGenerator gen,
            SerializerProvider serializers) throws IOException {
        
        if (facilityAttribute != null) {
            
            throw new RuntimeException();
            
        }
                
        gen.writeString("");
        
    }
        
}
