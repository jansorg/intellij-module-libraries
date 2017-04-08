package com.ansorgit.intellij.modulelibs.projectView.nodes;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ProjectViewNode;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.util.treeView.AbstractTreeNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.Icons;
import com.intellij.util.PlatformIcons;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * This is just a wrapper which takes the original children nodes of "External libraries" and displays them
 * in a separate node named "All libraries".
 */
public class AllAggregatedExternalLibsNode extends ProjectViewNode<String> {
    private Collection<? extends AbstractTreeNode> children;

    /**
     * Creates an instance of the project view node.
     *
     * @param project      the project containing the node.
     * @param viewSettings the settings of the project view.
     * @param children
     */
    public AllAggregatedExternalLibsNode(Project project, ViewSettings viewSettings, Collection<? extends AbstractTreeNode> children) {
        super(project, "All libraries", viewSettings);
        this.children = children;
    }

    @Override
    public boolean contains(@NotNull VirtualFile file) {
        //a file should be opened in the module group instead
        return false;
    }

    @NotNull
    @Override
    public Collection<? extends AbstractTreeNode> getChildren() {
        return children;
    }

    @Override
    protected void update(PresentationData presentation) {
        presentation.setPresentableText("All libraries (aggregated)");
        presentation.setIcon(PlatformIcons.LIBRARY_ICON);
    }
}
