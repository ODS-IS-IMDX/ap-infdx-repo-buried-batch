// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.manager;

import java.util.List;

import org.springframework.core.io.Resource;

import com.spatialid.app.common.exception.NoRetryableException;
import com.spatialid.app.common.exception.RetryableException;
import com.spatialid.app.dto.ParameterDto;
import com.spatialid.app.dto.sidattribute.SidAttributeDto;

/**
 * バッチのユーティリティ処理を定義するインターフェース．
 * 
 * @author matsumoto kentaro
 * @version 1.0 2024/09/19
 */
public interface IBatchUtilityManager {
    
    /**
     * パラメータとして渡されたタスクIDのバリデーションを提供するメソッド．
     * 
     * @param parameterDto タスクIDを格納したDTO．
     * @throws NoRetryableException バリデーション違反を検出した場合
     */
    public void validateTaskId(ParameterDto parameterDto) throws NoRetryableException;
    
    /**
     * 空間IDのリストの平面化を提供するメソッド．
     * 
     * @param extrudedSids 鉛直方向の情報が存在する空間IDのリスト
     * @return 平面化された空間IDのリスト
     * @throws RetryableException 処理に失敗した場合
     */
    public List<String> flattenSids(List<String> extrudedSids) throws RetryableException;
    
    /**
     * 空間・属性情報リストを元にしたファイル作成を提供するメソッド．
     * 
     * @param sidAttributeList 空間・属性情報リスト
     * @param jsonPath jsonファイルのパス
     * @param zipPath 圧縮ファイルのパス
     * @throws RetryableException 処理に失敗した場合
     */
    public void createFile(List<SidAttributeDto> sidAttributeList, String jsonPath, String zipPath) throws RetryableException;
        
    /**
     * ファイルの取得を提供するメソッド．
     * 
     * @param path 取得対象のパス．
     * @return {@link Resource}で取得されたファイル
     * @throws RetryableException 処理に失敗した場合
     */
    public Resource getFileAsResource(String path) throws RetryableException;
    
    /**
     * ファイル提供するメソッド．
     * <p>
     * {@link IBatchUtilityManager#createFile(List, String)}で作成されたファイルを全て削除する．
     * </p>
     * 
     * @param jsonPath jsonファイルのパス
     * @param zipPath 圧縮ファイルのパス
     * @throws NoRetryableException リトライ処理を行わず、異常終了することを示す例外
     */
    public void deleteFiles(String jsonPath, String zipPath) throws NoRetryableException;

}
