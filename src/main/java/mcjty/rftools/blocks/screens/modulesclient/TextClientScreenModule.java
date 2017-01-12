package mcjty.rftools.blocks.screens.modulesclient;

import mcjty.rftools.api.screens.IClientScreenModule;
import mcjty.rftools.api.screens.IModuleGuiBuilder;
import mcjty.rftools.api.screens.IModuleRenderHelper;
import mcjty.rftools.api.screens.ModuleRenderInfo;
import mcjty.rftools.api.screens.data.IModuleData;
import mcjty.rftools.blocks.screens.ScreenConfiguration;
import mcjty.rftools.proxy.ClientProxy;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TextClientScreenModule implements IClientScreenModule {
    private String line = "";
    private int color = 0xffffff;
    private boolean large = false;
    private int align = 0;  // 0 == left, 1 == center, 2 == right

    private ScreenTextCache cache = new ScreenTextCache();
    private boolean dirty = true;
    private int textx;
    private String text;
    private boolean truetype = false;

    @Override
    public TransformMode getTransformMode() {
        return large ? TransformMode.TEXTLARGE : TransformMode.TEXT;
    }

    @Override
    public int getHeight() {
        return large ? 20 : 10;
    }

    private void setup(FontRenderer fontRenderer) {
        if ((!dirty) && truetype == ScreenConfiguration.useTruetype) {
            return;
        }
        dirty = false;
        truetype = ScreenConfiguration.useTruetype;
        if (ScreenConfiguration.useTruetype) {
            int w = large ? 270 : 512;
            textx = large ? 3 : 7;
            text = ClientProxy.font.trimStringToWidth(line, w);
            switch (align) {
                case 0:
                    break;
                case 1:
                    textx += ((w-24 -textx - ClientProxy.font.getWidth(text)) / 2) / 4;
                    break;
                case 2:
                    textx += (w-24 -textx - ClientProxy.font.getWidth(text)) / 4;
                    break;
            }
        } else {
            int w = large ? 60 : 115;
            textx = large ? 4 : 7;
            text = fontRenderer.trimStringToWidth(line, w);
            switch (align) {
                case 0:
                    break;
                case 1:
                    textx += (w - fontRenderer.getStringWidth(text)) / 2;
                    break;
                case 2:
                    textx += w - fontRenderer.getStringWidth(text);
                    break;
            }
        }
    }

    @Override
    public void render(IModuleRenderHelper renderHelper, FontRenderer fontRenderer, int currenty, IModuleData screenData, ModuleRenderInfo renderInfo) {
        GlStateManager.disableLighting();
        setup(fontRenderer);
        int y = large ? (currenty / 2 + 1) : currenty;

        if (ScreenConfiguration.useTruetype) {
            float r = (color >> 16 & 255) / 255.0f;
            float g = (color >> 8 & 255) / 255.0f;
            float b = (color & 255) / 255.0f;
            ClientProxy.font.drawString(textx, (large ? 128 : 128) - y, text, 0.25f, 0.25f, large ? (-512f-44f) : (-512f-48f), r, g, b, 1.0f);
        } else {
            fontRenderer.drawString(text, textx, y, color);
        }
    }

    @Override
    public void mouseClick(World world, int x, int y, boolean clicked) {

    }

    @Override
    public void createGui(IModuleGuiBuilder guiBuilder) {
        guiBuilder
                .label("Text:").text("text", "Text to show").color("color", "Color for the text").nl()
                .toggle("large", "Large", "Large or small font")
                .choices("align", "Text alignment", "Left", "Center", "Right").nl();

    }

    public void setLine(String line) {
        this.line = line;
        dirty = true;
    }

    public void setColor(int color) {
        this.color = color;
        dirty = true;
    }

    public void setLarge(boolean large) {
        this.large = large;
        dirty = true;
    }

    @Override
    public void setupFromNBT(NBTTagCompound tagCompound, int dim, BlockPos pos) {
        if (tagCompound != null) {
            line = tagCompound.getString("text");
            if (tagCompound.hasKey("color")) {
                color = tagCompound.getInteger("color");
            } else {
                color = 0xffffff;
            }
            large = tagCompound.getBoolean("large");
            if (tagCompound.hasKey("align")) {
                String alignment = tagCompound.getString("align");
                align = "Left".equals(alignment) ? 0 : ("Right".equals(alignment) ? 2 : 1);
            } else {
                align = 0;
            }
            dirty = true;
        }
    }

    @Override
    public boolean needsServerData() {
        return false;
    }
}
