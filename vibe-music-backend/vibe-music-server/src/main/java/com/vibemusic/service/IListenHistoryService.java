package com.vibemusic.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.vibemusic.model.entity.ListenHistory;
import com.vibemusic.model.vo.SongVO;
import com.vibemusic.result.Result;

import java.util.List;

/**
 * 最近播放服务。
 */
public interface IListenHistoryService extends IService<ListenHistory> {

    Result report(Long songId);

    Result<List<SongVO>> getRecentSongs(Integer limit);
}
