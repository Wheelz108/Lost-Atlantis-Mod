package vespi.lostcity.dimension;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import vespi.lostcity.tools.SimplexNoise;

import java.util.Random;

public class TutorialChunkGenerator extends ChunkGenerator<TutorialChunkGenerator.Config> {
    public TutorialChunkGenerator(IWorld world, BiomeProvider provider) {
        super(world, provider, Config.createDefault());
    }

    @Override
    public int getSeaLevel() {
        return 100;
    }

    @Override
    public int getGroundHeight() {
        return 40;
    }

    @Override
    public void generateSurface(IChunk chunk) {
        BlockState bedrock = Blocks.BEDROCK.getDefaultState();
        BlockState water = Blocks.WATER.getDefaultState();
        BlockState sand = Blocks.SAND.getDefaultState();
        BlockState stone = Blocks.STONE.getDefaultState();
        ChunkPos chunkpos = chunk.getPos();

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        int x;
        int z;
        int height_offset;
        int flatness;

        if (chunk.getBiome(pos) == Biomes.WARM_OCEAN) {
            height_offset = 80;
            flatness = 5;
        } else if (chunk.getBiome(pos) == Biomes.DEEP_LUKEWARM_OCEAN) {
            height_offset = 40;
            flatness = 10;
        } else {
            height_offset = this.getGroundHeight();
            flatness = 10;
        }

        for (x = 0; x < 16; x++) {
            for (z = 0; z < 16; z++) {
                int realx = chunkpos.x * 16 + x;
                int realz = chunkpos.z * 16 + z;

                float noise0 = (float) SimplexNoise.noise((float)realx/100, (float)realz/100) * flatness;
                int height = height_offset + (int) noise0;

                for (int y = 0 ; y < 255 ; y++) {
                    if (y == 0){
                        chunk.setBlockState(pos.setPos(x, y, z), bedrock, false);
                    } else if (y <= height) {
                        chunk.setBlockState(pos.setPos(x, y, z), stone, false);
                    } else if (y == height + 1) {
                        chunk.setBlockState(pos.setPos(x, y, z), sand, false);
                    } else if (y <= getSeaLevel()) {
                        chunk.setBlockState(pos.setPos(x, y, z), water, false);
                    }
                }
            }
        }
    }

    float gradient(float p1, float p2, float p0, int pC) {
        return p2 + (p1 - p2 / pC) * p0;
    }

    @Override
    public void makeBase(IWorld worldIn, IChunk chunkIn) {
    }

    @Override
    public int func_222529_a(int p_222529_1_, int p_222529_2_, Heightmap.Type p_222529_3_) {
        return 0;
    }

    public static class Config extends GenerationSettings {
        public static Config createDefault() {
            Config config = new Config();
//            config.setDefaultBlock(Blocks.DIAMOND_BLOCK.getDefaultState());
//            config.setDefaultFluid(Blocks.LAVA.getDefaultState());
            return config;
        }
    }
}
