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
package org.nschmidt.ldparteditor.dialogs.startup;

import static org.nschmidt.ldparteditor.helpers.WidgetUtility.WidgetUtil;

import java.io.File;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Shell;
import org.nschmidt.ldparteditor.enums.MyLanguage;
import org.nschmidt.ldparteditor.helpers.FileHelper;
import org.nschmidt.ldparteditor.logger.NLogger;
import org.nschmidt.ldparteditor.workbench.UserSettingState;
import org.nschmidt.ldparteditor.workbench.WorkbenchManager;

/**
 * This first dialog - shown on startup - asks for mandatory information about
 * the environment.
 * <p>
 * Note: This class should be instantiated, it defines all listeners and part of
 * the business logic. It overrides the {@code open()} method to invoke the
 * listener definitions ;)
 *
 * @author nils
 *
 */
public class StartupDialog extends StartupDesign {

    private String partAuthoringPath = ""; //$NON-NLS-1$
    private String ldrawPath = ""; //$NON-NLS-1$

    private String unofficialPath = ""; //$NON-NLS-1$
    private String ldrawUserName = ""; //$NON-NLS-1$
    private String license = "0 !LICENSE Redistributable under CCAL version 2.0 : see CAreadme.txt"; //$NON-NLS-1$
    private String realName = ""; //$NON-NLS-1$
    private Locale locale = Locale.US;

    private boolean path1valid = false;
    private boolean path2valid = false;
    private boolean path3valid = false;

    /**
     * Create the dialog.
     *
     * @param parentShell
     */
    public StartupDialog(Shell parentShell) {
        super(parentShell);
    }

    @Override
    public int open() {

        String ldrawDir = System.getenv("LDRAWDIR"); //$NON-NLS-1$
        if (ldrawDir != null) {
            setLdrawPath(ldrawDir);
            setUnofficialPath(ldrawDir + File.separator + "Unofficial"); //$NON-NLS-1$
            path1valid = FileHelper.canReadFromPath(ldrawPath);
            path3valid = FileHelper.canReadFromPath(unofficialPath);
        }

        super.create();
        btn_ok[0].setEnabled(false);
        // MARK All final listeners will be configured here..
        WidgetUtil(btn_browseLdrawPath[0]).addSelectionListener(e -> {
            DirectoryDialog dlg = new DirectoryDialog(getShell());

            // Set the initial filter to null
            dlg.setFilterPath(null);

            // Change the title bar text
            dlg.setText("Define the LDraw Folder Path:"); //$NON-NLS-1$ NO_I18N!!

            // Customizable message displayed in the dialog
            dlg.setMessage("Select a Directory"); //$NON-NLS-1$ NO_I18N!!

            // Calling open() will open and run the dialog.
            // It will return the selected directory, or
            // null if user cancels
            String dir = dlg.open();
            if (dir != null) {
                // Set the text box to the new selection
                txt_ldrawPath[0].setText(dir);
                ldrawPath = dir;
                path1valid = FileHelper.canReadFromPath(ldrawPath);

                if (path1valid && unofficialPath.isEmpty()) {
                    if (ldrawPath.endsWith(File.separator)) {
                        unofficialPath = ldrawPath + "Unofficial"; //$NON-NLS-1$
                    } else {
                        unofficialPath = ldrawPath + File.separator + "Unofficial"; //$NON-NLS-1$
                    }
                    if (FileHelper.canWriteToPath(ldrawPath)) {
                        try {
                            File unofficialFolder = new File(unofficialPath);
                            if (!unofficialFolder.exists()) {
                                unofficialFolder.mkdir();
                            }
                            path3valid = true;
                        } catch (SecurityException s) {
                            NLogger.error(getClass(), "Failed to create unofficial library folder."); //$NON-NLS-1$
                            unofficialPath = ""; //$NON-NLS-1$
                        }
                    }
                    txt_unofficialPath[0].setText(unofficialPath);
                }

                btn_ok[0].setEnabled(path1valid && path2valid && path3valid && !ldrawUserName.isEmpty() && !license.isEmpty() && !realName.isEmpty());
            }
        });
        WidgetUtil(btn_browseAuthoringPath[0]).addSelectionListener(e -> {
            DirectoryDialog dlg = new DirectoryDialog(getShell());

            // Set the initial filter to null
            dlg.setFilterPath(null);

            // Change the title bar text
            dlg.setText("Where is your parts authoring folder located?"); //$NON-NLS-1$ NO_I18N!!

            // Customizable message displayed in the dialog
            dlg.setMessage("Select a Directory"); //$NON-NLS-1$ NO_I18N!!

            // Calling open() will open and run the dialog.
            // It will return the selected directory, or
            // null if user cancels
            String dir = dlg.open();
            if (dir != null) {
                // Set the text box to the new selection
                txt_partAuthoringPath[0].setText(dir);
                partAuthoringPath = dir;
                path2valid = FileHelper.canReadFromPath(partAuthoringPath) && FileHelper.canWriteToPath(partAuthoringPath);
                btn_ok[0].setEnabled(path1valid && path2valid && path3valid && !ldrawUserName.isEmpty() && !license.isEmpty() && !realName.isEmpty());
            }
        });
        WidgetUtil(btn_browseUnofficialPath[0]).addSelectionListener(e -> {
            DirectoryDialog dlg = new DirectoryDialog(getShell());

            // Set the initial filter to null
            dlg.setFilterPath(null);

            // Change the title bar text
            dlg.setText("Where is your unofficial parts folder located?"); //$NON-NLS-1$ NO_I18N!!

            // Customizable message displayed in the dialog
            dlg.setMessage("Select a Directory"); //$NON-NLS-1$ NO_I18N!!

            // Calling open() will open and run the dialog.
            // It will return the selected directory, or
            // null if user cancels
            String dir = dlg.open();
            if (dir != null) {
                // Set the text box to the new selection
                txt_unofficialPath[0].setText(dir);
                unofficialPath = dir;
                path3valid = FileHelper.canReadFromPath(unofficialPath);
                btn_ok[0].setEnabled(path1valid && path2valid && path3valid && !ldrawUserName.isEmpty() && !license.isEmpty() && !realName.isEmpty());
            }
        });
        txt_ldrawUserName[0].addListener(SWT.Modify, e -> {
            ldrawUserName = txt_ldrawUserName[0].getText();
            btn_ok[0].setEnabled(path1valid && path2valid && !ldrawUserName.isEmpty() && !license.isEmpty() && !realName.isEmpty());
        });
        txt_realName[0].addListener(SWT.Modify, e -> {
            realName = txt_realName[0].getText();
            btn_ok[0].setEnabled(path1valid && path2valid && !ldrawUserName.isEmpty() && !license.isEmpty() && !realName.isEmpty());
        });
        cmb_license[0].addListener(SWT.Modify, e -> {
            license = cmb_license[0].getText();
            btn_ok[0].setEnabled(path1valid && path2valid && !ldrawUserName.isEmpty() && !license.isEmpty() && !realName.isEmpty());
        });
        cmb_locale[0].addListener(SWT.Modify, e -> {
            if (localeMap.containsKey(cmb_locale[0].getText())) {
                locale = localeMap.get(cmb_locale[0].getText());
            }
        });
        btn_ok[0].addListener(SWT.Selection, event -> {
            UserSettingState userSettingState = new UserSettingState();
            userSettingState.setAuthoringFolderPath(partAuthoringPath);
            userSettingState.setLdrawFolderPath(ldrawPath);
            userSettingState.setUnofficialFolderPath(unofficialPath);
            userSettingState.setLdrawUserName(ldrawUserName);
            userSettingState.setLicense(license);
            userSettingState.setRealUserName(realName);
            userSettingState.setUsingRelativePaths(false);
            userSettingState.setLocale(locale);
            MyLanguage.LOCALE = locale;
            WorkbenchManager.setUserSettingState(userSettingState);
        });
        return super.open();
    }


    public void setLdrawPath(String ldrawPath) {
        this.ldrawPath = ldrawPath;
    }

    public void setUnofficialPath(String unofficialPath) {
        this.unofficialPath = unofficialPath;
    }
}
