package com.aizistral.nochatreports.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.aizistral.nochatreports.config.NCRConfigLegacy;
import com.aizistral.nochatreports.core.ServerDataExtension;

import net.minecraft.network.protocol.game.ClientboundServerDataPacket;
import net.minecraft.network.protocol.status.ServerStatus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

@Mixin(ServerStatus.class)
public class MixinServerStatus implements ServerDataExtension {

	/**
	 * Special additional variable that allows the client to know whether server prevents chat reports
	 * upon pinging it.
	 */

	private boolean preventsChatReports;

	/**
	 * @reason Spoof the value of "enforcesSecureChat" in case conversion to system messages is enabled.
	 * There is no way for client to verify the value of the option in such case, so that's one less
	 * annoying warning.
	 * @author Aizistral
	 */

	@Inject(method = "enforcesSecureChat", at = @At("HEAD"), cancellable = true)
	public void onSecureChatCheck(CallbackInfoReturnable<Boolean> info) {
		if (NCRConfigLegacy.convertToGameMessage()) {
			info.setReturnValue(true);
		}
	}

	@Override
	public boolean preventsChatReports() {
		return this.preventsChatReports;
	}

	@Override
	public void setPreventsChatReports(boolean prevents) {
		this.preventsChatReports = prevents;
	}

}