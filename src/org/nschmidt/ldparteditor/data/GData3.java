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

import java.math.BigDecimal;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import org.nschmidt.ldparteditor.composites.Composite3D;
import org.nschmidt.ldparteditor.enums.MyLanguage;
import org.nschmidt.ldparteditor.enums.Threshold;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.helpers.math.MathHelper;
import org.nschmidt.ldparteditor.helpers.math.ThreadsafeHashMap;
import org.nschmidt.ldparteditor.helpers.math.ThreadsafeTreeMap;
import org.nschmidt.ldparteditor.helpers.math.Vector3d;
import org.nschmidt.ldparteditor.opengl.GL33Helper;
import org.nschmidt.ldparteditor.opengl.GLShader;
import org.nschmidt.ldparteditor.opengl.OpenGLRenderer20;

/**
 * @author nils
 *
 */
public final class GData3 extends GData {

    public final boolean isTriangle;

    public final int colourNumber;

    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public final float x1;
    public final float y1;
    public final float z1;

    public final float x2;
    public final float y2;
    public final float z2;

    public final float x3;
    public final float y3;
    public final float z3;

    final BigDecimal X1;
    final BigDecimal Y1;
    final BigDecimal Z1;
    final BigDecimal X2;
    final BigDecimal Y2;
    final BigDecimal Z2;
    final BigDecimal X3;
    final BigDecimal Y3;
    final BigDecimal Z3;

    public final float xn;
    public final float yn;
    public final float zn;

    public GData3(final int colourNumber, float r, float g, float b, float a, float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3, GData1 parent, DatFile datFile, boolean isTriangle) {
        super(parent);
        this.isTriangle = isTriangle;
        this.colourNumber = colourNumber;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.x1 = x1 * 1000f;
        this.y1 = y1 * 1000f;
        this.z1 = z1 * 1000f;
        this.x2 = x2 * 1000f;
        this.y2 = y2 * 1000f;
        this.z2 = z2 * 1000f;
        this.x3 = x3 * 1000f;
        this.y3 = y3 * 1000f;
        this.z3 = z3 * 1000f;
        this.xn = (y3 - y1) * (z2 - z1) - (z3 - z1) * (y2 - y1);
        this.yn = (z3 - z1) * (x2 - x1) - (x3 - x1) * (z2 - z1);
        this.zn = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
        datFile.getVertexManager().add(this);
        this.X1 = new BigDecimal(x1);
        this.Y1 = new BigDecimal(y1);
        this.Z1 = new BigDecimal(z1);
        this.X2 = new BigDecimal(x2);
        this.Y2 = new BigDecimal(y2);
        this.Z2 = new BigDecimal(z2);
        this.X3 = new BigDecimal(x3);
        this.Y3 = new BigDecimal(y3);
        this.Z3 = new BigDecimal(z3);
    }

    public GData3(final int colourNumber, float r, float g, float b, float a, BigDecimal x1, BigDecimal y1, BigDecimal z1, BigDecimal x2, BigDecimal y2, BigDecimal z2, BigDecimal x3, BigDecimal y3,
            BigDecimal z3, GData1 parent, DatFile datFile, boolean isTriangle) {
        super(parent);
        this.isTriangle = isTriangle;
        this.colourNumber = colourNumber;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        final float tx1 = x1.floatValue();
        final float ty1 = y1.floatValue();
        final float tz1 = z1.floatValue();
        final float tx2 = x2.floatValue();
        final float ty2 = y2.floatValue();
        final float tz2 = z2.floatValue();
        final float tx3 = x3.floatValue();
        final float ty3 = y3.floatValue();
        final float tz3 = z3.floatValue();
        this.xn = (ty3 - ty1) * (tz2 - tz1) - (tz3 - tz1) * (ty2 - ty1);
        this.yn = (tz3 - tz1) * (tx2 - tx1) - (tx3 - tx1) * (tz2 - tz1);
        this.zn = (tx3 - tx1) * (ty2 - ty1) - (ty3 - ty1) * (tx2 - tx1);
        this.X1 = x1;
        this.Y1 = y1;
        this.Z1 = z1;
        this.X2 = x2;
        this.Y2 = y2;
        this.Z2 = z2;
        this.X3 = x3;
        this.Y3 = y3;
        this.Z3 = z3;
        this.x1 = X1.floatValue() * 1000f;
        this.y1 = Y1.floatValue() * 1000f;
        this.z1 = Z1.floatValue() * 1000f;
        this.x2 = X2.floatValue() * 1000f;
        this.y2 = Y2.floatValue() * 1000f;
        this.z2 = Z2.floatValue() * 1000f;
        this.x3 = X3.floatValue() * 1000f;
        this.y3 = Y3.floatValue() * 1000f;
        this.z3 = Z3.floatValue() * 1000f;
        datFile.getVertexManager().add(this);

    }

    public GData3(final int colourNumber, float r, float g, float b, float a, Vertex v1, Vertex v2, Vertex v3, GData1 parent, DatFile datFile, boolean isTriangle) {
        super(parent);
        this.isTriangle = isTriangle;
        this.colourNumber = colourNumber;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.x1 = v1.x;
        this.y1 = v1.y;
        this.z1 = v1.z;
        this.x2 = v2.x;
        this.y2 = v2.y;
        this.z2 = v2.z;
        this.x3 = v3.x;
        this.y3 = v3.y;
        this.z3 = v3.z;
        this.X1 = v1.X;
        this.Y1 = v1.Y;
        this.Z1 = v1.Z;
        this.X2 = v2.X;
        this.Y2 = v2.Y;
        this.Z2 = v2.Z;
        this.X3 = v3.X;
        this.Y3 = v3.Y;
        this.Z3 = v3.Z;
        this.xn = (y3 - y1) * (z2 - z1) - (z3 - z1) * (y2 - y1);
        this.yn = (z3 - z1) * (x2 - x1) - (x3 - x1) * (z2 - z1);
        this.zn = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
        datFile.getVertexManager().add(this);
    }

    /**
     * FOR CSG, TEXMAP AND Cut, Copy, Paste ONLY!
     *
     * @param v1
     * @param v2
     * @param v3
     * @param parent
     * @param c
     * @param isTriangle
     */
    public GData3(Vertex v1, Vertex v2, Vertex v3, GData1 parent, GColour c, boolean isTriangle) {
        super(parent);
        this.isTriangle = isTriangle;
        this.colourNumber = c.getColourNumber();
        this.r = c.getR();
        this.g = c.getG();
        this.b = c.getB();
        this.a = c.getA();
        this.x1 = v1.x;
        this.y1 = v1.y;
        this.z1 = v1.z;
        this.x2 = v2.x;
        this.y2 = v2.y;
        this.z2 = v2.z;
        this.x3 = v3.x;
        this.y3 = v3.y;
        this.z3 = v3.z;
        this.X1 = v1.X;
        this.Y1 = v1.Y;
        this.Z1 = v1.Z;
        this.X2 = v2.X;
        this.Y2 = v2.Y;
        this.Z2 = v2.Z;
        this.X3 = v3.X;
        this.Y3 = v3.Y;
        this.Z3 = v3.Z;
        this.xn = (y3 - y1) * (z2 - z1) - (z3 - z1) * (y2 - y1);
        this.yn = (z3 - z1) * (x2 - x1) - (x3 - x1) * (z2 - z1);
        this.zn = (x3 - x1) * (y2 - y1) - (y3 - y1) * (x2 - x1);
    }

    public GData3(final int colourNumber, float r, float g, float b, float a, BigDecimal x1, BigDecimal y1, BigDecimal z1, BigDecimal x2, BigDecimal y2, BigDecimal z2, BigDecimal x3, BigDecimal y3,
            BigDecimal z3, float x12, float y12, float z12, float x22, float y22, float z22, float x32, float y32, float z32, float xn, float yn, float zn, GData1 parent, DatFile datFile, boolean isTriangle) {
        super(parent);
        this.isTriangle = isTriangle;
        this.colourNumber = colourNumber;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.X1 = x1;
        this.Y1 = y1;
        this.Z1 = z1;
        this.X2 = x2;
        this.Y2 = y2;
        this.Z2 = z2;
        this.X3 = x3;
        this.Y3 = y3;
        this.Z3 = z3;
        this.x1 = x12;
        this.y1 = y12;
        this.z1 = z12;
        this.x2 = x22;
        this.y2 = y22;
        this.z2 = z22;
        this.x3 = x32;
        this.y3 = y32;
        this.z3 = z32;
        this.xn = xn;
        this.yn = yn;
        this.zn = zn;
        datFile.getVertexManager().add(this);

    }

    public Vector4f getNormal() {
        Vector4f vn = new Vector4f(xn, yn, zn, 0f);
        vn.normalise();
        vn.setW(1f);
        return vn;
    }

    @Override
    public void drawGL20(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        GL11.glBegin(GL11.GL_TRIANGLES);
        if (GData.globalNegativeDeterminant) {
            GL11.glColor4f(r, g, b, a);
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
        } else {
            GL11.glColor4f(r, g, b, a);
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
        }
        GL11.glEnd();
    }

    @Override
    public void drawGL20_RandomColours(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        final float r = MathHelper.randomFloat(ID, 0);
        final float g = MathHelper.randomFloat(ID, 1);
        final float b = MathHelper.randomFloat(ID, 2);
        GL11.glBegin(GL11.GL_TRIANGLES);
        if (GData.globalNegativeDeterminant) {
            GL11.glColor4f(r, g, b, a);
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
        } else {
            GL11.glColor4f(r, g, b, a);
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
        }
        GL11.glEnd();
    }

    @Override
    public void drawGL20_BFC(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        GL11.glBegin(GL11.GL_TRIANGLES);
        switch (GData.localWinding) {
        case BFC.CCW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            }
            break;
        case BFC.CW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(View.BFC_front_Colour_r[0], View.BFC_front_Colour_g[0], View.BFC_front_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            }
            break;
        case BFC.NOCERTIFY:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            }
        }
        GL11.glEnd();
    }

    @Override
    public void drawGL20_BFCuncertified(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        GL11.glBegin(GL11.GL_TRIANGLES);
        if (GData.globalNegativeDeterminant) {
            GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
        } else {
            GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
        }
        GL11.glEnd();
    }

    @Override
    public void drawGL20_BFC_backOnly(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        GL11.glBegin(GL11.GL_TRIANGLES);
        switch (GData.localWinding) {
        case BFC.CCW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            }
            break;
        case BFC.CW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glColor4f(View.BFC_back__Colour_r[0], View.BFC_back__Colour_g[0], View.BFC_back__Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            }
            break;
        case BFC.NOCERTIFY:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(View.BFC_uncertified_Colour_r[0], View.BFC_uncertified_Colour_g[0], View.BFC_uncertified_Colour_b[0], a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            }
        }
        GL11.glEnd();
    }

    @Override
    public void drawGL20_BFC_Colour(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        GL11.glBegin(GL11.GL_TRIANGLES);
        switch (a < 1f ? BFC.NOCERTIFY : GData.localWinding) {
        case BFC.CCW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            }
            break;
        case BFC.CW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            }
            break;
        case BFC.NOCERTIFY:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glNormal3f(xn, yn, zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x2, y2, z2);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glNormal3f(-xn, -yn, -zn);
                    GL11.glVertex3f(x1, y1, z1);
                    GL11.glVertex3f(x3, y3, z3);
                    GL11.glVertex3f(x2, y2, z2);
                }
            }
        }
        GL11.glEnd();
    }

    private void drawBFC_Colour2(Composite3D c3d, float r, float g, float b, float a, int useCubeMap) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        switch (a < 1f ? BFC.NOCERTIFY : GData.localWinding) {
        case BFC.CCW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    GL11.glEnd();
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    GL11.glEnd();
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    GL11.glEnd();
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    GL11.glEnd();
                }
            }
            break;
        case BFC.CW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    GL11.glEnd();
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    GL11.glNormal3f(xn, yn, zn);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    GL11.glEnd();
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    GL11.glEnd();
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    GL11.glEnd();
                }
            }
            break;
        case BFC.NOCERTIFY:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    GL11.glEnd();
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    GL11.glEnd();
                }
            } else {
                if (GData.globalInvertNext) {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    GL11.glEnd();
                } else {
                    GL11.glColor4f(r, g, b, a);
                    GL11.glBegin(GL11.GL_TRIANGLES);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                    c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                    GL11.glEnd();
                }
            }
        }
    }

    @Override
    public void drawGL20_BFC_Textured(Composite3D c3d) {
        if (GData.globalDrawObjects) {
            GColour c = View.getLDConfigColour(View.getLDConfigIndex(r, g, b));
            GColourType ct = c.getType();
            boolean hasColourType = ct != null;
            boolean matLight = true;
            int useCubeMap = 0;
            if (hasColourType) {
                switch (ct.type()) {
                case CHROME:
                    useCubeMap = 1;
                    break;
                case MATTE_METALLIC:
                    useCubeMap = 2;
                    break;
                case METAL:
                    useCubeMap = 3;
                    break;
                case RUBBER:
                    useCubeMap = 2;
                    matLight = false;
                    break;
                default:
                    break;
                }
            }
            float r = this.r;
            float g = this.g;
            float b = this.b;
            float a = this.a;
            if (hasColourType && useCubeMap < 1) {
                a = 0.99f;
            }
            final OpenGLRenderer20 ren = (OpenGLRenderer20) c3d.getRenderer();
            if (GData.globalTextureStack.isEmpty()) {
                GL20.glUniform1f(ren.getAlphaSwitchLoc(), c3d.isDrawingSolidMaterials() ? 1f : 0f); // Draw transparent
                GL20.glUniform1f(ren.getNormalSwitchLoc(), GData.globalNegativeDeterminant ^ GData.globalInvertNext ? 1f : 0f);
                GL20.glUniform1f(ren.getNoTextureSwitch(), 1f);
                GL20.glUniform1f(ren.getNoLightSwitch(), c3d.isLightOn() && matLight ? 0f : 1f);
                GL20.glUniform1f(ren.getCubeMapSwitch(), useCubeMap);
                switch (GData.accumClip) {
                case 0:
                    drawBFC_Colour2(c3d, r, g, b, a, useCubeMap);
                    break;
                default:
                    byte tmp = GData.localWinding;
                    GData.localWinding = BFC.NOCERTIFY;
                    drawBFC_Colour2(c3d, r, g, b, a, useCubeMap);
                    GData.localWinding = tmp;
                    break;
                }
            } else { // Draw the textured face
                if (!visible)
                    return;
                GTexture tex = GData.globalTextureStack.peek();
                tex.bind(c3d.isDrawingSolidMaterials(), GData.globalNegativeDeterminant ^ GData.globalInvertNext, c3d.isLightOn() && matLight, ren, useCubeMap);
                float[] uv;
                switch (a < 1f || GData.accumClip > 0 ? BFC.NOCERTIFY : GData.localWinding) {
                case BFC.CCW:
                    if (GData.globalNegativeDeterminant) {
                        if (GData.globalInvertNext) {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x3, y3, z3, parent);
                            tex.calcUVcoords3(x2, y2, z2, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                            GL11.glEnd();
                        } else {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x2, y2, z2, parent);
                            tex.calcUVcoords3(x3, y3, z3, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                            GL11.glEnd();
                        }
                    } else {
                        if (GData.globalInvertNext) {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x2, y2, z2, parent);
                            tex.calcUVcoords3(x3, y3, z3, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                            GL11.glEnd();
                        } else {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x3, y3, z3, parent);
                            tex.calcUVcoords3(x2, y2, z2, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                            GL11.glEnd();
                        }
                    }
                    break;
                case BFC.CW:
                    if (GData.globalNegativeDeterminant) {
                        if (GData.globalInvertNext) {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x2, y2, z2, parent);
                            tex.calcUVcoords3(x3, y3, z3, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                            GL11.glEnd();
                        } else {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x3, y3, z3, parent);
                            tex.calcUVcoords3(x2, y2, z2, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                            GL11.glEnd();
                        }
                    } else {
                        if (GData.globalInvertNext) {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x3, y3, z3, parent);
                            tex.calcUVcoords3(x2, y2, z2, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                            GL11.glEnd();
                        } else {
                            GL11.glColor4f(r, g, b, a);
                            GL11.glBegin(GL11.GL_TRIANGLES);
                            tex.calcUVcoords1(x1, y1, z1, parent, this);
                            tex.calcUVcoords2(x2, y2, z2, parent);
                            tex.calcUVcoords3(x3, y3, z3, parent);
                            uv = tex.getUVcoords(true, this);
                            GL11.glTexCoord2f(uv[0], uv[1]);
                            c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[2], uv[3]);
                            c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                            GL11.glTexCoord2f(uv[4], uv[5]);
                            c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                            GL11.glEnd();
                        }
                    }
                    break;
                case BFC.NOCERTIFY:
                    if (GData.globalNegativeDeterminant) {
                        GL11.glColor4f(r, g, b, a);
                        GL11.glBegin(GL11.GL_TRIANGLES);
                        tex.calcUVcoords1(x1, y1, z1, parent, null);
                        tex.calcUVcoords2(x3, y3, z3, parent);
                        tex.calcUVcoords3(x2, y2, z2, parent);
                        uv = tex.getUVcoords(true, null);
                        GL11.glTexCoord2f(uv[0], uv[1]);
                        c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                        GL11.glTexCoord2f(uv[2], uv[3]);
                        c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                        GL11.glTexCoord2f(uv[4], uv[5]);
                        c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                        GL11.glEnd();
                        GL11.glBegin(GL11.GL_TRIANGLES);
                        tex.calcUVcoords1(x1, y1, z1, parent, null);
                        tex.calcUVcoords2(x2, y2, z2, parent);
                        tex.calcUVcoords3(x3, y3, z3, parent);
                        uv = tex.getUVcoords(true, null);
                        GL11.glTexCoord2f(uv[0], uv[1]);
                        c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                        GL11.glTexCoord2f(uv[2], uv[3]);
                        c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                        GL11.glTexCoord2f(uv[4], uv[5]);
                        c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                        GL11.glEnd();
                    } else {
                        GL11.glColor4f(r, g, b, a);
                        GL11.glBegin(GL11.GL_TRIANGLES);
                        tex.calcUVcoords1(x1, y1, z1, parent, null);
                        tex.calcUVcoords2(x2, y2, z2, parent);
                        tex.calcUVcoords3(x3, y3, z3, parent);
                        uv = tex.getUVcoords(true, null);
                        GL11.glTexCoord2f(uv[0], uv[1]);
                        c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, false, this, useCubeMap);
                        GL11.glTexCoord2f(uv[2], uv[3]);
                        c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, false, this, useCubeMap);
                        GL11.glTexCoord2f(uv[4], uv[5]);
                        c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, false, this, useCubeMap);
                        GL11.glEnd();
                        GL11.glBegin(GL11.GL_TRIANGLES);
                        tex.calcUVcoords1(x1, y1, z1, parent, null);
                        tex.calcUVcoords2(x3, y3, z3, parent);
                        tex.calcUVcoords3(x2, y2, z2, parent);
                        uv = tex.getUVcoords(true, null);
                        GL11.glTexCoord2f(uv[0], uv[1]);
                        c3d.getVertexManager().setVertexAndNormal(x1, y1, z1, true, this, useCubeMap);
                        GL11.glTexCoord2f(uv[2], uv[3]);
                        c3d.getVertexManager().setVertexAndNormal(x3, y3, z3, true, this, useCubeMap);
                        GL11.glTexCoord2f(uv[4], uv[5]);
                        c3d.getVertexManager().setVertexAndNormal(x2, y2, z2, true, this, useCubeMap);
                        GL11.glEnd();
                    }
                }
            }
        }
        if (GData.globalFoundTEXMAPNEXT) {
            GData.globalFoundTEXMAPStack.pop();
            GData.globalTextureStack.pop();
            GData.globalFoundTEXMAPStack.push(false);
            GData.globalFoundTEXMAPNEXT = false;
        }
    }

    @Override
    public void drawGL20_WhileAddCondlines(Composite3D c3d) {
        drawGL20_BFC(c3d);
    }

    @Override
    public void drawGL20_CoplanarityHeatmap(Composite3D c3d) {
        if (!visible)
            return;
        if (a < 1f && c3d.isDrawingSolidMaterials() || !c3d.isDrawingSolidMaterials() && a == 1f)
            return;
        if (!isTriangle) {
            drawProtractor_GL20(false, c3d, X1, Y1, Z1, X2, Y2, Z2, X3, Y3, Z3);
            return;
        }
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glColor4f(0f, 0f, 1f, a);
        if (GData.globalNegativeDeterminant) {
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
        } else {
            GL11.glNormal3f(xn, yn, zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glNormal3f(-xn, -yn, -zn);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x3, y3, z3);
            GL11.glVertex3f(x2, y2, z2);
        }
        GL11.glEnd();
    }

    @Override
    public void drawGL20_Wireframe(Composite3D c3d) {}

    @Override
    public int type() {
        return 3;
    }

    @Override
    String getNiceString() {
        if (text != null)
            return text;
        StringBuilder lineBuilder = new StringBuilder();
        if (isTriangle) {
            lineBuilder.append("3 "); //$NON-NLS-1$
        } else {
            lineBuilder.append("0 !LPE PROTRACTOR "); //$NON-NLS-1$
        }
        if (colourNumber == -1) {
            lineBuilder.append("0x2"); //$NON-NLS-1$
            lineBuilder.append(MathHelper.toHex((int) (255f * r)).toUpperCase());
            lineBuilder.append(MathHelper.toHex((int) (255f * g)).toUpperCase());
            lineBuilder.append(MathHelper.toHex((int) (255f * b)).toUpperCase());
        } else {
            lineBuilder.append(colourNumber);
        }
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(X1));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Y1));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Z1));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(X2));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Y2));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Z2));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(X3));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Y3));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Z3));
        text = lineBuilder.toString();
        return text;
    }

    @Override
    public String inlinedString(byte bfc, GColour colour) {
        return getNiceString();
    }

    @Override
    public String transformAndColourReplace(String colour, Matrix matrix) {
        BigDecimal[] v1;
        BigDecimal[] v2;
        BigDecimal[] v3;
        if (X1 == null) {
            v1 = matrix.transform(new BigDecimal(x1 / 1000f), new BigDecimal(y1 / 1000f), new BigDecimal(z1 / 1000f));
            v2 = matrix.transform(new BigDecimal(x2 / 1000f), new BigDecimal(y2 / 1000f), new BigDecimal(z2 / 1000f));
            v3 = matrix.transform(new BigDecimal(x3 / 1000f), new BigDecimal(y3 / 1000f), new BigDecimal(z3 / 1000f));
        } else {
            v1 = matrix.transform(X1, Y1, Z1);
            v2 = matrix.transform(X2, Y2, Z2);
            v3 = matrix.transform(X3, Y3, Z3);
        }
        StringBuilder lineBuilder = new StringBuilder();
        if (isTriangle) {
            lineBuilder.append(3);
            lineBuilder.append(" "); //$NON-NLS-1$
        } else {
            lineBuilder.append("0 !LPE PROTRACTOR "); //$NON-NLS-1$
        }
        StringBuilder colourBuilder = new StringBuilder();
        if (colourNumber == -1) {
            colourBuilder.append("0x2"); //$NON-NLS-1$
            colourBuilder.append(MathHelper.toHex((int) (255f * r)).toUpperCase());
            colourBuilder.append(MathHelper.toHex((int) (255f * g)).toUpperCase());
            colourBuilder.append(MathHelper.toHex((int) (255f * b)).toUpperCase());
        } else {
            colourBuilder.append(colourNumber);
        }
        String col = colourBuilder.toString();
        if (col.equals(colour))
            col = "16"; //$NON-NLS-1$
        lineBuilder.append(col);
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v1[0]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v1[1]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v1[2]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v2[0]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v2[1]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v2[2]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v3[0]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v3[1]));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(v3[2]));
        return lineBuilder.toString();
    }

    @Override
    public void getBFCorientationMap(HashMap<GData, Byte> map) {
        switch (GData.localWinding) {
        case BFC.CCW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    map.put(this, BFC.CCW);
                } else {
                    map.put(this, BFC.CW);
                }
            } else {
                if (GData.globalInvertNext) {
                    map.put(this, BFC.CW);
                } else {
                    map.put(this, BFC.CCW);
                }
            }
            break;
        case BFC.CW:
            if (GData.globalNegativeDeterminant) {
                if (GData.globalInvertNext) {
                    map.put(this, BFC.CW);
                } else {
                    map.put(this, BFC.CCW);
                }
            } else {
                if (GData.globalInvertNext) {
                    map.put(this, BFC.CCW);
                } else {
                    map.put(this, BFC.CW);
                }
            }
            break;
        case BFC.NOCERTIFY:
            // map.put(this, BFC.NOCERTIFY);
            break;
        }
    }

    @Override
    public void getBFCorientationMapNOCERTIFY(HashMap<GData, Byte> map) {
        // map.put(this, BFC.NOCERTIFY);
    }

    @Override
    public void getBFCorientationMapNOCLIP(HashMap<GData, Byte> map) {
        map.put(this, BFC.NOCLIP);
    }

    @Override
    public void getVertexNormalMap(GDataState state, ThreadsafeTreeMap<Vertex, float[]> vertexLinkedToNormalCACHE, ThreadsafeHashMap<GData, float[]> dataLinkedToNormalCACHE, VM00Base vm) {
        if (state.globalDrawObjects) {
            Vertex[] verts = vm.getTriangles_NOCLONE().get(this);
            if (verts == null) {
                verts = new Vertex[3];
                Vector4f v1 = new Vector4f(x1, y1, z1, 1f);
                Vector4f v2 = new Vector4f(x2, y2, z2, 1f);
                Vector4f v3 = new Vector4f(x3, y3, z3, 1f);
                Matrix4f.transform(this.parent.productMatrix, v1, v1);
                Matrix4f.transform(this.parent.productMatrix, v2, v2);
                Matrix4f.transform(this.parent.productMatrix, v3, v3);
                verts[0] = new Vertex(v1.x , v1.y , v1.z, true);
                verts[1] = new Vertex(v2.x , v2.y , v2.z, true);
                verts[2] = new Vertex(v3.x , v3.y , v3.z, true);
            }

            float xn = (verts[2].y - verts[0].y) * (verts[1].z - verts[0].z) - (verts[2].z - verts[0].z) * (verts[1].y - verts[0].y);
            float yn = (verts[2].z - verts[0].z) * (verts[1].x - verts[0].x) - (verts[2].x - verts[0].x) * (verts[1].z - verts[0].z);
            float zn = (verts[2].x - verts[0].x) * (verts[1].y - verts[0].y) - (verts[2].y - verts[0].y) * (verts[1].x - verts[0].x);

            final float length = (float) Math.sqrt(xn * xn + yn * yn + zn *zn);
            if (length > 0) {
                xn = xn / length;
                yn = yn / length;
                zn = zn / length;
            }

            for (Vertex vertex : verts) {
                float[] result = new float[3];
                switch (state.localWinding) {
                case BFC.NOCLIP:
                    result[0] = xn;
                    result[1] = yn;
                    result[2] = zn;
                    break;
                case BFC.CCW:
                    if (state.globalInvertNext) {
                        if (state.globalNegativeDeterminant) {
                            result[0] = -xn;
                            result[1] = -yn;
                            result[2] = -zn;
                        } else {
                            result[0] = xn;
                            result[1] = yn;
                            result[2] = zn;
                        }
                    } else {
                        if (state.globalNegativeDeterminant) {
                            result[0] = -xn;
                            result[1] = -yn;
                            result[2] = -zn;
                        } else {
                            result[0] = xn;
                            result[1] = yn;
                            result[2] = zn;
                        }
                    }
                    break;
                case BFC.CW:
                    if (state.globalInvertNext) {
                        if (state.globalNegativeDeterminant) {
                            result[0] = -xn;
                            result[1] = -yn;
                            result[2] = -zn;
                        } else {
                            result[0] = xn;
                            result[1] = yn;
                            result[2] = zn;
                        }
                    } else {
                        if (state.globalNegativeDeterminant) {
                            result[0] = -xn;
                            result[1] = -yn;
                            result[2] = -zn;
                        } else {
                            result[0] = xn;
                            result[1] = yn;
                            result[2] = zn;
                        }
                    }
                    break;
                case BFC.NOCERTIFY:
                    break;
                }
                if (state.globalInvertNext) {
                    dataLinkedToNormalCACHE.put(this, new float[]{-result[0], -result[1], -result[2]});
                } else {
                    dataLinkedToNormalCACHE.put(this, new float[]{result[0], result[1], result[2]});
                }
                if (!vertexLinkedToNormalCACHE.containsKey(vertex)) {
                    vertexLinkedToNormalCACHE.put(vertex, result);
                } else {
                    float[] n = vertexLinkedToNormalCACHE.get(vertex);
                    n[0] = n[0] + result[0];
                    n[1] = n[1] + result[1];
                    n[2] = n[2] + result[2];
                }
            }
        }
        if (state.globalFoundTEXMAPNEXT) {
            state.globalFoundTEXMAPStack.pop();
            state.globalTextureStack.pop();
            state.globalFoundTEXMAPStack.push(false);
            state.globalFoundTEXMAPNEXT = false;
        }
    }

    @Override
    public void getVertexNormalMapNOCERTIFY(GDataState state, ThreadsafeTreeMap<Vertex, float[]> vertexLinkedToNormalCACHE, ThreadsafeHashMap<GData, float[]> dataLinkedToNormalCACHE, VM00Base vm) {}

    @Override
    public void getVertexNormalMapNOCLIP(GDataState state, ThreadsafeTreeMap<Vertex, float[]> vertexLinkedToNormalCACHE, ThreadsafeHashMap<GData, float[]> dataLinkedToNormalCACHE, VM00Base vm) {
        getVertexNormalMap(state, vertexLinkedToNormalCACHE, dataLinkedToNormalCACHE, vm);
    }

    public boolean isCollinear() {
        double angle;
        Vector3d vertexA = new Vector3d(X1, Y1, Z1);
        Vector3d vertexB = new Vector3d(X2, Y2, Z2);
        Vector3d vertexC = new Vector3d(X3, Y3, Z3);
        Vector3d A = new Vector3d();
        Vector3d B = new Vector3d();
        Vector3d C = new Vector3d();
        Vector3d.sub(vertexB, vertexA, A);
        Vector3d.sub(vertexC, vertexB, B);
        Vector3d.sub(vertexC, vertexA, C);

        angle = Vector3d.angle(A, C);
        double sumAngle = angle;
        if (angle < Threshold.collinear_angle_minimum || angle > Threshold.collinear_angle_maximum) {
            return true;
        }

        A.negate();
        angle = Vector3d.angle(A, B);
        sumAngle = sumAngle + angle;
        if (angle < Threshold.collinear_angle_minimum || angle > Threshold.collinear_angle_maximum) {
            return true;
        }

        angle = 180.0 - sumAngle;
        if (angle < Threshold.collinear_angle_minimum || angle > Threshold.collinear_angle_maximum) {
            return true;
        }
        return false;
    }

    public String colourReplace(String col) {
        StringBuilder lineBuilder = new StringBuilder();
        if (isTriangle) {
            lineBuilder.append(3);
            lineBuilder.append(" "); //$NON-NLS-1$
        } else {
            lineBuilder.append("0 !LPE PROTRACTOR "); //$NON-NLS-1$
        }
        lineBuilder.append(col);
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(X1));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Y1));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Z1));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(X2));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Y2));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Z2));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(X3));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Y3));
        lineBuilder.append(" "); //$NON-NLS-1$
        lineBuilder.append(bigDecimalToString(Z3));
        return lineBuilder.toString();
    }

    public void drawProtractorGL33(Composite3D c3d, GLShader shader, BigDecimal x1c, BigDecimal y1c, BigDecimal z1c, BigDecimal x2c, BigDecimal y2c, BigDecimal z2c, BigDecimal x3c, BigDecimal y3c, BigDecimal z3c) {
        GL20.glUniform3f(shader.getUniformLocation("color"), r, g, b); //$NON-NLS-1$

        final java.text.DecimalFormat NUMBER_FORMAT2F = new java.text.DecimalFormat(View.NUMBER_FORMAT2F, new DecimalFormatSymbols(MyLanguage.LOCALE));
        final float zoom = 1f / c3d.getZoom();

        final Vector4f textOrigin = new Vector4f(x1, y1, z1, 1f);
        Matrix4f.transform(c3d.getRotation(), textOrigin, textOrigin);

        Vector3d va = new Vector3d(x1c, y1c, z1c);
        Vector3d vb = new Vector3d(x2c, y2c, z2c);
        Vector3d vc = new Vector3d(x3c, y3c, z3c);
        vb = Vector3d.sub(va, vb);
        vc = Vector3d.sub(va, vc);
        double angle = Vector3d.angle(vb, vc);
        BigDecimal ang = new BigDecimal(angle);
        String angle_s = NUMBER_FORMAT2F.format(ang) + "°"; //$NON-NLS-1$

        drawNumberGL33(angle_s, textOrigin.x, textOrigin.y, textOrigin.z, zoom);
    }

    public void drawProtractor_GL20(boolean selected, Composite3D c3d, BigDecimal x1c, BigDecimal y1c, BigDecimal z1c, BigDecimal x2c, BigDecimal y2c, BigDecimal z2c, BigDecimal x3c, BigDecimal y3c, BigDecimal z3c) {
        final java.text.DecimalFormat NUMBER_FORMAT2F = new java.text.DecimalFormat(View.NUMBER_FORMAT2F, new DecimalFormatSymbols(MyLanguage.LOCALE));
        final OpenGLRenderer20 renderer = (OpenGLRenderer20) c3d.getRenderer();
        final float zoom = 1f / c3d.getZoom();

        GL11.glDisable(GL11.GL_LIGHTING);

        GL11.glLineWidth(View.lineWidthGL[0]);
        if (selected) {
            GL11.glColor4f(View.vertex_selected_Colour_r[0], View.vertex_selected_Colour_g[0], View.vertex_selected_Colour_b[0], 1f);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glEnd();
        } else {
            final float s = ((r + g + b) / 3 + .5f) % 1f;
            GL11.glColor4f(s, s, s, 1f);
            GL11.glBegin(GL11.GL_LINES);
            GL11.glVertex3f(x1, y1, z1);
            GL11.glVertex3f(x2, y2, z2);
            GL11.glEnd();
        }
        GL11.glColor4f(r, g, b, 1f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(x1, y1, z1);
        GL11.glVertex3f(x3, y3, z3);
        GL11.glEnd();

        final Vector4f textOrigin = new Vector4f(x1, y1, z1, 1f);
        Matrix4f.transform(c3d.getRotation(), textOrigin, textOrigin);

        Vector3d va = new Vector3d(x1c, y1c, z1c);
        Vector3d vb = new Vector3d(x2c, y2c, z2c);
        Vector3d vc = new Vector3d(x3c, y3c, z3c);
        vb = Vector3d.sub(va, vb);
        vc = Vector3d.sub(va, vc);
        double angle = Vector3d.angle(vb, vc);
        BigDecimal ang = new BigDecimal(angle);
        String angle_s = NUMBER_FORMAT2F.format(ang) + "°"; //$NON-NLS-1$

        float sx1 = x1 + (x2 - x1) * .2f;
        float sy1 = y1 + (y2 - y1) * .2f;
        float sz1 = z1 + (z2 - z1) * .2f;
        float sx2 = x1 + (x3 - x1) * .2f;
        float sy2 = y1 + (y3 - y1) * .2f;
        float sz2 = z1 + (z3 - z1) * .2f;
        float sx1t = x1 + (x2 - x1) * .25f;
        float sy1t = y1 + (y2 - y1) * .25f;
        float sz1t = z1 + (z2 - z1) * .25f;
        float sx2t = x1 + (x3 - x1) * .25f;
        float sy2t = y1 + (y3 - y1) * .25f;
        float sz2t = z1 + (z3 - z1) * .25f;
        float sx1tt = x1 + (x2 - x1) * .24f;
        float sy1tt = y1 + (y2 - y1) * .24f;
        float sz1tt = z1 + (z2 - z1) * .24f;
        float sx2tt = x1 + (x3 - x1) * .24f;
        float sy2tt = y1 + (y3 - y1) * .24f;
        float sz2tt = z1 + (z3 - z1) * .24f;
        float sx3 = sx1t * .5f + sx2t * .5f;
        float sy3 = sy1t * .5f + sy2t * .5f;
        float sz3 = sz1t * .5f + sz2t * .5f;
        float sx3r = sx1tt * .7f + sx2tt * .3f;
        float sy3r = sy1tt * .7f + sy2tt * .3f;
        float sz3r = sz1tt * .7f + sz2tt * .3f;
        float sx3l = sx1tt * .3f + sx2tt * .7f;
        float sy3l = sy1tt * .3f + sy2tt * .7f;
        float sz3l = sz1tt * .3f + sz2tt * .7f;

        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(sx1, sy1, sz1);
        GL11.glVertex3f(sx3r, sy3r, sz3r);
        GL11.glVertex3f(sx3r, sy3r, sz3r);
        GL11.glVertex3f(sx3, sy3, sz3);
        GL11.glVertex3f(sx3, sy3, sz3);
        GL11.glVertex3f(sx3l, sy3l, sz3l);
        GL11.glVertex3f(sx3l, sy3l, sz3l);
        GL11.glVertex3f(sx2, sy2, sz2);
        GL11.glEnd();

        GL11.glPushMatrix();
        GL11.glMultMatrixf(renderer.getRotationInverse());

        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        PGData3.beginDrawText();
        GL11.glColor4f(r, g, b, 1f);
        drawNumber(angle_s, textOrigin.x, textOrigin.y, textOrigin.z, zoom);
        PGData3.endDrawText();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glPopMatrix();

        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void drawNumber(String number, float ox, float oy, float oz, float zoom) {
        final int length =  number.length();
        float ox2 = 0f;
        for (int i = 0; i < length; i++) {
            Set<PGData3> tris = new HashSet<PGData3>();
            final char c = number.charAt(i);
            switch (c) {
            case '0':
                tris = View.D0;
                break;
            case '1':
                tris = View.D1;
                break;
            case '2':
                tris = View.D2;
                break;
            case '3':
                tris = View.D3;
                break;
            case '4':
                tris = View.D4;
                break;
            case '5':
                tris = View.D5;
                break;
            case '6':
                tris = View.D6;
                break;
            case '7':
                tris = View.D7;
                break;
            case '8':
                tris = View.D8;
                break;
            case '9':
                tris = View.D9;
                break;
            case '.':
                tris = View.Dd;
                break;
            case ',':
                tris = View.Dc;
                break;
            case '°':
                tris = View.Dg;
                break;
            case '-':
                tris = View.DM;
                break;
            default:
                break;
            }
            for (PGData3 tri : tris) {
                tri.drawText(ox + ox2, oy, oz + 100000f, zoom);
            }
            ox2 = ox2 - .01f * zoom;
        }
    }

    private void drawNumberGL33(String number, float ox, float oy, float oz, float zoom) {
        final int length =  number.length();
        float ox2 = 0f;
        for (int i = 0; i < length; i++) {
            Set<PGData3> tris = new HashSet<PGData3>();
            final char c = number.charAt(i);
            switch (c) {
            case '0':
                tris = View.D0;
                break;
            case '1':
                tris = View.D1;
                break;
            case '2':
                tris = View.D2;
                break;
            case '3':
                tris = View.D3;
                break;
            case '4':
                tris = View.D4;
                break;
            case '5':
                tris = View.D5;
                break;
            case '6':
                tris = View.D6;
                break;
            case '7':
                tris = View.D7;
                break;
            case '8':
                tris = View.D8;
                break;
            case '9':
                tris = View.D9;
                break;
            case '.':
                tris = View.Dd;
                break;
            case ',':
                tris = View.Dc;
                break;
            case '°':
                tris = View.Dg;
                break;
            case '-':
                tris = View.DM;
                break;
            default:
                break;
            }
            for (PGData3 tri : tris) {
                tri.drawTextGL33_VAO(ox + ox2, oy, oz + 100000f, zoom);
            }
            ox2 = ox2 - .01f * zoom;
        }
    }

    public double getProtractorAngle() {
        Vector3d va = new Vector3d(X1, Y1, Z1);
        Vector3d vb = new Vector3d(X2, Y2, Z2);
        Vector3d vc = new Vector3d(X3, Y3, Z3);
        vb = Vector3d.sub(va, vb);
        vc = Vector3d.sub(va, vc);
        return Vector3d.angle(vb, vc);
    }

    public BigDecimal getProtractorLength() {
        Vector3d va = new Vector3d(X1, Y1, Z1);
        Vector3d vc = new Vector3d(X3, Y3, Z3);
        vc = Vector3d.sub(va, vc);
        return vc.length();
    }

    public int insertProtractor(Vertex[] v, float[] lineData, int lineIndex) {

        final float s = ((r + g + b) / 3 + .5f) % 1f;

        GL33Helper.pointAt7(0, x1, y1, z1, lineData, lineIndex);
        GL33Helper.pointAt7(1, x2, y2, z2, lineData, lineIndex);
        GL33Helper.colourise7(0, 2, s, s, s, 7f, lineData, lineIndex);

        lineIndex += 2;

        GL33Helper.pointAt7(0, x1, y1, z1, lineData, lineIndex);
        GL33Helper.pointAt7(1, x3, y3, z3, lineData, lineIndex);
        GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

        lineIndex += 2;

        GL33Helper.pointAt7(0, v[0].x, v[0].y, v[0].z, lineData, lineIndex);
        GL33Helper.pointAt7(1, v[1].x, v[1].y, v[1].z, lineData, lineIndex);
        GL33Helper.colourise7(0, 2, View.vertex_selected_Colour_r[0], View.vertex_selected_Colour_g[0], View.vertex_selected_Colour_b[0], 7f, lineData, lineIndex);

        lineIndex += 2;

        GL33Helper.pointAt7(0, v[0].x, v[0].y, v[0].z, lineData, lineIndex);
        GL33Helper.pointAt7(1, v[2].x, v[2].y, v[2].z, lineData, lineIndex);
        GL33Helper.colourise7(0, 2, View.vertex_selected_Colour_r[0], View.vertex_selected_Colour_g[0], View.vertex_selected_Colour_b[0], 7f, lineData, lineIndex);

        lineIndex += 2;

        {
            float sx1 = x1 + (x2 - x1) * .2f;
            float sy1 = y1 + (y2 - y1) * .2f;
            float sz1 = z1 + (z2 - z1) * .2f;
            float sx2 = x1 + (x3 - x1) * .2f;
            float sy2 = y1 + (y3 - y1) * .2f;
            float sz2 = z1 + (z3 - z1) * .2f;
            float sx1t = x1 + (x2 - x1) * .25f;
            float sy1t = y1 + (y2 - y1) * .25f;
            float sz1t = z1 + (z2 - z1) * .25f;
            float sx2t = x1 + (x3 - x1) * .25f;
            float sy2t = y1 + (y3 - y1) * .25f;
            float sz2t = z1 + (z3 - z1) * .25f;
            float sx1tt = x1 + (x2 - x1) * .24f;
            float sy1tt = y1 + (y2 - y1) * .24f;
            float sz1tt = z1 + (z2 - z1) * .24f;
            float sx2tt = x1 + (x3 - x1) * .24f;
            float sy2tt = y1 + (y3 - y1) * .24f;
            float sz2tt = z1 + (z3 - z1) * .24f;
            float sx3 = sx1t * .5f + sx2t * .5f;
            float sy3 = sy1t * .5f + sy2t * .5f;
            float sz3 = sz1t * .5f + sz2t * .5f;
            float sx3r = sx1tt * .7f + sx2tt * .3f;
            float sy3r = sy1tt * .7f + sy2tt * .3f;
            float sz3r = sz1tt * .7f + sz2tt * .3f;
            float sx3l = sx1tt * .3f + sx2tt * .7f;
            float sy3l = sy1tt * .3f + sy2tt * .7f;
            float sz3l = sz1tt * .3f + sz2tt * .7f;

            GL33Helper.pointAt7(0, sx1, sy1, sz1, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx3r, sy3r, sz3r, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;

            GL33Helper.pointAt7(0, sx3r, sy3r, sz3r, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx3, sy3, sz3, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;

            GL33Helper.pointAt7(0, sx3, sy3, sz3, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx3l, sy3l, sz3l, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;

            GL33Helper.pointAt7(0, sx3l, sy3l, sz3l, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx2, sy2, sz2, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;
        }

        {
            float x1 = v[0].x;
            float y1 = v[0].y;
            float z1 = v[0].z;
            float x2 = v[1].x;
            float y2 = v[1].y;
            float z2 = v[1].z;
            float x3 = v[2].x;
            float y3 = v[2].y;
            float z3 = v[2].z;

            float sx1 = x1 + (x2 - x1) * .2f;
            float sy1 = y1 + (y2 - y1) * .2f;
            float sz1 = z1 + (z2 - z1) * .2f;
            float sx2 = x1 + (x3 - x1) * .2f;
            float sy2 = y1 + (y3 - y1) * .2f;
            float sz2 = z1 + (z3 - z1) * .2f;
            float sx1t = x1 + (x2 - x1) * .25f;
            float sy1t = y1 + (y2 - y1) * .25f;
            float sz1t = z1 + (z2 - z1) * .25f;
            float sx2t = x1 + (x3 - x1) * .25f;
            float sy2t = y1 + (y3 - y1) * .25f;
            float sz2t = z1 + (z3 - z1) * .25f;
            float sx1tt = x1 + (x2 - x1) * .24f;
            float sy1tt = y1 + (y2 - y1) * .24f;
            float sz1tt = z1 + (z2 - z1) * .24f;
            float sx2tt = x1 + (x3 - x1) * .24f;
            float sy2tt = y1 + (y3 - y1) * .24f;
            float sz2tt = z1 + (z3 - z1) * .24f;
            float sx3 = sx1t * .5f + sx2t * .5f;
            float sy3 = sy1t * .5f + sy2t * .5f;
            float sz3 = sz1t * .5f + sz2t * .5f;
            float sx3r = sx1tt * .7f + sx2tt * .3f;
            float sy3r = sy1tt * .7f + sy2tt * .3f;
            float sz3r = sz1tt * .7f + sz2tt * .3f;
            float sx3l = sx1tt * .3f + sx2tt * .7f;
            float sy3l = sy1tt * .3f + sy2tt * .7f;
            float sz3l = sz1tt * .3f + sz2tt * .7f;

            GL33Helper.pointAt7(0, sx1, sy1, sz1, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx3r, sy3r, sz3r, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;

            GL33Helper.pointAt7(0, sx3r, sy3r, sz3r, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx3, sy3, sz3, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;

            GL33Helper.pointAt7(0, sx3, sy3, sz3, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx3l, sy3l, sz3l, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);

            lineIndex += 2;

            GL33Helper.pointAt7(0, sx3l, sy3l, sz3l, lineData, lineIndex);
            GL33Helper.pointAt7(1, sx2, sy2, sz2, lineData, lineIndex);
            GL33Helper.colourise7(0, 2, r, g, b, 7f, lineData, lineIndex);
        }

        return 24;
    }

    /**
     * Calculates the "area" of the triangle, but does take the square root and does not divide by two.<br>
     * (1/2) * Math.sqrt((x2⋅y3−x3⋅y2)²+(x3⋅y1−x1⋅y3)²+(x1⋅y2−x2⋅y1)²)
     *
     * @return the double-squared-area
     */
    public BigDecimal getDoubleSquaredArea() {

        final BigDecimal d1 = X2.multiply(Y3).subtract(X3.multiply(Y2));
        final BigDecimal d2 = X3.multiply(Y1).subtract(X1.multiply(Y3));
        final BigDecimal d3 = X1.multiply(Y2).subtract(X2.multiply(Y1));

        return d1.add(d2).add(d3);
    }
}
