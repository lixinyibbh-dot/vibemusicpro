<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'
import { getRecentSongs } from '@/api/system'
import type { Song } from '@/api/interface'
import { UserStore } from '@/stores/modules/user'
import { useRoute } from 'vue-router'

const route = useRoute()
const userStore = UserStore()

const songs = ref<Song[]>([])
const loading = ref(false)

const loadRecentSongs = async () => {
  if (!userStore.isLoggedIn) {
    songs.value = []
    return
  }

  loading.value = true
  try {
    const res = await getRecentSongs()
    songs.value =
      res.code === 0 && Array.isArray(res.data) ? (res.data as Song[]) : []
  } catch (error) {
    console.error('获取最近播放失败:', error)
    songs.value = []
  } finally {
    loading.value = false
  }
}

watch(
  () => route.path,
  (path) => {
    if (path === '/recent') {
      loadRecentSongs()
    }
  }
)

watch(
  () => userStore.isLoggedIn,
  (isLoggedIn) => {
    if (isLoggedIn) {
      loadRecentSongs()
    } else {
      songs.value = []
    }
  }
)

onMounted(loadRecentSongs)
</script>

<template>
  <div class="flex flex-col h-full flex-1 overflow-hidden">
    <div
      class="flex items-end justify-between gap-4 px-6 pt-6 pb-4 border-b border-border"
    >
      <div>
        <div
          class="flex items-center gap-2 text-xs font-semibold tracking-[0.22em] text-muted-foreground"
        >
          <icon-ri:history-line class="text-base" />
          LISTENING HISTORY
        </div>
        <h1 class="mt-2 text-3xl font-bold">最近播放</h1>
        <p class="mt-1 text-sm text-muted-foreground">
          最近听过的歌曲会自动出现在这里
        </p>
      </div>
      <el-tag effect="plain" round>{{ songs.length }} 首</el-tag>
    </div>

    <div v-loading="loading" class="flex-1 min-h-0 overflow-hidden">
      <Table v-if="songs.length" :data="songs" class="h-full" />
      <div
        v-else-if="!loading"
        class="flex h-full flex-col items-center justify-center gap-3"
      >
        <div
          class="flex h-14 w-14 items-center justify-center rounded-full bg-[hsl(var(--hover-menu-bg))]"
        >
          <icon-ri:history-line class="text-2xl text-muted-foreground" />
        </div>
        <el-empty description="还没有播放记录" :image-size="80" />
      </div>
    </div>
  </div>
</template>
