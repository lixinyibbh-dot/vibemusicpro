package com.vibemusic.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.vibemusic.model.entity.ListenHistory;
import com.vibemusic.model.vo.SongVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 最近播放记录 Mapper。
 */
@Mapper
public interface ListenHistoryMapper extends BaseMapper<ListenHistory> {

    /**
     * 记录播放。利用用户和歌曲的唯一索引，重复播放时仅更新时间。
     */
    @Insert("""
            INSERT INTO tb_listen_history (user_id, song_id, played_at)
            VALUES (#{userId}, #{songId}, #{playedAt})
            ON DUPLICATE KEY UPDATE played_at = #{playedAt}
            """)
    int upsert(@Param("userId") Long userId,
               @Param("songId") Long songId,
               @Param("playedAt") LocalDateTime playedAt);

    /**
     * 查询用户最近播放的歌曲，并同时返回当前用户的收藏状态。
     */
    @Select("""
            SELECT
                s.id AS songId,
                s.name AS songName,
                a.name AS artistName,
                s.album,
                s.duration,
                s.cover_url AS coverUrl,
                s.audio_url AS audioUrl,
                s.release_time AS releaseTime,
                CASE WHEN EXISTS (
                    SELECT 1
                    FROM tb_user_favorite f
                    WHERE f.user_id = #{userId}
                      AND f.type = 0
                      AND f.song_id = s.id
                ) THEN 1 ELSE 0 END AS likeStatus
            FROM tb_listen_history h
            JOIN tb_song s ON s.id = h.song_id
            LEFT JOIN tb_artist a ON a.id = s.artist_id
            WHERE h.user_id = #{userId}
            ORDER BY h.played_at DESC, h.id DESC
            LIMIT #{limit}
            """)
    List<SongVO> getRecentSongs(@Param("userId") Long userId,
                                @Param("limit") int limit);
}
