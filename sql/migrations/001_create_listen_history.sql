-- 最近播放
-- 同一用户重复播放同一首歌时只更新时间，避免历史记录无限膨胀。
CREATE TABLE IF NOT EXISTS `tb_listen_history` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录 id',
  `user_id` bigint NOT NULL COMMENT '用户 id',
  `song_id` bigint NOT NULL COMMENT '歌曲 id',
  `played_at` datetime(3) NOT NULL COMMENT '最近播放时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_listen_history_user_song` (`user_id`, `song_id`) USING BTREE,
  INDEX `idx_listen_history_user_played` (`user_id`, `played_at`) USING BTREE,
  INDEX `fk_listen_history_song_id` (`song_id`) USING BTREE,
  CONSTRAINT `fk_listen_history_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_listen_history_song_id` FOREIGN KEY (`song_id`) REFERENCES `tb_song` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;
