package unknowndomain.engine.client.rendering.block;

import unknowndomain.engine.client.block.ClientBlock;
import unknowndomain.engine.client.rendering.block.model.BlockModel;
import unknowndomain.engine.client.rendering.block.model.BlockModelQuad;
import unknowndomain.engine.client.rendering.texture.TextureUV;
import unknowndomain.engine.client.rendering.util.BufferBuilder;
import unknowndomain.engine.math.BlockPos;
import unknowndomain.engine.util.ChunkCache;
import unknowndomain.engine.util.Facing;
import unknowndomain.engine.util.Math2;

public class DefaultBlockRenderer implements BlockRenderer {

    @Override
    public void render(ClientBlock block, ChunkCache chunkCache, BlockPos pos, BufferBuilder buffer) {
        if (!block.isRenderable()) {
            return;
        }

        BlockModel model = block.getModel();
        buffer.posOffest(pos.getX(), pos.getY(), pos.getZ());
        BlockPos.Mutable mutablePos = new BlockPos.Mutable(pos);
        for (Facing facing : Facing.values()) {
            mutablePos.set(pos);
            if (!block.canRenderFace(chunkCache, mutablePos, facing)) {
                continue;
            }

            for (BlockModelQuad modelQuad : model.facedModelQuads.get(facing)) {
                renderModelQuad(modelQuad, pos, buffer);
            }
        }
    }

    @Override
    public void render(ClientBlock block, BufferBuilder buffer) {
        if (!block.isRenderable()) {
            return;
        }
        BlockModel model = block.getModel();
        for (Facing facing : Facing.values()) {
            for (BlockModelQuad modelQuad : model.facedModelQuads.get(facing)) {
                renderModelQuad(modelQuad, BlockPos.ZERO, buffer);
            }
        }
    }

    public void renderModelQuad(BlockModelQuad modelQuad, BlockPos pos, BufferBuilder buffer) {
        TextureUV textureUV = modelQuad.textureUV;
        var normal = Math2.calcNormalByVertices(new float[]{
                modelQuad.vertexs[0],modelQuad.vertexs[1],modelQuad.vertexs[2],
                modelQuad.vertexs[3],modelQuad.vertexs[4],modelQuad.vertexs[5],
                modelQuad.vertexs[6],modelQuad.vertexs[7],modelQuad.vertexs[8],
        });
        var normal1 = Math2.calcNormalByVertices(new float[]{
                modelQuad.vertexs[0],modelQuad.vertexs[1],modelQuad.vertexs[2],
                modelQuad.vertexs[6],modelQuad.vertexs[7],modelQuad.vertexs[8],
                modelQuad.vertexs[9],modelQuad.vertexs[10],modelQuad.vertexs[11],
        });
        buffer.pos(modelQuad.vertexs[0], modelQuad.vertexs[1], modelQuad.vertexs[2]).color(1, 1, 1).tex(textureUV.getMinU(), textureUV.getMaxV()).normal(normal).endVertex(); // 1
        buffer.pos(modelQuad.vertexs[3], modelQuad.vertexs[4], modelQuad.vertexs[5]).color(1, 1, 1).tex(textureUV.getMaxU(), textureUV.getMaxV()).normal(normal).endVertex(); // 2
        buffer.pos(modelQuad.vertexs[6], modelQuad.vertexs[7], modelQuad.vertexs[8]).color(1, 1, 1).tex(textureUV.getMaxU(), textureUV.getMinV()).normal(normal).endVertex(); // 3

        buffer.pos(modelQuad.vertexs[0], modelQuad.vertexs[1], modelQuad.vertexs[2]).color(1, 1, 1).tex(textureUV.getMinU(), textureUV.getMaxV()).normal(normal1).endVertex(); // 1
        buffer.pos(modelQuad.vertexs[6], modelQuad.vertexs[7], modelQuad.vertexs[8]).color(1, 1, 1).tex(textureUV.getMaxU(), textureUV.getMinV()).normal(normal1).endVertex(); // 3
        buffer.pos(modelQuad.vertexs[9], modelQuad.vertexs[10], modelQuad.vertexs[11]).color(1, 1, 1).tex(textureUV.getMinU(), textureUV.getMinV()).normal(normal1).endVertex(); // 4
    }

    @Override
    public void dispose() {

    }
}