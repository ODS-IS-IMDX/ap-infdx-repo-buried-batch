# © 2025 NTT DATA Japan Co., Ltd. & NTT InfraNet All Rights Reserved.

###メインステージ
FROM amazoncorretto:21.0.4-al2023

EXPOSE 8080

# 環境変数の設定
ENV SPRING_PROFILES_ACTIVE=prod

# yumリポジトリのリリースバージョン
ARG RELEASE_VER=2023.6.20241212

#既存ライブラリのアップデート
RUN yum -y update --releasever=${RELEASE_VER}

#取得ファイル名
ARG TARGET_FILE=infra_batch_indx_0010*.jar

#jarファイルを配置
COPY target/${TARGET_FILE} /opt/batch/infra_batch_indx_0010/infra_batch_indx_0010.jar

#バッチの出力先ディレクトリ作成・念のためjarファイルに権限を付与
RUN mkdir -p /opt/batch/infra_batch_indx_0010/export \
    && chmod +x /opt/batch/infra_batch_indx_0010/infra_batch_indx_0010.jar