package me.codexadrian.spirit.compat.forge;
import mcjty.theoneprobe.api.*;
import me.codexadrian.spirit.*;
import me.codexadrian.spirit.blocks.blockentity.PedestalBlockEntity;
import me.codexadrian.spirit.blocks.blockentity.SoulCageBlockEntity;
import me.codexadrian.spirit.blocks.blockentity.SoulPedestalBlockEntity;
import me.codexadrian.spirit.utils.SoulUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class TOPCompat implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(new IProbeInfoProvider() {
            @Override
            public ResourceLocation getID() {
                return new ResourceLocation(Spirit.MODID, "spirit_probe");
            }

            @Override
            public void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData probeHitData) {
                var soulStyle = probeInfo.defaultProgressStyle().filledColor(0xff7ac4c3).alternateFilledColor(0xff7ac4c3).suffix("spirit.the_one_probe.soul_cage.bar_suffix");
                if (blockState.is(SpiritRegistry.SOUL_CAGE.get()) && level.getBlockEntity(probeHitData.getPos()) instanceof SoulCageBlockEntity blockEntity) {
                    if(blockEntity.isEmpty()) {
                        probeInfo.horizontal().text("block.spirit.soul_cage.empty_hover_text");
                    } else if(blockEntity.type != null) {
                        probeInfo.horizontal().entity(blockEntity.type.create(level)).vertical().text(" ").horizontal().text(blockEntity.type.getDescription()).text("spirit.the_one_probe.soul_cage.tier_suffix").text(SoulUtils.getTierDisplay(blockEntity.getItem(0), level));
                    }
                }
                if (blockState.is(SpiritRegistry.SOUL_PEDESTAL.get()) && level.getBlockEntity(probeHitData.getPos()) instanceof SoulPedestalBlockEntity blockEntity) {
                    if(!blockEntity.isEmpty()) {
                        ItemStack item = blockEntity.getItem(0);
                        if(item.is(SpiritRegistry.SOUL_CRYSTAL.get())) {
                            String entityName = SoulUtils.getSoulCrystalType(item);
                            //noinspection ConstantConditions
                            var entityType = EntityType.byString(entityName);
                            if(entityType.isPresent()) {
                                probeInfo.horizontal().item(item).vertical().itemLabel(item).horizontal().text(entityType.get().getDescription()).text(" ").text(SoulUtils.getTierDisplay(blockEntity.getItem(0), level));
                            } else {
                                probeInfo.horizontal().item(item).vertical().padding(5, 5).itemLabel(item);
                            }
                            probeInfo.progress(SoulUtils.getSoulsInCrystal(item), SoulUtils.getMaxSouls(item, level), soulStyle);
                        } else if (item.is(SpiritRegistry.CRUDE_SOUL_CRYSTAL.get())) {
                            probeInfo.horizontal().item(item).vertical().padding(5, 5).itemLabel(item);
                            probeInfo.progress(SoulUtils.getSoulsInCrystal(item), SpiritConfig.getCrudeSoulCrystalCap(), soulStyle);
                        }
                    }
                }
                if (blockState.is(SpiritRegistry.PEDESTAL.get()) && level.getBlockEntity(probeHitData.getPos()) instanceof PedestalBlockEntity blockEntity) {
                    if(!blockEntity.isEmpty()) {
                        ItemStack item = blockEntity.getItem(0);
                        probeInfo.horizontal().item(item).itemLabel(item);
                    }
                }
            }
        });
        theOneProbe.registerEntityProvider(new IProbeInfoEntityProvider() {
            @Override
            public String getID() {
                return Spirit.MODID + ":corrupted_info";
            }

            @Override
            public void addProbeEntityInfo(ProbeMode probeMode, IProbeInfo probeInfo, Player player, Level level, Entity entity, IProbeHitEntityData iProbeHitEntityData) {
                Corrupted corrupted = (Corrupted) entity;
                if(corrupted.isCorrupted()) {
                    probeInfo.text("effect.spirit.corrupted");
                }
            }
        });
        return null;
    }
}
