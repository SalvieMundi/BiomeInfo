package bl4ckscor3.mod.biomeinfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class BiomeInfo implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			if(!MinecraftClient.getInstance().options.debugEnabled)
			{
				MinecraftClient mc = MinecraftClient.getInstance();

				if(mc.world != null)
				{
					BlockPos pos = mc.getCameraEntity().getBlockPos();

					if(mc.world.isInBuildLimit(pos))
					{
						Biome biome = mc.world.getBiome(pos);

						if(biome != null)
						{
							TranslatableText biomeName = new TranslatableText(Util.createTranslationKey("biome", mc.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome)));
							String biomeText = biomeName.getString();
							if (biomeText.contains(".")) biomeText = biomeText.substring(biomeText.lastIndexOf(".")+1);
							if (biomeText.contains("_")) {
								biomeText = biomeText.replace("_", " ").trim();
								boolean capitalize = true;
								for (int i=0; i<biomeText.length()-1; i++) {
									if (capitalize) {
										biomeText = biomeText.substring(0, i) + biomeText.substring(i, i+1).toUpperCase() + biomeText.substring(i+1);
										capitalize = false;
									}
									if (biomeText.substring(i, i+1).equals(" ")) capitalize = true;
								}
							}
							matrixStack.push();
							matrixStack.scale(1,1,1);
							mc.textRenderer.drawWithShadow(matrixStack, biomeText, 3, 3, 0xFFFFFFFF);
							matrixStack.pop();
						}
					}
				}
			}
		});
	}
}
