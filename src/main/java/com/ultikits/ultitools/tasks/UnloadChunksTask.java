package com.ultikits.ultitools.tasks;

import com.ultikits.ultitools.enums.ConfigsEnum;
import com.ultikits.ultitools.ultitools.UltiTools;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.List;
import java.util.Random;

public class UnloadChunksTask extends BukkitRunnable {
    final static File file = new File(ConfigsEnum.CLEANER.toString());
    final static YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    final static int maxChunks = config.getInt("max_unused_chunks");
    final static int chunksUnloadRate = config.getInt("unload_chunks_per_minute");
    final static boolean enableUnloadChunkTask = config.getBoolean("unload_chunk_task_enable");

    @Override
    public void run() {
        if (!UltiTools.isProVersion) {
            return;
        }
        if (enableUnloadChunkTask) {
            List<Chunk> chunks = CleanerTask.getUnusedChunks();
            if (chunks.size() > maxChunks) {
                if (chunksUnloadRate > 0) {
                    for (int i = 0; i < chunksUnloadRate + 1; i++) {
                        Random random = new Random();
                        int randomCh = random.nextInt(chunks.size());
                        Chunk chunk = chunks.get(randomCh);
                        chunk.unload();
                        chunks.remove(randomCh);
                    }
                } else if (chunksUnloadRate == -1) {
                    for (Chunk each : chunks) {
                        each.unload(true);
                    }
                }
            }
        }
    }
}
