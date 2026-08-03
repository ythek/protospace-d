package in.tech_camp.prototype_d.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

public class WebSocketConfig {
  @Configuration
  @EnableWebSocketMessageBroker // WebSocketを使ったメッセージブローカーを有効にする
  public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
      // サーバーからクライアントへメッセージを送る（配信する）際の宛先プレフィックス
      // クライアントはこのトピックを購読（Subscribe）してメッセージを待ち受ける
      config.enableSimpleBroker("/topic");

      // クライアントからサーバーへメッセージを送る際の宛先プレフィックス。URLの先頭にこれをつける
      config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
      // Renderデプロイ時に環境変数ALLOWED_ORIGINにフロントエンドのURLを設定する
      String allowedOrigin = System.getenv("ALLOWED_ORIGIN");

      if (allowedOrigin == null || allowedOrigin.isEmpty()) {
        allowedOrigin = "http://localhost:3000"; // ローカル用のデフォルト
      }

      // WebSocket接続を開始するためのエンドポイント（URL）を設定
      // Next.jsのクライアントから最初にここに接続
      registry.addEndpoint("/ws-chat")
          .setAllowedOriginPatterns(allowedOrigin) // 開発環境でのCORSエラー対策
          .withSockJS(); // WebSocketが使えない古いブラウザ向けにSockJSを有効化
    }
  }
}
