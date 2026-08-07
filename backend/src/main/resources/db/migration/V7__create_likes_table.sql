CREATE TABLE likes (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGINT NOT NULL,
  prototype_id BIGINT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

-- 同じユーザーが複数回同じ投稿にいいねするの禁止
  CONSTRAINT unique_user_prototype UNIQUE (user_id, prototype_id),

  CONSTRAINT fk_likes_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_likes_prototype FOREIGN KEY (prototype_id) REFERENCES prototypes(id) ON DELETE CASCADE
);