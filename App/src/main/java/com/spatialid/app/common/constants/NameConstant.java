// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.constants;

/**
 * ジョブ名・ステップ名を定義したクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.0 2024/09/17
 */
public class NameConstant {
    
    /**
     * 埋設物情報取得のジョブ名．
     */
    public static final String GET_UF_JOB_NAME = "getUndergroundFacilityJob";
    
    /**
     * 普通例外送出時のサブフロー名．
     */
    public static final String GENERIC_ERROR_FLOW_NAME = "genericErrorFlow";
    
    /**
     * 特殊例外送出時のサブフロー名．
     */
    public static final String EXCLUSIVE_ERROR_FLOW_NAME = "exclusiveErrorFlow";
    
    /**
     * 埋設物情報取得のステップ名．
     */
    public static final String GET_UF_STEP_NAME = "getUndergroundFacilityStep";
    
    /**
     * 普通例外登録のステップ名．
     */
    public static final String REGIST_GENERIC_ERROR_STEP_NAME = "registGenericErrorStep";
    
    /**
     * 特殊例外登録のステップ名．
     */
    public static final String REGIST_EXCLUSIVE_ERROR_STEP_NAME = "registExclusiveErrorStep";
    
    /**
     * ファイル削除のステップ名．
     */
    public static final String DELETE_FILES_STEP_NAME = "deleteFilesStep";
        
}
