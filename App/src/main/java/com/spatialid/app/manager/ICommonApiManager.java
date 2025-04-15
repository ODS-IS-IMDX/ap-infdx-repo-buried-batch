// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.manager;

import org.springframework.util.MultiValueMap;

import com.spatialid.app.common.exception.NoRetryableException;
import com.spatialid.app.common.exception.NoRetryableOutputTasksException;
import com.spatialid.app.common.exception.NoRetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableException;
import com.spatialid.app.common.exception.RetryableOutputTasksException;
import com.spatialid.app.common.exception.RetryableS3Exception;
import com.spatialid.app.common.exception.RetryableSidAttributeException;
import com.spatialid.app.common.exception.RetryableSidAttributeLockedException;
import com.spatialid.app.dto.outputtasks.GetOutputTasksRequestDto;
import com.spatialid.app.dto.outputtasks.GetOutputTasksResponseDto;
import com.spatialid.app.dto.outputtasks.PutOutputTasksRequestDto;
import com.spatialid.app.dto.s3.PostS3ResponseDto;
import com.spatialid.app.dto.sidattribute.PostSidAttributeRequestDto;
import com.spatialid.app.dto.sidattribute.PostSidAttributeResponseDto;

/**
 * 共通APIの呼び出しを定義するインターフェース．
 * 
 * @author matsumoto kentaro
 * @version 1.0 2024/09/19
 */
public interface ICommonApiManager {
    
    /**
     * 設備データ出力参照APIの呼び出しを提供する．
     *
     * @param requestDto {@link GetOutputTasksRequestDto} リクエストDto
     * @return {@link GetOutputTasksResponseDto} 設備データ出力参照APIのレスポンス
     * @exception RetryableOutputTasksException APIのレスポンスコードが、正常以外であった場合
     * @exception NoRetryableOutputTasksException APIのレスポンスコードが正常 かつ 件数が0件であった場合
     * @throws RetryableException
     * @throws NoRetryableException
     */
    public GetOutputTasksResponseDto callGetOutputTasks(GetOutputTasksRequestDto requestDto) throws RetryableException, NoRetryableException;
    
    /**
     * バッチ開始時の設備データ出力更新APIの呼び出しを提供する．
     * 
     * @param requestDto {@link PutOutputTasksRequestDto} リクエストDto
     * @exception RetryableOutputTasksException APIのレスポンスコードが、正常以外であった場合
     * @throws RetryableException
     */
    public void callPutOutputTasks(PutOutputTasksRequestDto requestDto) throws RetryableException;
        
    /**
     * 空間・属性情報参照APIの呼び出しを提供する．
     * 
     * @param requestDto {@link PostSidAttributeRequestDto} リクエストDto
     * @return {@link PostSidAttributeResponseDto} 空間・属性情報参照APIのレスポンス
     * @exception RetryableSidAttributeException APIのレスポンスコードが正常以外、データ整備内 かつ DB更新中でなかった場合
     * @exception RetryableSidAttributeLockedException APIのレスポンスコードが正常以外、かつ DB更新中であった場合
     * @exception NoRetryableSidAttributeException APIのレスポンスコードが正常以外、かつ データ整備範囲外であった場合
     * @throws RetryableException
     * @throws NoRetryableException
     */
    public PostSidAttributeResponseDto callSidAttribute(PostSidAttributeRequestDto requestDto) throws RetryableException, NoRetryableException;
    
    /**
     * S3格納APIの呼び出しを提供する．
     * 
     * @param requestMap {@link MultiValueMap} リクエストマップ
     * @return {@link PostS3ResponseDto} S3格納APIのレスポンス
     * @exception RetryableS3Exception APIのレスポンスコードが正常以外であった場合
     * @throws RetryableException
     */
    public PostS3ResponseDto callFileToS3(MultiValueMap<String, Object> requestMap) throws RetryableException;
    
}
