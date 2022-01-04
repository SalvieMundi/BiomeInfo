package bl4ckscor3.mod.biomeinfo;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.impl.biome.modification.BuiltInRegistryKeys;
import net.minecraft.client.MinecraftClient;
import net.minecraft.tag.Tag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;

public class BiomeInfo implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		
		HudRenderCallback.EVENT.register((matrixStack, delta) -> {
			if(!client.options.debugEnabled)
			{
				if(client.world != null)
				{
					BlockPos pos = client.getCameraEntity().getBlockPos();

					if(client.world.isInBuildLimit(pos))
					{
						Biome biome = client.world.getBiome(pos);
						TranslatableText biomeName = new TranslatableText(Util.createTranslationKey("biome", client.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome)));
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

						if(biome != null && client.world.isSkyVisibleAllowingSea(pos)) {
							matrixStack.push();
							matrixStack.scale(1,1,1);
							client.textRenderer.drawWithShadow(matrixStack, biomeText, 3, 3, 0xFFFFFFFF);
							matrixStack.pop();
						} else if (pos.getY() <= 12) {
							if (biome.getCategory() != Biome.Category.THEEND && biome.getCategory() != Biome.Category.NETHER) {
								if (biome == null || BuiltinRegistries.BIOME.getKey(biome).isEmpty()) {
									matrixStack.push();
									matrixStack.scale(1,1,1);
									client.textRenderer.drawWithShadow(matrixStack, "Caves", 3, 3, 0xFFFFFFFF);
									matrixStack.pop();
								} else {
									matrixStack.push();
									matrixStack.scale(1,1,1);
									client.textRenderer.drawWithShadow(matrixStack, biomeText, 3, 3, 0xFFFFFFFF);
									matrixStack.pop();
								}
							} else {
								matrixStack.push();
								matrixStack.scale(1,1,1);
								client.textRenderer.drawWithShadow(matrixStack, biomeText, 3, 3, 0xFFFFFFFF);
								matrixStack.pop();
							}
						} else {
							matrixStack.push();
							matrixStack.scale(1,1,1);
							client.textRenderer.drawWithShadow(matrixStack, biomeText, 3, 3, 0xFFFFFFFF);
							matrixStack.pop();
						} 
					}
				}
			}
		});
	}
}
