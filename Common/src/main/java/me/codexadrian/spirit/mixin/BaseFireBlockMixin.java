package me.codexadrian.spirit.mixin;

import me.codexadrian.spirit.SpiritRegistry;
import me.codexadrian.spirit.platform.Services;
import me.codexadrian.spirit.utils.RecipeUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {

    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void onBurn(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        if (blockState.is(Blocks.SOUL_FIRE) && entity instanceof ItemEntity itemE) {
            if (itemE.getItem().getItem().equals(SpiritRegistry.BROKEN_SPAWNER_ITEM.get()) && RecipeUtils.checkMultiblock(blockPos, level, Blocks.OBSIDIAN)) {
                itemE.setInvulnerable(true);
                ItemEntity cage = new ItemEntity(itemE.level, itemE.getX(), itemE.getY(), itemE.getZ(),
                        new ItemStack(SpiritRegistry.SOUL_CAGE_ITEM.get(), 1));
                cage.setInvulnerable(true);
                itemE.level.addFreshEntity(cage);
                itemE.getItem().shrink(1);
                if (!itemE.level.isClientSide()) {
                    ServerLevel sLevel = (ServerLevel) itemE.level;
                    sLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(),
                            blockPos.getZ(), 40, 1, 2, 1, 0);
                }
                ci.cancel();
            }

            if (itemE.getItem().getItem().equals(Items.AMETHYST_SHARD) && RecipeUtils.checkMultiblock(blockPos, level, Blocks.LAPIS_BLOCK)) {
                itemE.setInvulnerable(true);
                ItemEntity crystal = new ItemEntity(itemE.level, itemE.getX(), itemE.getY(), itemE.getZ(),
                        new ItemStack(SpiritRegistry.SOUL_CRYSTAL.get(), 1));
                crystal.setInvulnerable(true);
                itemE.level.addFreshEntity(crystal);
                itemE.getItem().shrink(1);
                if (!itemE.level.isClientSide()) {
                    ServerLevel sLevel = (ServerLevel) itemE.level;
                    sLevel.sendParticles(ParticleTypes.SOUL, blockPos.getX(), blockPos.getY(),
                            blockPos.getZ(), 40, 1, 2, 1, 0);
                }
                ci.cancel();
            }

            if(Services.PLATFORM.isModLoaded("patchouli")) {
                if (itemE.getItem().getItem().equals(Items.BOOK)) {
                    //PatchouliCompat.bookRecipe(blockPos, itemE, ci);
                }
            }
        }
    }
}
