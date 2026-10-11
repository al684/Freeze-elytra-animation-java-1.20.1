package com.upright.mixin;

import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * While gliding with elytra, vanilla tilts the whole player model forward
 * (up to 90 degrees) with a pitch rotation on the matrix stack.
 * We cancel that single rotation so the body stays upright.
 *
 * The head already uses a fixed -45 degree pitch while gliding in
 * BipedEntityModel.setAngles, and because the body is no longer tilted,
 * that makes the head look up 45 degrees (between forward and straight up).
 */
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin {

	@Redirect(
		method = "setupTransforms(Lnet/minecraft/client/network/AbstractClientPlayerEntity;Lnet/minecraft/client/util/math/MatrixStack;FFF)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V",
			ordinal = 0
		)
	)
	private void elytraUpright$skipForwardTilt(MatrixStack matrices, Quaternionf rotation) {
		// Intentionally empty: skip the forward-lean rotation.
	}
}
