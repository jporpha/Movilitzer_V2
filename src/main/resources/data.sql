INSERT INTO venues (name, access_code, timezone, status, created_at)
VALUES ('Mi Primer Local', 'ABC123', 'America/Santiago', 'ACTIVE', CURRENT_TIMESTAMP);

INSERT INTO playlist_source (venue_id, provider, playlist_id, display_name, active)
VALUES (1, 'SPOTIFY', '395KVAfNasZ4hXLzMMskqE', 'Playlist de prueba', true);
