// © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

package com.spatialid.app.common.printer;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

/**
 * ObjectMapperでJSON出力時に使用される{@link DefaultPrettyPrinter}の拡張クラス．
 * 
 * @author matsumoto kentaro
 * @version 1.1 2024/11/13
 */
public class LineFeedPrinter extends DefaultPrettyPrinter {

    private static final long serialVersionUID = 1L;
    
    public LineFeedPrinter () {
        
        this._objectFieldValueSeparatorWithSpaces = ": ";
        
        this._objectIndenter = new LineFeedIndenter();
        
    }
    
    /**
     * 自身のインスタンスを生成・返却する．
     */
    @Override
    public DefaultPrettyPrinter createInstance() {
        
        return new LineFeedPrinter();
        
    }
        
    /**
     * インデントに関する設定を行う内部クラス．
     */
    public static class LineFeedIndenter implements Indenter {
        
        /**
         * インデントと改行コードを指定する．
         * 
         * @param gen {@link JsonGenerator}
         * @param level インデントのネスト数
         */
        @Override
        public void writeIndentation(JsonGenerator gen,
                int level) throws IOException {
            
            gen.writeRaw("\n");
            
            for (int i = 0; i < level; i++) {
                
                gen.writeRaw("  ");
                
            }
            
        }
        
        /**
         * インデント動作がインラインかを示す真偽値を返却する．
         * 
         * @return boolean インラインかを示す真偽値
         */
        @Override
        public boolean isInline() {
            
            return false;
            
        }
        
    }
    
}
