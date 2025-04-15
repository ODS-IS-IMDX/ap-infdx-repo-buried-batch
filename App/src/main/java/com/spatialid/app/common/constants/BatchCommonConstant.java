// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.constants;

/**
 * バッチに関する汎用的な値を定義したクラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/09/19
 */
public class BatchCommonConstant {
    
    /**
     * プロジェクト名．
     */
    public static final String PROJECT_NAME = "埋設物情報取得バッチ";
    
    /**
     * 日時フォーマット．
     */
    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";
    
    /**
     * 処理区分．
     */
    public static final String PROCESS_CLASS = "01";
        
    /**
     * タスク状況(処理中)．
     */
    public static final String TASK_STATUS_PROCESSING = "1";
    
    /**
     * タスク状況(完了)．
     */
    public static final String TASK_STATUS_COMPLETE = "2";
    
    /**
     * タスク状況(エラー)．
     */
    public static final String TASK_STATUS_ERROR = "9";
    
    /**
     * 出力ファイルの接尾辞．
     */
    public static final String EXPORT_FILE_SURFIX = "_location";
    
    /**
     * 出力ファイルの拡張子．
     */
    public static final String EXPORT_FILE_EXTENTION = ".json";
    
    /**
     * 出力ファイルの圧縮形式．
     */
    public static final String FILE_COMPRESSION_FORMAT = ".zip";
    
    /**
     * リトライの最大試行回数．
     */
    public static final int MAX_RETRY_ATTEMPTS = 3;
    
    /**
     * リトライの待機時間．
     */
    public static final int BACKOFF_RETRY_ATTEMPTS = 60000;
    
    /**
     * ステップの異常終了を示す終了コード．
     */
    public static final String STATUS_ABEND = "abendError";
    
    /**
     * 設備データ出力タスク管理テーブルにエラー終了を記録する終了コード．
     */
    public static final String STATUS_GENERIC_ERROR = "genericError";
    
    /**
     * 設備データ出力タスク管理テーブルにエラー内容と共にエラー終了を記録する終了コード．
     */
    public static final String STATUS_EXCLUSIVE_ERROR = "exclusiveError";
    
    /**
     * データ整備範囲外を識別する正規表現．
     */
    public static final String REGEX_INVALID_DATA_RANGE = ".*InvalidDataRange.*";
    
    /**
     * 行ロックを識別する正規表現．
     */
    public static final String REGEX_DATA_LOCKED = ".*DataLocked.*";
    
    /**
     * 汎用例外メッセージ．
     */
    public static final String MSG_ERROR = "内部処理でエラーが発生しました。";
    
    /**
     * 内部API呼び出し失敗の例外メッセージ
     */
    public static final String MSG_INTERNAL_API_ERROR = "内部API呼び出しで異常が発生しました。";
    
    /**
     * 設備データ出力タスク参照でレスポンス件数が0件であった場合の例外メッセージ．
     */
    public static final String MSG_TASKID_NOT_FOUND_ERROR = "存在しないタスクIDが指定されました。";
    
    /**
     * 設備データ更新中の例外メッセージ．
     */
    public static final String MSG_DATA_LOCKED_ERROR = "設備データ更新中";
    
    /**
     * データ整備範囲外の例外メッセージ．
     */
    public static final String MSG_DATA_RANGE_ERROR = "データ整備範囲外";
    
    /**
     * JobExecutionのエラー内容に対応するキー名．
     */
    public static final String CONTEXT_KEY_ERROR_MSG = "errorMsg";
    
    /**
     * エラー時のログメッセージに対応するキー名．
     */
    public static final String KEY_LOGGING_MSG_ERROR = "BATCH_LOG_ERROR0001";
    
    /**
     * 警告時のログメッセージに対応するキー名．
     */
    public static final String KEY_LOGGING_MSG_WARN = "BATCH_LOG_WARN0001";
    
    /**
     * バッチ起動時のログメッセージに対応するキー名．
     */
    public static final String KEY_LOGGING_MSG_BOOT = "BATCH_LOG_INFO0001";
    
    /**
     * バッチ処理開始時のログメッセージに対応するキー名．
     */
    public static final String KEY_LOGGING_MSG_START = "BATCH_LOG_INFO0002";
    
    /**
     * バッチ処理終了時のログメッセージに対応するキー名．
     */
    public static final String KEY_LOGGING_MSG_END = "BATCH_LOG_INFO0003";
    
    /**
     * バッチ処理の処理時間出力のログメッセージに対応するキー名．
     */
    public static final String KEY_LOGGING_MSG_MEASURE = "BATCH_LOG_INFO0004";
    
    
}
