package com.vibemusic.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.vibemusic.constant.JwtClaimsConstant;
import com.vibemusic.constant.MessageConstant;
import com.vibemusic.mapper.ListenHistoryMapper;
import com.vibemusic.mapper.SongMapper;
import com.vibemusic.model.entity.ListenHistory;
import com.vibemusic.model.entity.Song;
import com.vibemusic.model.vo.SongVO;
import com.vibemusic.result.Result;
import com.vibemusic.service.IListenHistoryService;
import com.vibemusic.util.ThreadLocalUtil;
import com.vibemusic.util.TypeConversionUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 最近播放服务实现。
 */
@Service
public class ListenHistoryServiceImpl
        extends ServiceImpl<ListenHistoryMapper, ListenHistory>
        implements IListenHistoryService {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 50;

    @Resource
    private ListenHistoryMapper listenHistoryMapper;

    @Resource
    private SongMapper songMapper;

    @Override
    public Result report(Long songId) {
        if (songId == null || songId <= 0
                || songMapper.selectCount(new QueryWrapper<Song>().eq("id", songId)) == 0) {
            return Result.error(MessageConstant.SONG + MessageConstant.NOT_FOUND);
        }

        listenHistoryMapper.upsert(currentUserId(), songId, LocalDateTime.now());
        return Result.success(MessageConstant.UPDATE + MessageConstant.SUCCESS);
    }

    @Override
    public Result<List<SongVO>> getRecentSongs(Integer limit) {
        int resultLimit = limit == null
                ? DEFAULT_LIMIT
                : Math.max(1, Math.min(limit, MAX_LIMIT));
        return Result.success(listenHistoryMapper.getRecentSongs(currentUserId(), resultLimit));
    }

    private Long currentUserId() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        return TypeConversionUtil.toLong(claims.get(JwtClaimsConstant.USER_ID));
    }
}
