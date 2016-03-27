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

import java.util.ArrayList;
import java.util.Set;

class VM11HideShow extends VM10Selector {

    private ArrayList<Boolean> state = new ArrayList<Boolean>();

    protected VM11HideShow(DatFile linkedDatFile) {
        super(linkedDatFile);
    }

    public synchronized void showHidden() {
        for (GData gd : dataToHide) {
            gd.show();
        }
        dataToHide.clear();
    }

    public void hideSelection() {
        for (GData1 data : selectedSubfiles) {
            hide(data);
        }
        for (GData2 data : selectedLines) {
            hide(data);
        }
        for (GData3 data : selectedTriangles) {
            hide(data);
        }
        for (GData4 data : selectedQuads) {
            hide(data);
        }
        for (GData5 data : selectedCondlines) {
            hide(data);
        }
        for (Vertex vert : selectedVertices) {
            Set<VertexManifestation> m = vertexLinkedToPositionInFile.get(vert);
            boolean isHidden = true;
            for (VertexManifestation vm : m) {
                if (vm.getGdata().type() != 0 && vm.getGdata().visible) {
                    isHidden = false;
                    break;
                }
            }
            if (isHidden)
                hiddenVertices.add(vert);
        }
        clearSelection();
    }

    private void hide(GData gdata) {
        gdata.hide();
        hiddenData.add(gdata);
    }

    public void showAll() {
        cleanupHiddenData();
        for (GData ghost : hiddenData) {
            ghost.show();
        }
        hiddenVertices.clear();
        hiddenData.clear();
    }

    public void backupHideShowState() {
        if (hiddenData.size() > 0) {
            state.clear();
            backup(linkedDatFile.getDrawChainStart(), state);
        }
    }

    private void backup(GData g, ArrayList<Boolean> s) {
        s.add(g.visible);
        while ((g = g.getNext()) != null) {
            s.add(g.visible);
            if (g.type() == 1) {
                backup(((GData1) g).myGData, s);
            }
        }
    }

    public void restoreHideShowState() {
        if (state.size() > 0) {
            restore(linkedDatFile.getDrawChainStart(), state, new int[]{0}, state.size());
            state.clear();
        }
    }

    private void restore(GData g, ArrayList<Boolean> s, int[] c, final int size) {
        g.visible = s.get(c[0]);
        if (!g.visible) {
            hiddenData.add(g);
        }
        c[0]++;
        if (c[0] == size) return;
        while ((g = g.getNext()) != null) {
            g.visible = s.get(c[0]);
            if (!g.visible) {
                hiddenData.add(g);
            }
            c[0]++;
            if (c[0] == size) return;
            if (g.type() == 1) {
                restore(((GData1) g).myGData, s, c, size);
            }
        }
    }

}
