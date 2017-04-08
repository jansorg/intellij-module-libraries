package com.ansorgit.intellij.modulelibs.projectView.nodes;

import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.ProjectViewDirectoryHelper;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.ide.projectView.impl.nodes.PsiFileNode;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.ide.util.treeView.NodeDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A psi directory node which is only equal to itself.
 * This avoids the selection jumping in the tree while opening and closing packages of library dependencies.
 * <p/>
 * User: jansorg
 * Date: 28.10.10
 * Time: 19:32
 */
public class FixedPsiDirectoryNode extends PsiDirectoryNode {
    public FixedPsiDirectoryNode(Project project, PsiDirectory value, ViewSettings viewSettings) {
        super(project, value, viewSettings);
    }

    @Override
    public Collection<AbstractTreeNode> getChildrenImpl() {
        VirtualFile virtualFile = getVirtualFile();
        if (virtualFile != null && !virtualFile.isInLocalFileSystem() && getParent() instanceof PsiDirectoryNode) {
            PsiDirectoryNode parent = (PsiDirectoryNode) getParent();

            if (!parent.getVirtualFile().isInLocalFileSystem()) {
                return Collections.emptyList();
            }
        }

        Collection<AbstractTreeNode> childs = super.getChildrenImpl();

        List<AbstractTreeNode> wrappedNodes = new ArrayList<AbstractTreeNode>();
        for (AbstractTreeNode node : childs) {
            wrappedNodes.add(wrapNode(node));
        }

        return wrappedNodes;
    }

    private AbstractTreeNode wrapNode(AbstractTreeNode node) {
        if (node instanceof PsiDirectoryNode) {
            return new FixedPsiDirectoryNode(node.getProject(), ((PsiDirectoryNode) node).getValue(), ((PsiDirectoryNode) node).getSettings());
        } else if (node instanceof PsiFileNode) {
            return new FixedPsiFileNode(node.getProject(), ((PsiFileNode) node).getValue(), ((PsiFileNode) node).getSettings());
        }

        return node;
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        if (file.isInLocalFileSystem()) {
            return super.contains(file);
        }

        return false;
    }

    @Override
    public boolean canRepresent(Object element) {
        return false; //super.canRepresent(element);
    }

    @Override
    public boolean equals(Object object) {
        if (getVirtualFile() != null && getVirtualFile().isInLocalFileSystem()) {
            return super.equals(object);
        }

        //return super.equals(object);
        return this == object;
    }
}
