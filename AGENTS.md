# Vibe Music Project Guide

## Architecture

- `vibe-music-client`: Vue 3 + TypeScript + Vite user application.
- `vibe-music-admin`: Vue 3 + TypeScript + Vite admin application based on vue-pure-admin.
- `vibe-music-backend`: Java 17 + Spring Boot Maven multi-module backend.
- `sql/vibe_music.sql`: full database baseline. Put incremental schema changes in `sql/migrations/`.

## Backend Conventions

- Follow Controller -> Service interface -> ServiceImpl -> MyBatis Mapper layering.
- Return `Result<T>` for APIs and `PageResult<T>` for paginated data.
- Read the current user from `ThreadLocalUtil.get()` after `LoginInterceptor` authentication.
- Private user endpoints must be added under an allowed role prefix in `application.yml`.
- Do not add request-dependent or user-specific `@Cacheable` methods unless the cache key includes all user context.
- Add new entities in `vibe-music-model`, and mappers/controllers/services in `vibe-music-server`.

## Frontend Conventions

- Define API calls in `src/api/system.ts` and shared response types in `src/api/interface.ts`.
- Use Pinia stores in `src/stores/modules/` for cross-page client state.
- Add pages under `src/pages/`, routes in `src/routers/index.ts`, and navigation in `layout/components/aside/data.ts`.
- Reuse existing components such as `src/components/Table.vue` before creating duplicates.
- Protect login-only navigation through the existing aside/menu logic.

## Verification

Run the relevant checks before reporting completion:

```powershell
cd vibe-music-backend
.\mvnw.cmd -pl vibe-music-server -am test
```

```powershell
cd vibe-music-client
pnpm type-check
pnpm build
```

```powershell
cd vibe-music-admin
pnpm type-check
$env:NODE_OPTIONS='--max-old-space-size=8192'; pnpm vite build
```

## Guardrails

- Keep changes scoped to the requested feature.
- Do not modify unrelated modules or perform broad refactors.
- Do not commit real credentials, API keys, local database passwords, or generated build output.
- Do not edit `sql/vibe_music.sql` for incremental features; add a migration file instead.
