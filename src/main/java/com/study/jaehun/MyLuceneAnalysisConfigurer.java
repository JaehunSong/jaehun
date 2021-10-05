package com.study.jaehun;

import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilterFactory;
import org.apache.lucene.analysis.snowball.SnowballPorterFilterFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurationContext;
import org.hibernate.search.backend.lucene.analysis.LuceneAnalysisConfigurer;

public class MyLuceneAnalysisConfigurer implements LuceneAnalysisConfigurer {
    @Override
    public void configure(LuceneAnalysisConfigurationContext context) {
        // 영어 텍스트를 분석하기 위한 "english"라는 사용자 정의 분석기 정의
        // .tokenizer : 토크나이저 설정, 단어별로 분할, StandardTokenizerFactory : 표준 토크나이저
        // .tokenFilter : 토큰 필터 설정, 주어진 순서대로 작동
        // LowerCase : 소문자로 치환, SnowballPorter : 형태소 분석을 적용
        // ASCIIFoldingFilter :  분음 부호("é", "à", … )가 있는 문자를
        //                       ASCII에 해당하는 문자("e", "a", … )로 치환
        context.analyzer( "english" ).custom()
                .tokenizer( StandardTokenizerFactory.class )
                .tokenFilter( LowerCaseFilterFactory.class )
                .tokenFilter( SnowballPorterFilterFactory.class )
                .param( "language", "English" )
                .tokenFilter( ASCIIFoldingFilterFactory.class );

        context.analyzer( "name" ).custom()
                .tokenizer( StandardTokenizerFactory.class )
                .tokenFilter( LowerCaseFilterFactory.class )
                .tokenFilter( ASCIIFoldingFilterFactory.class );
    }
}