package com.ansorgit.intellij.modulelibs.projectView.nodes;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;

/**
 * A psi file node which is only equal to itself.
 * This avoids the selection jumping in the tree while opening and closing packages of library dependencies.
 *
 * @author jansorg
 */
public class FixedPsiFileNode extends PsiFileNode {
    public FixedPsiFileNode(Project project, PsiFile value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    @Override
    public Collection<AbstractTreeNode> getChildrenImpl() {
        return Collections.emptyList();
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        return false;
    }

    @Override
    public boolean canRepresent(Object element) {
        return false; //this == element;
    }

}
