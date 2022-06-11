package me.codexadrian.spirit.platform.services;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface IShaderHelper {

    void setSoulShader(ShaderInstance shader);

    <T extends Entity> RenderType getSoulShader(T entity, EntityRenderer<T> livingEntity);
}