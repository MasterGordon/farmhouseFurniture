package github.cutiecat2804.farmhousefurniture.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PlateBlock extends Block {

    // (position front (z), position bottom (y), position left (x), position back (z), position top (y), position right (X))
    private static final VoxelShape SHAPE_ONE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 1.0D, 12.0D);
    private static final VoxelShape SHAPE_TWO = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 2.0D, 12.0D);
    private static final VoxelShape SHAPE_THREE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 3.0D, 12.0D);

    public static final IntegerProperty PLATES = IntegerProperty.create("plates", 1, 3);

    public PlateBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(PLATES, 1));
    }

    public boolean canSurvive(@NotNull BlockState blockState, @NotNull LevelReader levelReader, BlockPos blockPos) {
        return Block.canSupportCenter(levelReader, blockPos.below(), Direction.UP);
    }

    public boolean canBeReplaced(@NotNull BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return !blockPlaceContext.isSecondaryUseActive() && blockPlaceContext.getItemInHand().getItem() == this.asItem() && blockState.getValue(PLATES) < 3 || super.canBeReplaced(blockState, blockPlaceContext);
    }

    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {

        BlockState blockstate = blockPlaceContext.getLevel().getBlockState(blockPlaceContext.getClickedPos());

        if (blockstate.is(this)) {
            return blockstate
                    .setValue(PLATES, Math.min(3, blockstate.getValue(PLATES) + 1));
        } else {
            return this.defaultBlockState();
        }
    }

    public @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return switch (blockState.getValue(PLATES)) {
            default -> SHAPE_ONE;
            case 2 -> SHAPE_TWO;
            case 3 -> SHAPE_THREE;
        };
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockBlockStateBuilder) {
        blockBlockStateBuilder.add(PLATES);
    }
}
