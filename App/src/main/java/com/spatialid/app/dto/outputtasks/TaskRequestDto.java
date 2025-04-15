// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.dto.outputtasks;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * {@link OutputTasksDto}の子DTO．<br>
 * JobExecutionContextへ保存するため、{@link Serializable}を実装する．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/18
 */
@Data
public class TaskRequestDto implements Serializable {
    
    /**
     * シリアルバージョン
     */
    private static final long serialVersionUID = 1L;

    /**
     * 空間IDのリスト．
     */
    private List<String> sidList;
    
    /**
     * インフラ事業者IDのリスト．
     */
    private List<String> infraCompanyIdList;
    
    /**
     * 返却ズームレベル．
     */
    private Integer returnZoomLevel;
    
    /**
     * 更新日時．
     */
    private String updateDate;
    
}
