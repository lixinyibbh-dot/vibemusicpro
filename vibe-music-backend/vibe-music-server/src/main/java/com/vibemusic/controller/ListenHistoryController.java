package com.vibemusic.controller;

import com.vibemusic.model.vo.SongVO;
import com.vibemusic.result.Result;
import com.vibemusic.service.IListenHistoryService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户最近播放接口。
 */
@RestController
@RequestMapping("/history")
public class ListenHistoryController {

    @Resource
    private IListenHistoryService listenHistoryService;

    @PostMapping("/report")
    public Result report(@RequestParam("songId") Long songId) {
        return listenHistoryService.report(songId);
    }

    @GetMapping("/recent")
    public Result<List<SongVO>> getRecentSongs(
            @RequestParam(value = "limit", defaultValue = "20") Integer limit) {
        return listenHistoryService.getRecentSongs(limit);
    }
}
