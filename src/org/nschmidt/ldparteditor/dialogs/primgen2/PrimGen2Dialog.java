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
package org.nschmidt.ldparteditor.dialogs.primgen2;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ExtendedModifyEvent;
import org.eclipse.swt.custom.ExtendedModifyListener;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import org.nschmidt.ldparteditor.data.GData;
import org.nschmidt.ldparteditor.data.GDataCSG;
import org.nschmidt.ldparteditor.data.VertexManager;
import org.nschmidt.ldparteditor.enums.MyLanguage;
import org.nschmidt.ldparteditor.enums.View;
import org.nschmidt.ldparteditor.project.Project;
import org.nschmidt.ldparteditor.text.SyntaxFormatter;
import org.nschmidt.ldparteditor.widgets.BigDecimalSpinner;
import org.nschmidt.ldparteditor.widgets.IntegerSpinner;
import org.nschmidt.ldparteditor.widgets.ValueChangeAdapter;
import org.nschmidt.ldparteditor.workbench.UserSettingState;
import org.nschmidt.ldparteditor.workbench.WorkbenchManager;

/**
 *
 * <p>
 * Note: This class should be instantiated, it defines all listeners and part of
 * the business logic. It overrides the {@code open()} method to invoke the
 * listener definitions ;)
 *
 * @author nils
 *
 */
public class PrimGen2Dialog extends PrimGen2Design {

    private final int CIRCLE = 0;
    private final int RING = 1;
    private final int CONE = 2;
    private final int TORUS = 3;
    private final int CYLINDER = 4;
    private final int DISC = 5;
    private final int DISC_NEGATIVE = 6;
    private final int CHORD = 7;
    
    private boolean doUpdate = false;
    
    private final DecimalFormat DEC_VFORMAT_4F = new DecimalFormat(View.NUMBER_FORMAT4F, new DecimalFormatSymbols(MyLanguage.LOCALE));
    private final DecimalFormat DEC_VFORMAT_0F = new DecimalFormat(View.NUMBER_FORMAT0F, new DecimalFormatSymbols(MyLanguage.LOCALE));
    
    private enum EventType {
        SPN,
        CBO
    }
    
    private enum DEC_FORMAT_4F {
        INSTANCE;
        
        private static DecimalFormat DF4F = new DecimalFormat(View.NUMBER_FORMAT4F, new DecimalFormatSymbols(Locale.ENGLISH));
        
        public static String format(double d) {
            String result = DF4F.format(d + 0.00001d);
            return result;
        }
    }
    
    // I18N Needs translation / i18n!
    
    // FIXME Needs implementation (Logic)!
    
    private SyntaxFormatter syntaxFormatter;
    
    /**
     * Create the dialog.
     *
     * @param parentShell
     * @param es
     */
    public PrimGen2Dialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public int open() {
        super.create();
        
        syntaxFormatter = new SyntaxFormatter(txt_data[0]);
        
        spn_major[0].setValue(2);
        spn_minor[0].setValue(BigDecimal.ONE);
        
        Display.getCurrent().asyncExec(new Runnable() {
            @Override
            public void run() {
                
                lbl_standard[0].setText("STANDARD"); //$NON-NLS-1$
                        
                final StringBuilder sb = new StringBuilder();
                sb.append("0 Circle 0.25\n"); //$NON-NLS-1$
                sb.append("0 Name: 1-4edge.dat\n"); //$NON-NLS-1$
                
                final UserSettingState user = WorkbenchManager.getUserSettingState();
                
                String ldrawName = user.getLdrawUserName();
                String realName = user.getRealUserName();
                if (ldrawName == null || ldrawName.isEmpty()) {
                    if (realName == null || realName.isEmpty()) {
                        sb.append("0 Author: Primitive Generator 2\n"); //$NON-NLS-1$
                    } else {
                        sb.append("0 Author: " + realName + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                    }
                } else {
                    if (realName == null || realName.isEmpty()) {
                        sb.append("0 Author: [" + ldrawName + "]\n"); //$NON-NLS-1$ //$NON-NLS-2$                        
                    } else {
                        sb.append("0 Author: " + realName + " [" + ldrawName + "]\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                    }
                }                
                
                sb.append("0 !LDRAW_ORG Unofficial_Primitive\n"); //$NON-NLS-1$
                sb.append("0 !LICENSE Redistributable under CCAL version 2.0 : see CAreadme.txt\n\n"); //$NON-NLS-1$

                sb.append("0 BFC CERTIFY CCW\n\n"); //$NON-NLS-1$
                
                sb.append("2 24 1 0 0 0.9239 0 0.3827\n"); //$NON-NLS-1$
                sb.append("2 24 0.9239 0 0.3827 0.7071 0 0.7071\n"); //$NON-NLS-1$
                sb.append("2 24 0.7071 0 0.7071 0.3827 0 0.9239\n"); //$NON-NLS-1$
                sb.append("2 24 0.3827 0 0.9239 0 0 1\n"); //$NON-NLS-1$
                sb.append("0 // Build by Primitive Generator 2"); //$NON-NLS-1$
                
                c3d.setRenderMode(6);
                c3d.getModifier().switchMeshLines(false);
                txt_data[0].setText(sb.toString());
            }
        });
        
        // MARK All final listeners will be configured here..
        
        txt_data[0].addLineStyleListener(new LineStyleListener() {
            @Override
            public void lineGetStyle(final LineStyleEvent e) {
                // So the line will be formated with the syntax formatter from
                // the CompositeText.
                final VertexManager vm = df.getVertexManager();
                final GData data = df.getDrawPerLine_NOCLONE().getValue(txt_data[0].getLineAtOffset(e.lineOffset) + 1);
                boolean isSelected = vm.isSyncWithTextEditor() && vm.getSelectedData().contains(data);
                isSelected = isSelected || vm.isSyncWithTextEditor() && GDataCSG.getSelection(df).contains(data);
                syntaxFormatter.format(e,
                        BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                        0f, false, isSelected, df);
            }
        });
        
        txt_data[0].addExtendedModifyListener(new ExtendedModifyListener() {
            @Override
            public void modifyText(ExtendedModifyEvent event) {
                df.disposeData();
                df.setText(txt_data[0].getText());
                df.parseForData(false);
                Display.getCurrent().asyncExec(new Runnable() {
                    @Override
                    public void run() {
                        c3d.getRenderer().drawScene();
                    }
                });
            }
        });
        
        btn_ok[0].removeListener(SWT.Selection, btn_ok[0].getListeners(SWT.Selection)[0]);
        btn_cancel[0].removeListener(SWT.Selection, btn_cancel[0].getListeners(SWT.Selection)[0]);
        
        btn_ok[0].addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // FIXME Needs implementation!
                getShell().close();
            }
        });
        
        btn_cancel[0].addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                getShell().close();
            }
        });
        
        cmb_type[0].addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                
                doUpdate = true;
                        
                switch (cmb_type[0].getSelectionIndex()) {
                case CIRCLE:                              
                case CYLINDER:
                case DISC:
                case DISC_NEGATIVE:
                case CHORD:
                    lbl_minor[0].setText("Minor"); //$NON-NLS-1$
                    lbl_major[0].setEnabled(false);
                    lbl_minor[0].setEnabled(false);                    
                    lbl_size[0].setEnabled(false);
                    lbl_torusType[0].setEnabled(false);
                    spn_major[0].setEnabled(false);
                    spn_minor[0].setEnabled(false);
                    spn_size[0].setEnabled(false);
                    cmb_torusType[0].setEnabled(false);
                    spn_minor[0].setNumberFormat(DEC_VFORMAT_0F);
                    spn_size[0].setNumberFormat(DEC_VFORMAT_0F);
                    spn_size[0].setValue(BigDecimal.ONE);
                    cmb_torusType[0].select(1);
                    break;
                case RING:
                case CONE:
                    lbl_minor[0].setText("Width"); //$NON-NLS-1$
                    lbl_major[0].setEnabled(false);
                    lbl_minor[0].setEnabled(true);                    
                    lbl_size[0].setEnabled(true);
                    lbl_torusType[0].setEnabled(false);
                    spn_major[0].setEnabled(false);
                    spn_minor[0].setEnabled(true);
                    spn_size[0].setEnabled(true);
                    cmb_torusType[0].setEnabled(false);
                    spn_minor[0].setNumberFormat(DEC_VFORMAT_4F);
                    spn_size[0].setNumberFormat(DEC_VFORMAT_4F);
                    spn_size[0].setValue(BigDecimal.ONE);
                    cmb_torusType[0].select(1);
                    break;
                case TORUS:
                    lbl_minor[0].setText("Minor"); //$NON-NLS-1$
                    lbl_major[0].setEnabled(true);
                    lbl_minor[0].setEnabled(true);                    
                    lbl_size[0].setEnabled(false);
                    lbl_torusType[0].setEnabled(true);
                    spn_major[0].setEnabled(true);
                    spn_minor[0].setEnabled(true);
                    spn_size[0].setEnabled(false);
                    cmb_torusType[0].setEnabled(true);
                    spn_minor[0].setNumberFormat(DEC_VFORMAT_0F);
                    spn_size[0].setNumberFormat(DEC_VFORMAT_4F);
                    spn_size[0].setValue(BigDecimal.ONE);
                    cmb_torusType[0].select(1);
                    break;
                default:
                    break;
                }
                
                BigDecimal minor = spn_minor[0].getValue();
                BigDecimal size = spn_size[0].getValue();
                
                spn_minor[0].setValue(BigDecimal.ZERO);
                spn_size[0].setValue(BigDecimal.ZERO);
                
                spn_minor[0].setValue(minor);
                spn_size[0].setValue(size);
                
                
                doUpdate = false;
                
                rebuildPrimitive(EventType.CBO, cmb_type[0]);
            }
        });
        
        final SelectionAdapter sa = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                rebuildPrimitive(EventType.CBO, e.widget);
            }
        };
        
        final ValueChangeAdapter vca = new ValueChangeAdapter() {
            @Override
            public void valueChanged(IntegerSpinner spn) {
                rebuildPrimitive(EventType.SPN, spn);
            }
            @Override
            public void valueChanged(BigDecimalSpinner spn) {
                rebuildPrimitive(EventType.SPN, spn);
            }
        };
        
        spn_divisions[0].addValueChangeListener(vca);
        spn_major[0].addValueChangeListener(vca);
        spn_minor[0].addValueChangeListener(vca);
        spn_segments[0].addValueChangeListener(vca);
        spn_size[0].addValueChangeListener(vca);
        
        cmb_divisions[0].addSelectionListener(sa);
        cmb_segments[0].addSelectionListener(sa);
        cmb_torusType[0].addSelectionListener(sa);
        cmb_winding[0].addSelectionListener(sa);
        
        float backup = View.edge_threshold;
        View.edge_threshold = 5e6f;
        int result = super.open();
        View.edge_threshold = backup;
        Project.getUnsavedFiles().remove(df);        
        df.disposeData();
        Project.getOpenedFiles().remove(df);
        return result;
    } 
    
    @Override
    protected void handleShellCloseEvent() {
        c3d.getRenderer().disposeAllTextures();
        super.handleShellCloseEvent();
    }
    
    private void rebuildPrimitive(EventType et, Widget w) {
        
        if (doUpdate) {
            return;
        }
        
        boolean standard = true;
        
        boolean ccw = cmb_winding[0].getSelectionIndex() == 0;        
        int torusType = cmb_torusType[0].getSelectionIndex();
        
        doUpdate = true;
        
        if (cmb_torusType[0].getSelectionIndex() == 0 && spn_major[0].getValue() <= spn_minor[0].getValue().intValue()) {
            cmb_torusType[0].select(1);
        }
        
        if (w != spn_divisions[0] && w != cmb_divisions[0] && spn_segments[0].getValue() > spn_divisions[0].getValue()) {
            spn_divisions[0].setValue(spn_segments[0].getValue());
        }
        
        if (w != spn_segments[0] && w != cmb_segments[0] && spn_segments[0].getValue() > spn_divisions[0].getValue()) {
            spn_segments[0].setValue(spn_divisions[0].getValue());
        }
        
        if (et == EventType.CBO) {
            
            switch (cmb_divisions[0].getSelectionIndex()) {
            case 0:
                spn_divisions[0].setValue(8);
                break;
            case 1:
                spn_divisions[0].setValue(16);
                break;
            case 2:
                spn_divisions[0].setValue(48);
                break;
            default:
                break;
            }
            switch (cmb_segments[0].getSelectionIndex()) {
            case 0:
                spn_segments[0].setValue(spn_divisions[0].getValue() / 4);
                break;
            case 1:
                spn_segments[0].setValue(spn_divisions[0].getValue() / 2);
                break;
            case 2:
                spn_segments[0].setValue(spn_divisions[0].getValue() * 3 / 4);
                break;
            case 3:
                spn_segments[0].setValue(spn_divisions[0].getValue());
                break;
            default:
                break;
            }
            
        } else {
            
            switch (spn_divisions[0].getValue()) {
            case 8:
                cmb_divisions[0].select(0);
                break;
            case 16:
                cmb_divisions[0].select(1);
                break;
            case 48:
                cmb_divisions[0].select(2);
                break;
            default:
                cmb_divisions[0].select(3);
                break;
            }
            final int QUARTER = spn_divisions[0].getValue() / 4; 
            final int HALF = spn_divisions[0].getValue() / 2;
            final int THREE_OF_FOUR = spn_divisions[0].getValue() * 3 / 4;
            final int WHOLE = spn_divisions[0].getValue();
            
            if (spn_segments[0].getValue() == QUARTER && spn_divisions[0].getValue() / 4d - QUARTER == 0d) {
                cmb_segments[0].select(0);
            } else if (spn_segments[0].getValue() == HALF && spn_divisions[0].getValue() / 2d - HALF == 0d) {
                cmb_segments[0].select(1);
            } else if (spn_segments[0].getValue() == THREE_OF_FOUR && spn_divisions[0].getValue() * 3d / 4d - QUARTER == 0d) {
                cmb_segments[0].select(2);
            } else if (spn_segments[0].getValue() == WHOLE) {
                cmb_segments[0].select(3);
            } else {
                cmb_segments[0].select(4);
            }
            
        }
        
        if (cmb_type[0].getSelectionIndex() == TORUS) {
            spn_size[0].setValue(new BigDecimal(DEC_FORMAT_4F.format(spn_minor[0].getValue().intValue() * 1d / spn_major[0].getValue()))); 
        }
        
        doUpdate = false;
        
        final double deg90 = Math.PI / 2d;
        
        int divisions = spn_divisions[0].getValue();
        int segments = spn_segments[0].getValue();
        int major = spn_major[0].getValue();
        int minor = spn_minor[0].getValue().intValue();
        double width = spn_minor[0].getValue().doubleValue();
        double size = spn_size[0].getValue().doubleValue();
        int quarter_div = divisions / 4;
        
        int gcd = gcd(divisions, segments);
        
        int upper = segments / gcd;
        int lower = divisions / gcd;
        
        if (lower == 2) {
            lower = lower * 2;
            upper = upper * 2;
        } else if (lower == 1) {
            lower = 4;
            upper = 4;
        }
        
        final boolean closed = segments == divisions;
        
        final String prefix;
        final String resolution;
        final String type;
        if (divisions != 16) {
            if (divisions > 16) {
                resolution = "Hi-Res "; //$NON-NLS-1$
            } else {
                resolution = "Lo-Res "; //$NON-NLS-1$
            }
                    
            prefix = divisions + "\\"; //$NON-NLS-1$
            type = "0 !LDRAW_ORG Unofficial_" + divisions + "_Primitive\n"; //$NON-NLS-1$ //$NON-NLS-2$
        } else {
            prefix = ""; //$NON-NLS-1$
            resolution = ""; //$NON-NLS-1$
            type = "0 !LDRAW_ORG Unofficial_Primitive\n"; //$NON-NLS-1$
        }
        
        final String suffixWidth;
        final String suffixWidthTitle;
        if (width != 1d) {
            suffixWidth = "w" + removeTrailingZeros(DEC_FORMAT_4F.format(width)); //$NON-NLS-1$
            suffixWidthTitle = " Width " + removeTrailingZeros(DEC_FORMAT_4F.format(width)); //$NON-NLS-1$
        } else {
            suffixWidth = ""; //$NON-NLS-1$
            suffixWidthTitle = ""; //$NON-NLS-1$
        }
        
        final StringBuilder sb = new StringBuilder();
        
        final UserSettingState user = WorkbenchManager.getUserSettingState();
        
        String ldrawName = user.getLdrawUserName();
        String realName = user.getRealUserName();
        if (ldrawName == null || ldrawName.isEmpty()) {
            if (realName == null || realName.isEmpty()) {
                sb.append("0 Author: Primitive Generator 2\n"); //$NON-NLS-1$
            } else {
                sb.append("0 Author: " + realName + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
            }
        } else {
            if (realName == null || realName.isEmpty()) {
                sb.append("0 Author: [" + ldrawName + "]\n"); //$NON-NLS-1$ //$NON-NLS-2$                        
            } else {
                sb.append("0 Author: " + realName + " [" + ldrawName + "]\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }                
        
        sb.append(type);
        sb.append("0 !LICENSE Redistributable under CCAL version 2.0 : see CAreadme.txt\n\n"); //$NON-NLS-1$
        
        if (ccw) {
            sb.append("0 BFC CERTIFY CCW\n\n"); //$NON-NLS-1$
        } else {
            sb.append("0 BFC CERTIFY CW\n\n"); //$NON-NLS-1$
        }
        
        switch (cmb_type[0].getSelectionIndex()) {
        case CIRCLE:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "edge.dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.insert(0, "0 " + resolution + "Circle " + removeTrailingZeros2(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x1 = Math.cos(angle); 
                    double z1 = Math.sin(angle);
                    double x2 = Math.cos(nextAngle); 
                    double z2 = Math.sin(nextAngle);
                    sb.append("2 24 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                    sb.append(" 0 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                    sb.append(" 0 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            }       
                    
            break;
        case RING:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "ring" + removeTrailingZeros2(DEC_FORMAT_4F.format(size)) + suffixWidth +  " .dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.insert(0, "0 " + resolution + "Ring " + addExtraSpaces1(removeTrailingZeros(DEC_FORMAT_4F.format(size))) + " x " + removeTrailingZeros(DEC_FORMAT_4F.format(segments * 1d / divisions)) + suffixWidthTitle + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                double size2 = size + width;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x1 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size; 
                    double z1 = Math.round(Math.sin(angle) * 1E4) / 1E4 * size;
                    double x2 = Math.round(Math.cos(nextAngle) * 1E4) / 1E4 * size; 
                    double z2 = Math.round(Math.sin(nextAngle) * 1E4) / 1E4 * size;
                    
                    double x3 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size2; 
                    double z3 = Math.round(Math.sin(angle) * 1E4) / 1E4 * size2;
                    double x4 = Math.round(Math.cos(nextAngle) * 1E4) / 1E4 * size2; 
                    double z4 = Math.round(Math.sin(nextAngle) * 1E4) / 1E4 * size2;
                    if (ccw) {
                        sb.append("4 16 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x4)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z4)));                        
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    } else {
                        sb.append("4 16 "); //$NON-NLS-1$                    
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x4)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z4)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                    }
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            }
            
            break;
        case CONE:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "con" + removeTrailingZeros2(DEC_FORMAT_4F.format(size)) + suffixWidth +  " .dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            sb.insert(0, "0 " + resolution + "Cone " + addExtraSpaces1(removeTrailingZeros(DEC_FORMAT_4F.format(size))) + " x " + removeTrailingZeros(DEC_FORMAT_4F.format(segments * 1d / divisions)) + suffixWidthTitle + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                double size2 = size + width;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x1 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size; 
                    double z1 = Math.round(Math.sin(angle) * 1E4) / 1E4 * size;
                    double x2 = Math.round(Math.cos(nextAngle) * 1E4) / 1E4 * size; 
                    double z2 = Math.round(Math.sin(nextAngle) * 1E4) / 1E4 * size;
                    
                    double x3 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size2; 
                    double z3 = Math.round(Math.sin(angle) * 1E4) / 1E4 * size2;
                    double x4 = Math.round(Math.cos(nextAngle) * 1E4) / 1E4 * size2; 
                    double z4 = Math.round(Math.sin(nextAngle) * 1E4) / 1E4 * size2;
                    if (ccw) {
                        sb.append("4 16 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));                                               
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x4)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z4)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                    } else {
                        sb.append("4 16 "); //$NON-NLS-1$                    
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x4)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z4)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    }
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            }
            
            sb.append("0 // conditional lines\n"); //$NON-NLS-1$
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                double size2 = size + width;
                int off = closed ? 0 : 1;
                
                for(int i = 0; i < segments + off; i++) {
                    double nextAngle = angle + deltaAngle;
                    double prevAngle = angle - deltaAngle;
                    double x1 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size; 
                    double z1 = Math.round(Math.sin(angle) * 1E4) / 1E4 * size; 
                    double x11 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size2; 
                    double z11 = Math.round(Math.sin(angle) * 1E4) / 1E4 * size2; 
                    double x2 = Math.round(Math.cos(nextAngle) * 1E4) / 1E4 * size; 
                    double z2 = Math.round(Math.sin(nextAngle) * 1E4) / 1E4 * size; 
                    double x3 = Math.round(Math.cos(prevAngle) * 1E4) / 1E4 * size; 
                    double z3 = Math.round(Math.sin(prevAngle) * 1E4) / 1E4 * size; 
                    
                    if (!closed) { 
                        if (i == 0) {
                            x3 = size;
                            z3 = 1d - Math.sqrt(2);
                        } else if (i == segments) {
                            double strangeFactor = Math.sqrt(2) - 1d;
                            x2 = x1 + Math.cos(angle + deg90) * strangeFactor; 
                            z2 = z1 + Math.sin(angle + deg90) * strangeFactor; 
                        }
                    }
                    
                    sb.append("5 16 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                    sb.append(" 1 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x11)));
                    sb.append(" 0 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z11)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                    sb.append(" 1 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                    sb.append(" 1 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            }
            
            break;
        case TORUS:

            {
                String sweep = DEC_FORMAT_4F.format(minor * 1d / major);
                String sweep2 = sweep.replace(".", "").substring(sweep.charAt(0) == '0' ? 1 : 0, Math.min(sweep.charAt(0) == '0' ? 5 : 4, sweep.length())); //$NON-NLS-1$ //$NON-NLS-2$
                String edge = removeTrailingZeros(DEC_FORMAT_4F.format(minor * 1d / major));
                String frac = "99"; //$NON-NLS-1$
                if (upper == 1 && lower < 100) {
                    frac = lower + ""; //$NON-NLS-1$
                    if (frac.length() == 1) {
                        frac = "0" + lower; //$NON-NLS-1$
                    }
                }
                
                String tt = " Inside  1 x "; //$NON-NLS-1$
                String t = "i"; //$NON-NLS-1$
                String r = "t"; //$NON-NLS-1$
                if (minor >= major) {
                    r = "r"; //$NON-NLS-1$
                }
                if (torusType == 1) {
                    tt = " Outside  1 x "; //$NON-NLS-1$
                    t = "o"; //$NON-NLS-1$
                } else if (torusType == 2) {
                    tt = " Tube  1 x "; //$NON-NLS-1$
                    t = "q"; //$NON-NLS-1$
                    quarter_div = divisions;
                }
                
                sb.insert(0, "0 Name: " + prefix + r + frac + t + sweep2 + ".dat\n"); //$NON-NLS-1$ //$NON-NLS-2$
                sb.insert(0, "0 " + resolution + "Torus" + tt + sweep + " x " + removeTrailingZeros2(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

                sb.append("0 // Major Radius: " + major + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                sb.append("0 // Tube(Minor) Radius: " + minor + "\n"); //$NON-NLS-1$ //$NON-NLS-2$
                sb.append("0 // Segments(Sweep): " + segments + "/" + divisions + " = " + removeTrailingZeros3(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
                sb.append("0 // 1 9 0 0 0 1 0 0 0 1 0 0 0 1 4-4edge.dat\n"); //$NON-NLS-1$
                sb.append("0 // 1 12 1 0 0 " + edge + " 0 0 0 0 " + edge + " 0 1 0 4-4edge.dat\n\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

                
                // FIXME Needs implementation (Torus)!
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = -deg90;
                
                if (torusType == 0) {
                    angle = -deg90 - deg90;
                }
                
                StringBuilder[] sbs = new StringBuilder[segments];
                for (int i = 0; i < segments; i++) {
                    sbs[i] = new StringBuilder();
                }
                
                for(int i = 0; i < quarter_div; i++) {
                    double nextAngle = angle + deltaAngle;
                    double angle2 = 0d;
                    
                    double size1 = Math.round(Math.cos(angle) * 1E4) / 1E4 * size + 1d;
                    double size2 = Math.round(Math.cos(nextAngle) * 1E4) / 1E4 * size + 1d;
                    
                    double y1 = Math.round(Math.sin(angle) * 1E4) / 1E4 * -size;
                    double y2 = Math.round(Math.sin(nextAngle) * 1E4) / 1E4 * -size;
                    
                    
                    for(int j = 0; j < segments; j++) {
                        double nextAngle2 = angle2 + deltaAngle;
                        double x1 = Math.round(Math.cos(angle2) * 1E4) / 1E4 * size1; 
                        double z1 = Math.round(Math.sin(angle2) * 1E4) / 1E4 * size1;
                        double x2 = Math.round(Math.cos(nextAngle2) * 1E4) / 1E4 * size1; 
                        double z2 = Math.round(Math.sin(nextAngle2) * 1E4) / 1E4 * size1;
                        
                        double x3 = Math.round(Math.cos(angle2) * 1E4) / 1E4 * size2; 
                        double z3 = Math.round(Math.sin(angle2) * 1E4) / 1E4 * size2;
                        double x4 = Math.round(Math.cos(nextAngle2) * 1E4) / 1E4 * size2; 
                        double z4 = Math.round(Math.sin(nextAngle2) * 1E4) / 1E4 * size2;
                        StringBuilder sb3 = sbs[j];
                        StringBuilder sb2 = new StringBuilder();
                        if (ccw) {
                            sb2.append("4 16 "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y1)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));                                               
                            sb2.append(" "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y1)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                            sb2.append(" "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x4)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y2)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z4)));
                            sb2.append(" "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y2)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                        } else {
                            sb2.append("4 16 "); //$NON-NLS-1$                    
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y2)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                            sb2.append(" "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x4)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y2)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z4)));
                            sb2.append(" "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y1)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                            sb2.append(" "); //$NON-NLS-1$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                            sb2.append(" " + removeTrailingZeros(DEC_FORMAT_4F.format(y1)) + " "); //$NON-NLS-1$ //$NON-NLS-2$
                            sb2.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        }
                        sb2.append("\n"); //$NON-NLS-1$
                        sb3.append(sb2);
                        angle2 = nextAngle2;
                    }
                    angle = nextAngle;
                }
                
                for (int i = 0; i < segments; i++) {
                    sb.append(sbs[i]);
                }
                
                sb.append("0 // conditional lines\n"); //$NON-NLS-1$                                
            }
            
            break;
        case CYLINDER:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "cyli.dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.insert(0, "0 " + resolution + "Cylinder " + removeTrailingZeros2(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
          
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x1 = Math.cos(angle); 
                    double z1 = Math.sin(angle);
                    double x2 = Math.cos(nextAngle); 
                    double z2 = Math.sin(nextAngle);
                    if (ccw) {
                        sb.append("4 16 "); //$NON-NLS-1$                    
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                    } else {
                        sb.append("4 16 "); //$NON-NLS-1$                    
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 1 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    }
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            }     
            
            sb.append("0 // conditional lines\n"); //$NON-NLS-1$
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                
                int off = closed ? 0 : 1;
                
                for(int i = 0; i < segments + off; i++) {
                    double nextAngle = angle + deltaAngle;
                    double prevAngle = angle - deltaAngle;
                    double x1 = Math.cos(angle); 
                    double z1 = Math.sin(angle);
                    double x2 = Math.cos(nextAngle); 
                    double z2 = Math.sin(nextAngle);
                    double x3 = Math.cos(prevAngle); 
                    double z3 = Math.sin(prevAngle);
                    
                    if (!closed) { 
                        if (i == 0) {
                            x3 = 1d;
                            z3 = 1d - Math.sqrt(2);
                        } else if (i == segments) {
                            double strangeFactor = Math.sqrt(2) - 1d;
                            x2 = x1 + Math.cos(angle + deg90) * strangeFactor; 
                            z2 = z1 + Math.sin(angle + deg90) * strangeFactor; 
                        }
                    }
                    
                    sb.append("5 16 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                    sb.append(" 1 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                    sb.append(" 0 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                    sb.append(" 1 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                    sb.append(" "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                    sb.append(" 1 "); //$NON-NLS-1$
                    sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            }
            break;
        case DISC:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "disc.dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.insert(0, "0 " + resolution + "Disc " + removeTrailingZeros2(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x1 = Math.cos(angle); 
                    double z1 = Math.sin(angle);
                    double x2 = Math.cos(nextAngle); 
                    double z2 = Math.sin(nextAngle);                    
                    if (ccw) {
                        sb.append("3 16 0 0 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));    
                    } else {
                        sb.append("3 16 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" 0 0 0"); //$NON-NLS-1$
                    }
                    
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            } 
            
            break;
        case DISC_NEGATIVE:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "ndis.dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.insert(0, "0 " + resolution + "Disc Negative " + removeTrailingZeros2(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = 0d;
                int s1 = divisions / 4;
                int s2 = s1 * 2;
                int s3 = s1 * 3;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x2 = Math.cos(angle); 
                    double z2 = Math.sin(angle);
                    double x1 = Math.cos(nextAngle); 
                    double z1 = Math.sin(nextAngle);  
                    double x3; 
                    double z3;  
                    if (i < s1) {
                        x3 = 1d; 
                        z3 = 1d;
                    } else if (i < s2) {
                        x3 = -1d; 
                        z3 = 1d;
                    } else if (i < s3) {
                        x3 = -1d; 
                        z3 = -1d;
                    } else {
                        x3 = 1d; 
                        z3 = -1d;  
                    }
                    if (ccw) {
                        sb.append("3 16 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));    
                    } else {
                        sb.append("3 16 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x3)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z3)));
                    }
                    
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            } 
            
            break;
        case CHORD:
            sb.insert(0, "0 Name: " + prefix + upper + "-" + lower + "chrd.dat\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            sb.insert(0, "0 " + resolution + "Chord " + removeTrailingZeros2(DEC_FORMAT_4F.format(segments * 1d / divisions)) + "\n"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
     
            {
                double deltaAngle = Math.PI * 2d / divisions;
                double angle = deltaAngle;
                segments = segments - 1;
                for(int i = 0; i < segments; i++) {
                    double nextAngle = angle + deltaAngle;
                    double x1 = Math.cos(angle); 
                    double z1 = Math.sin(angle);
                    double x2 = Math.cos(nextAngle); 
                    double z2 = Math.sin(nextAngle);                    
                    if (ccw) {
                        sb.append("3 16 1 0 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));    
                    } else {
                        sb.append("3 16 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x2)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z2)));
                        sb.append(" "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(x1)));
                        sb.append(" 0 "); //$NON-NLS-1$
                        sb.append(removeTrailingZeros(DEC_FORMAT_4F.format(z1)));
                        sb.append(" 1 0 0"); //$NON-NLS-1$
                    }
                    
                    sb.append("\n"); //$NON-NLS-1$
                    angle = nextAngle;
                }
            } 
            
            break;
        default:
            break;
        }
        
        sb.append("0 // Build by Primitive Generator 2"); //$NON-NLS-1$
        
        if (standard) {
            lbl_standard[0].setText("STANDARD"); //$NON-NLS-1$
        } else {
            lbl_standard[0].setText("NON-STANDARD"); //$NON-NLS-1$
        }
        txt_data[0].setText(sb.toString());
    }

    private int gcd(int a, int b) {
        while (b > 0)
        {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private String removeTrailingZeros(String str) {
        StringBuilder sb = new StringBuilder();
        if (str.contains(".")) { //$NON-NLS-1$
            final int len = str.length();
            boolean nonZeroFound = false;
            for (int i = len - 1; i > -1; i--) {
                char c = str.charAt(i);
                if (c == ',') {
                    continue;
                }
                if (!nonZeroFound) {                    
                    if (c == '0') {
                        
                    } else if (c == '.') {
                        nonZeroFound = true;
                    } else {
                        sb.insert(0, c);
                        nonZeroFound = true;
                    }
                } else {
                    sb.insert(0, c);
                }
            }
            String result = sb.toString();
            if (result.equals("-0")) { //$NON-NLS-1$
                result = "0"; //$NON-NLS-1$
            }
            return result;
        } else {
            return str;
        }
    }
    
    private String removeTrailingZeros2(String str) {
        StringBuilder sb = new StringBuilder();
        String result = str;
        if (str.contains(".")) { //$NON-NLS-1$
            final int len = str.length();
            boolean nonZeroFound = false;
            for (int i = len - 1; i > -1; i--) {
                char c = str.charAt(i);
                if (c == ',') {
                    continue;
                }
                if (!nonZeroFound) {                    
                    if (c == '0') {
                        
                    } else if (c == '.') {
                        nonZeroFound = true;
                    } else {
                        sb.insert(0, c);
                        nonZeroFound = true;
                    }
                } else {
                    sb.insert(0, c);
                }
            }
            result = sb.toString();
            if (result.equals("-0")) { //$NON-NLS-1$
                result = "0"; //$NON-NLS-1$
            }            
        }
        if (!result.contains(".") && !result.equals("0")) { //$NON-NLS-1$ //$NON-NLS-2$
            result = result + ".0"; //$NON-NLS-1$
        }
        return result;
    }
    
    private String removeTrailingZeros3(String str) {
        StringBuilder sb = new StringBuilder();
        String result = str;
        if (str.contains(".")) { //$NON-NLS-1$
            final int len = str.length();
            boolean nonZeroFound = false;
            for (int i = len - 1; i > -1; i--) {
                char c = str.charAt(i);
                if (c == ',') {
                    continue;
                }
                if (!nonZeroFound) {                    
                    if (c == '0') {
                        
                    } else if (c == '.') {
                        nonZeroFound = true;
                    } else {
                        sb.insert(0, c);
                        nonZeroFound = true;
                    }
                } else {
                    sb.insert(0, c);
                }
            }
            result = sb.toString();
            if (result.equals("-0")) { //$NON-NLS-1$
                result = "0"; //$NON-NLS-1$
            }            
        }
        int index = -1;
        if (!result.contains(".") && !result.equals("0")) { //$NON-NLS-1$ //$NON-NLS-2$
            result = result + ".00"; //$NON-NLS-1$
        } else if ((index = result.indexOf(".")) != -1) { //$NON-NLS-1$
            if (result.length() - index < 2) {
                result = result + "0"; //$NON-NLS-1$
            }
        }
        return result;
    }
    
    private String addExtraSpaces1(String str) {
        final int len = str.length();
        switch (len) {
        case 1:
            return " " + str; //$NON-NLS-1$
        default:
            break;
        }
        return str;
    }
}