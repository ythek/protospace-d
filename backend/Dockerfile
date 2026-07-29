# --- 1. ビルド用のステージ ---
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app-src

# プロジェクトの全ファイルをコピー
COPY . .

# Gradleを使ってアプリケーションをビルド（テストを確実にスキップ）
RUN chmod +x gradlew && ./gradlew clean build -x test -x check --no-daemon

# --- 2. 実行用のステージ（本番環境） ---
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# 【修正】標準的なパスに戻し、ワイルドカードでJARファイルをコピー
COPY --from=builder /app-src/build/libs/*.jar app.jar

# 画像が保存されるフォルダをあらかじめ作成（エラー防止）
# RUN mkdir -p src/main/resources/static/images/uploads
RUN mkdir -p uploads

EXPOSE 8080

# アプリを起動
ENTRYPOINT ["java", "-jar", "app.jar"]