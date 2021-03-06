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
package org.nschmidt.ldparteditor.dialogs.unificator;

import java.math.BigDecimal;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.nschmidt.ldparteditor.helpers.composite3d.UnificatorSettings;
import org.nschmidt.ldparteditor.i18n.I18n;
import org.nschmidt.ldparteditor.widgets.BigDecimalSpinner;

/**
 * The unificator dialog
 * <p>
 * Note: This class should not be instantiated, it defines the gui layout and no
 * business logic.
 *
 * @author nils
 *
 */
class UnificatorDesign extends Dialog {

    final UnificatorSettings us;
    final BigDecimalSpinner[] spn_vertexThreshold = new BigDecimalSpinner[1];
    final BigDecimalSpinner[] spn_subfileThreshold = new BigDecimalSpinner[1];

    final Combo[] cmb_whatToUnify = new Combo[1];
    final Combo[] cmb_scope = new Combo[1];

    // Use final only for subclass/listener references!

    UnificatorDesign(Shell parentShell, UnificatorSettings us) {
        super(parentShell);
        this.us = us;
    }

    /**
     * Create contents of the dialog.
     *
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite cmp_container = (Composite) super.createDialogArea(parent);
        GridLayout gridLayout = (GridLayout) cmp_container.getLayout();
        gridLayout.verticalSpacing = 10;
        gridLayout.horizontalSpacing = 10;

        Label lbl_specify = new Label(cmp_container, SWT.NONE);
        lbl_specify.setText(I18n.UNIFICATOR_Title);

        Label lbl_separator = new Label(cmp_container, SWT.SEPARATOR | SWT.HORIZONTAL);
        lbl_separator.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));

        Label lbl_hint = new Label(cmp_container, SWT.NONE);
        lbl_hint.setText(I18n.UNIFICATOR_VertexUnifiation);

        BigDecimalSpinner spn_vertexThreshold = new BigDecimalSpinner(cmp_container, SWT.NONE);
        this.spn_vertexThreshold [0] = spn_vertexThreshold;
        spn_vertexThreshold.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        spn_vertexThreshold.setMaximum(new BigDecimal(1000));
        spn_vertexThreshold.setMinimum(new BigDecimal(0));
        spn_vertexThreshold.setValue(us.getVertexThreshold());

        Label lbl_precision = new Label(cmp_container, SWT.NONE);
        lbl_precision.setText(I18n.UNIFICATOR_VertexSnap);

        BigDecimalSpinner spn_subfileThreshold = new BigDecimalSpinner(cmp_container, SWT.NONE);
        this.spn_subfileThreshold [0] = spn_subfileThreshold;
        spn_subfileThreshold.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        spn_subfileThreshold.setMaximum(new BigDecimal(1000));
        spn_subfileThreshold.setMinimum(new BigDecimal(0));
        spn_subfileThreshold.setValue(us.getSubvertexThreshold());

        Label lbl_splitPlane = new Label(cmp_container, SWT.NONE);
        lbl_splitPlane.setText(I18n.UNIFICATOR_SnapOn);

        {
            Combo cmb_splitPlane = new Combo(cmp_container, SWT.READ_ONLY);
            this.cmb_whatToUnify[0] = cmb_splitPlane;
            cmb_splitPlane.setItems(new String[] {I18n.UNIFICATOR_Vertices, I18n.UNIFICATOR_SubpartVertices, I18n.UNIFICATOR_VerticesSubpartVertices});
            cmb_splitPlane.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            cmb_splitPlane.setText(cmb_splitPlane.getItem(us.getSnapOn()));
            cmb_splitPlane.select(us.getSnapOn());
        }

        Combo cmb_scope = new Combo(cmp_container, SWT.READ_ONLY);
        this.cmb_scope[0] = cmb_scope;
        cmb_scope.setItems(new String[] {I18n.UNIFICATOR_ScopeFile, I18n.UNIFICATOR_ScopeSelection});
        cmb_scope.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
        cmb_scope.setText(cmb_scope.getItem(us.getScope()));
        cmb_scope.select(us.getScope());

        cmp_container.pack();
        return cmp_container;
    }

    /**
     * Create contents of the button bar.
     *
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, I18n.DIALOG_OK, true);
        createButton(parent, IDialogConstants.CANCEL_ID, I18n.DIALOG_Cancel, false);
    }

    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return super.getInitialSize();
    }

}
