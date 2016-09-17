/* MIT - License

Copyright (c) 2012 - this year, Nils Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"),
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE. */
package org.nschmidt.ldparteditor.data;

import java.util.HashMap;
import java.util.HashSet;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.nschmidt.ldparteditor.composites.Composite3D;
import org.nschmidt.ldparteditor.data.colour.GCChrome;
import org.nschmidt.ldparteditor.data.colour.GCMatteMetal;
import org.nschmidt.ldparteditor.data.colour.GCMetal;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.helpers.composite3d.ViewIdleManager;
import org.nschmidt.ldparteditor.opengl.GLMatrixStack;
import org.nschmidt.ldparteditor.opengl.GLShader;
import org.nschmidt.ldparteditor.shells.editor3d.Editor3DWindow;

/**
 * New OpenGL 3.3 high performance render function for the model (VAO accelerated)
 * @author nils
 *
 */
public class GL33ModelRenderer {

    boolean isPaused = false;
    
    HashMap<String, Integer[]> mapGLO = new HashMap<>();
    HashSet<Integer> sourceVAO = new HashSet<>();
    HashSet<Integer> targetVAO = new HashSet<>();    
    HashSet<Integer> sourceBUF = new HashSet<>();
    HashSet<Integer> targetBUF = new HashSet<>();
    HashSet<Integer> swapPool = null;
    HashSet<String> swapPool2 = null;
    HashSet<String> sourceID = new HashSet<>();
    HashSet<String> targetID = new HashSet<>();
    final Composite3D c3d;
    
    private static final GTexture CUBEMAP_TEXTURE = new GTexture(TexType.PLANAR, "cmap.png", null, 1, new Vector3f(1,0,0), new Vector3f(1,1,0), new Vector3f(1,1,1), 0, 0); //$NON-NLS-1$
    private static final GDataTEX CUBEMAP = new GDataTEX(null, "", TexMeta.NEXT, CUBEMAP_TEXTURE); //$NON-NLS-1$

    private static final GTexture CUBEMAP_MATTE_TEXTURE = new GTexture(TexType.PLANAR, "matte_metal.png", null, 2, new Vector3f(1,0,0), new Vector3f(1,1,0), new Vector3f(1,1,1), 0, 0); //$NON-NLS-1$
    private static final GDataTEX CUBEMAP_MATTE = new GDataTEX(null, "", TexMeta.NEXT, CUBEMAP_MATTE_TEXTURE); //$NON-NLS-1$

    private static final GTexture CUBEMAP_METAL_TEXTURE = new GTexture(TexType.PLANAR, "metal.png", null, 2, new Vector3f(1,0,0), new Vector3f(1,1,0), new Vector3f(1,1,1), 0, 0); //$NON-NLS-1$
    private static final GDataTEX CUBEMAP_METAL = new GDataTEX(null, "", TexMeta.NEXT, CUBEMAP_METAL_TEXTURE); //$NON-NLS-1$
    
    public GL33ModelRenderer(Composite3D c3d) {
        this.c3d = c3d;
    }

    // FIXME Renderer needs implementation!
    public void draw(GLMatrixStack stack, GLShader shaderProgram, boolean drawSolidMaterials, DatFile df) {

        GDataCSG.resetCSG(df, c3d.getManipulator().isModified());

        if (drawSolidMaterials) {
            swapPool = sourceVAO;
            sourceVAO = targetVAO;
            targetVAO = swapPool;
            
            swapPool = sourceBUF;
            sourceBUF = targetBUF;
            targetBUF = swapPool;
            
            swapPool2 = sourceID;
            sourceID = targetID;
            targetID = swapPool2;
        }
        
        GData data2draw = df.getDrawChainStart();
        int renderMode = c3d.getRenderMode();

        if (Editor3DWindow.getWindow().isAddingCondlines())
            renderMode = 6;
        switch (renderMode) {
        case -1: // Wireframe
            break;
        case 0: // No BFC
            data2draw.drawGL33(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                data2draw.drawGL33(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                
            }
            break;
        case 1: // Random Colours
            data2draw.drawGL33_RandomColours(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                data2draw.drawGL33_RandomColours(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            }
            break;
        case 2: // Front-Backface BFC
            data2draw.drawGL33_BFC(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                switch (GData.accumClip) {
                case 0:
                    data2draw.drawGL33_BFC(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                    break;
                default:
                    data2draw.drawGL33(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                    break;
                }
            }
            break;
        case 3: // Backface only BFC
            data2draw.drawGL33_BFC_backOnly(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                switch (GData.accumClip) {
                case 0:
                    data2draw.drawGL33_BFC_backOnly(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                    break;
                default:
                    data2draw.drawGL33(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                    break;
                }
            }
            break;
        case 4: // Real BFC
            data2draw.drawGL33_BFC_Colour(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                switch (GData.accumClip) {
                case 0:
                    data2draw.drawGL33_BFC_Colour(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                    break;
                default:
                    data2draw.drawGL33(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
                    break;
                }
            }
            break;
        case 5: // FIXME Real BFC with texture mapping
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            data2draw.drawGL33_BFC_Textured(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            GDataInit.resetBfcState();
            data2draw.drawGL33_BFC_Textured(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            CUBEMAP.drawGL33_BFC_Textured(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            new GData3(new Vertex(0,0,0), new Vertex(1,0,0), new Vertex(1,1,0), View.DUMMY_REFERENCE, new GColour(0, 0, 0, 0, 0, new GCChrome()), true).drawGL33_BFC_Textured(c3d.getComposite3D(), stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            CUBEMAP_MATTE.drawGL33_BFC_Textured(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            new GData3(new Vertex(0,0,0), new Vertex(1,0,0), new Vertex(1,1,0), View.DUMMY_REFERENCE, new GColour(0, 0, 0, 0, 0, new GCMatteMetal()), true).drawGL33_BFC_Textured(c3d.getComposite3D(), stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            CUBEMAP_METAL.drawGL33_BFC_Textured(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            new GData3(new Vertex(0,0,0), new Vertex(1,0,0), new Vertex(1,1,0), View.DUMMY_REFERENCE, new GColour(0, 0, 0, 0, 0, new GCMetal()), true).drawGL33_BFC_Textured(c3d.getComposite3D(), stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                data2draw.drawGL33_BFC_Textured(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            }
            // vertices.clearVertexNormalCache();
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 2);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 4);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 8);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL13.glActiveTexture(GL13.GL_TEXTURE0 + 16);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            break;
        case 6: // Special mode for "Add condlines"
            data2draw.drawGL33_WhileAddCondlines(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            while ((data2draw = data2draw.getNext()) != null && !isPaused) {
                isPaused = ViewIdleManager.pause[0].get();
                data2draw.drawGL33_WhileAddCondlines(c3d, stack, drawSolidMaterials, sourceVAO, targetVAO, sourceBUF, targetBUF, sourceID, targetID, mapGLO);
            }
            break;
        default:
            break;
        }
        
        if (!drawSolidMaterials) {
            if (isPaused) {
                targetID.addAll(sourceID);
                targetVAO.addAll(sourceVAO);
                targetBUF.addAll(sourceBUF);
            } else {
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
                GL30.glBindVertexArray(0);
                for (Integer id : sourceVAO) {
                    GL30.glDeleteVertexArrays(id);
                }
                for (Integer id : sourceBUF) {
                    GL15.glDeleteBuffers(id);
                }
                for (String id : sourceID) {
                    mapGLO.remove(id);
                }
            }
            sourceID.clear();
            sourceVAO.clear();
            sourceBUF.clear();
            isPaused = false;
        }

        GDataCSG.finishCacheCleanup(c3d.getLockableDatFileReference());

        if (drawSolidMaterials && renderMode != 5)
            df.getVertexManager().showHidden();
        
    }
}